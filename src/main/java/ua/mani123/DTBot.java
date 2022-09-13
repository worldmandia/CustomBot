package ua.mani123;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.mani123.action.ActionUtils;
import ua.mani123.activity.ActivityUtils;
import ua.mani123.command.CommandUtils;
import ua.mani123.config.BotConfig;
import ua.mani123.config.BotFilesManager;
import ua.mani123.interaction.interactions.InteractionUtils;
import ua.mani123.listeners.AutoComplete;
import ua.mani123.listeners.ButtonListener;
import ua.mani123.listeners.onReadyListener;
import ua.mani123.listeners.UseCommand;

import javax.security.auth.login.LoginException;

public class DTBot {
    private static BotConfig config;
    private static BotConfig lang;
    private static BotConfig interaction;
    private static BotConfig commands;
    private static BotConfig actions;
    private static BotConfig activities;
    private static BotConfig database;
    private static String TOKEN;
    private static final Logger logger = LoggerFactory.getLogger(DTBot.class);
    private static DefaultShardManagerBuilder BotApi;
    private static boolean botEnabled = true;

    // Main method

    public static void main(String[] args) {
        getLogger().info("Loading configs...");
        createConfigs();
        loadConfigs();
        getLogger().info("Load data...");
        loadUtils();
        getLogger().info("Start bot...");
        startBot();
    }

    private static void createConfigs() {
        BotFilesManager.createFile(Constants.DEFAULT_TOKEN_CONFIG_NAME);
        BotFilesManager.createResourceFile("default-config.toml", Constants.DEFAULT_CONFIG_NAME);
        BotFilesManager.createResourceFile("default-lang.toml", Constants.DEFAULT_LANG_NAME);
        BotFilesManager.createResourceFile("default-interaction.toml", Constants.DEFAULT_INTERACTION_NAME);
        BotFilesManager.createResourceFile("default-commands.toml", Constants.DEFAULT_COMMAND_NAME);
        BotFilesManager.createResourceFile("default-actions.toml", Constants.DEFAULT_ACTION_NAME);
        BotFilesManager.createResourceFile("default-activities.toml", Constants.DEFAULT_ACTIVITIES_NAME);
        BotFilesManager.createResourceFile("default-database.toml", Constants.DEFAULT_DATABASE_NAME);
    }

    private static void loadConfigs() {
        try {
            TOKEN = BotFilesManager.readFile(Constants.DEFAULT_TOKEN_CONFIG_NAME).readLine();
            config = new BotConfig(Constants.DEFAULT_CONFIG_NAME);
            lang = new BotConfig(Constants.DEFAULT_LANG_NAME);
            interaction = new BotConfig(Constants.DEFAULT_INTERACTION_NAME);
            commands = new BotConfig(Constants.DEFAULT_COMMAND_NAME);
            actions = new BotConfig(Constants.DEFAULT_ACTION_NAME);
            activities = new BotConfig(Constants.DEFAULT_ACTIVITIES_NAME);
            database = new BotConfig(Constants.DEFAULT_DATABASE_NAME);
        } catch (Exception e) {
            getLogger().error(e.getMessage() + ", check or reset cfg files");
        }
    }

    private static void startBot() {
        try {
            BotApi = DefaultShardManagerBuilder.createDefault(TOKEN);
            BotApi.addEventListeners(
                    new AutoComplete(),
                    new ButtonListener(),
                    new onReadyListener(),
                    new UseCommand()
            );
            BotApi.setCompression(Compression.ZLIB);
            BotApi.setActivity(Activity.of(Activity.ActivityType.LISTENING, "Loading..."));
            BotApi.setStatus(OnlineStatus.valueOf(getConfig().get("bot-ticket.status").toUpperCase()));
            BotApi.setMemberCachePolicy(MemberCachePolicy.ALL);
            BotApi.setEnabledIntents(
                    GatewayIntent.GUILD_MEMBERS,
                    GatewayIntent.GUILD_VOICE_STATES,
                    GatewayIntent.GUILD_EMOJIS_AND_STICKERS,
                    GatewayIntent.GUILD_PRESENCES,
                    GatewayIntent.MESSAGE_CONTENT);
            BotApi.setChunkingFilter(ChunkingFilter.ALL);
            BotApi.setUseShutdownNow(true);
            BotApi.build();
        } catch (LoginException e) {
            getLogger().error("LoginException, wrong TOKEN");
        }
    }

    private static void loadUtils() {
        // load data from cfg
        InteractionUtils.preLoad();
        getLogger().info("Load Actions");
        ActionUtils.load();
        getLogger().info("Load Interactions");
        InteractionUtils.load();
        getLogger().info("Load Commands");
        CommandUtils.load();
        getLogger().info("Load activities");
        ActivityUtils.load();
        // catch CTRL+C
        Runtime.getRuntime().addShutdownHook(new Thread(DTBot::saveAll, "Shutdown-thread"));
    }

    private static void saveAll() {
        getLogger().warn("Dont close app with CTRL+C, use /close");
        botEnabled = false;
        config.save();
        lang.save();
        interaction.save();
        commands.save();
        actions.save();
        activities.save();
        database.save();
    }

    // Getters


    public static BotConfig getDatabase() {
        return database;
    }

    public static BotConfig getActivities() {
        return activities;
    }

    public static boolean isBotEnabled() {
        return botEnabled;
    }

    public static BotConfig getConfig() {
        return config;
    }

    public static BotConfig getLang() {
        return lang;
    }

    public static BotConfig getInteraction() {
        return interaction;
    }

    public static BotConfig getCommands() {
        return commands;
    }

    public static BotConfig getActions() {
        return actions;
    }

    public static Logger getLogger() {
        return logger;
    }

    public static DefaultShardManagerBuilder getBotApi() {
        return BotApi;
    }
}
