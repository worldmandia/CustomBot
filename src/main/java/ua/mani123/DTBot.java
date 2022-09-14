package ua.mani123;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.exceptions.InvalidTokenException;
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
import ua.mani123.interaction.interactions.InteractionUtils;
import ua.mani123.listeners.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;

public class DTBot {
    private static CommentedFileConfig config;
    private static CommentedFileConfig lang;
    private static CommentedFileConfig interaction;
    private static CommentedFileConfig commands;
    private static CommentedFileConfig actions;
    private static CommentedFileConfig activities;
    private static CommentedFileConfig database;
    private static final Logger logger = LoggerFactory.getLogger(DTBot.class);
    private static DefaultShardManagerBuilder BotApi;

    // Main method

    public static void main(String[] args) {
        try {
            getLogger().info("Loading configs...");
            createConfigs();
            loadConfigs();
            getLogger().info("Load data...");
            loadUtils();
            getLogger().info("Start bot...");
            startBot();
        } catch (Exception e) {
            DTBot.getLogger().error(e.getMessage());
        }
    }

    private static void createConfigs() {
        createResourceFile(Constants.DEFAULT_CONFIG_NAME, "default-config.toml");
        config = CommentedFileConfig.builder(Constants.DEFAULT_CONFIG_NAME).defaultResource("default-config.toml").autosave().build();
        createResourceFile(Constants.DEFAULT_LANG_NAME, "default-lang.toml");
        lang = CommentedFileConfig.builder(Constants.DEFAULT_LANG_NAME).defaultResource("default-lang.toml").autosave().build();
        createResourceFile(Constants.DEFAULT_INTERACTION_NAME, "default-interaction.toml");
        interaction = CommentedFileConfig.builder(Constants.DEFAULT_INTERACTION_NAME).defaultResource("default-interaction.toml").autosave().build();
        createResourceFile(Constants.DEFAULT_COMMAND_NAME, "default-commands.toml");
        commands = CommentedFileConfig.builder(Constants.DEFAULT_COMMAND_NAME).defaultResource("default-commands.toml").autosave().build();
        createResourceFile(Constants.DEFAULT_ACTION_NAME, "default-actions.toml");
        actions = CommentedFileConfig.builder(Constants.DEFAULT_ACTION_NAME).defaultResource("default-actions.toml").autosave().build();
        createResourceFile(Constants.DEFAULT_ACTIVITIES_NAME, "default-activities.toml");
        activities = CommentedFileConfig.builder(Constants.DEFAULT_ACTIVITIES_NAME).defaultResource("default-activities.toml").autosave().build();
        createResourceFile(Constants.DEFAULT_DATABASE_NAME, "default-database.toml");
        database = CommentedFileConfig.builder(Constants.DEFAULT_DATABASE_NAME).defaultResource("default-database.toml").autosave().build();
    }

    private static void loadConfigs() {
        config.load();
        lang.load();
        interaction.load();
        commands.load();
        actions.load();
        activities.load();
        database.load();
    }

    private static void startBot() {
        try {
            BotApi = DefaultShardManagerBuilder.createDefault(config.get("bot.bot-token"));
            BotApi.addEventListeners(
                    new AutoComplete(),
                    new ButtonListener(),
                    new onReadyListener(),
                    new UseCommand(),
                    new GuildListener(),
                    new MemberListener()
            );
            BotApi.setCompression(Compression.ZLIB);
            BotApi.setActivity(Activity.of(Activity.ActivityType.LISTENING, "Loading..."));
            BotApi.setStatus(OnlineStatus.valueOf(config.get("bot.status").toString().toUpperCase()));
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
        } catch (InvalidTokenException e) {
            getLogger().error("InvalidTokenException, wrong TOKEN");
            System.exit(0);
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
        config.save();
        lang.save();
        interaction.save();
        commands.save();
        actions.save();
        activities.save();
        database.save();
    }

    public static void createResourceFile(String newName, String resourceName) {
        try {
            InputStream inputStream = DTBot.class.getClassLoader().getResourceAsStream(resourceName);
            Path of = Path.of(newName);
            if (inputStream == null) {
                DTBot.getLogger().error("Not found file in resources: " + resourceName);
            } else if (Files.notExists(of, LinkOption.NOFOLLOW_LINKS)) {
                DTBot.getLogger().info("File created: " + newName);
                Files.copy(inputStream, of);
            } else {
                DTBot.getLogger().info(newName + " found");
            }
        } catch (IOException e) {
            DTBot.getLogger().info(e.getMessage() + " found");
        }
    }

    // Getters

    public static CommentedFileConfig getDatabase() {
        return database;
    }

    public static CommentedFileConfig getActivities() {
        return activities;
    }

    public static CommentedFileConfig getConfig() {
        return config;
    }

    public static CommentedFileConfig getLang() {
        return lang;
    }

    public static CommentedFileConfig getInteraction() {
        return interaction;
    }

    public static CommentedFileConfig getCommands() {
        return commands;
    }

    public static CommentedFileConfig getActions() {
        return actions;
    }

    public static Logger getLogger() {
        return logger;
    }

    public static DefaultShardManagerBuilder getBotApi() {
        return BotApi;
    }
}
