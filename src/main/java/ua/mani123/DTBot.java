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
import ua.mani123.config.BotConfig;
import ua.mani123.config.BotFilesManager;
import ua.mani123.listeners.AutoComplete;
import ua.mani123.listeners.ButtonListener;
import ua.mani123.listeners.GuildListeners;
import ua.mani123.listeners.UseCommand;

import javax.security.auth.login.LoginException;

public class DTBot {
    protected static BotConfig config;
    protected static BotConfig lang;

    protected static BotConfig interaction;
    protected static BotConfig tickets;
    protected static BotConfig commands;
    protected static BotConfig actions;
    protected static String TOKEN;
    protected static Logger logger = LoggerFactory.getLogger(DTBot.class);
    protected static DefaultShardManagerBuilder BotApi;

    // Main method

    public static void main(String[] args) {
        createConfigs();
        loadConfigs();
        startBot();
        loadUtils();
    }

    private static void createConfigs() {
        BotFilesManager.createFile(Constants.DEFAULT_TOKEN_CONFIG_NAME);
        BotFilesManager.createResourceFile("default-config.toml", Constants.DEFAULT_CONFIG_NAME);
        BotFilesManager.createResourceFile("default-lang.toml", Constants.DEFAULT_LANG_NAME);
        BotFilesManager.createResourceFile("default-interaction.toml", Constants.DEFAULT_INTERACTION_NAME);
        BotFilesManager.createResourceFile("default-commands.toml", Constants.DEFAULT_COMMAND_NAME);
        BotFilesManager.createResourceFile("default-tickets.toml", Constants.DEFAULT_TICKET_NAME);
        BotFilesManager.createResourceFile("default-actions.toml", Constants.DEFAULT_ACTION_NAME);
    }

    private static void loadConfigs() {
        try {
            TOKEN = BotFilesManager.readFile(Constants.DEFAULT_TOKEN_CONFIG_NAME).readLine();
            config = new BotConfig(Constants.DEFAULT_CONFIG_NAME);
            lang = new BotConfig(Constants.DEFAULT_LANG_NAME);
            interaction = new BotConfig(Constants.DEFAULT_INTERACTION_NAME);
            tickets = new BotConfig(Constants.DEFAULT_TICKET_NAME);
            commands = new BotConfig(Constants.DEFAULT_COMMAND_NAME);
            actions = new BotConfig(Constants.DEFAULT_ACTION_NAME);
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
                    new GuildListeners(),
                    new UseCommand()
            );
            BotApi.setCompression(Compression.ZLIB);
            BotApi.setActivity(Activity.of(Activity.ActivityType.LISTENING, "Loading..."));
            BotApi.setStatus(OnlineStatus.valueOf(config.getString("bot-custom.status".toUpperCase(), "ONLINE")));
            BotApi.setMemberCachePolicy(MemberCachePolicy.ALL);
            BotApi.setEnabledIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.GUILD_EMOJIS_AND_STICKERS, GatewayIntent.GUILD_PRESENCES, GatewayIntent.MESSAGE_CONTENT);
            BotApi.setChunkingFilter(ChunkingFilter.ALL);
            BotApi.build();
        } catch (LoginException e) {
            getLogger().error("LoginException, wrong TOKEN");
        }
    }

    private static void loadUtils() {

        // catch CTRL+C
        Runtime.getRuntime().addShutdownHook(new Thread(DTBot::saveAll, "Shutdown-thread"));
    }

    private static void saveAll() {
        getLogger().warn("Dont close app with CTRL+C, use /close");
        config.save();
        lang.save();
        interaction.save();
    }

    // Getters

    public static BotConfig getConfig() {
        return config;
    }

    public static BotConfig getLang() {
        return lang;
    }

    public static BotConfig getInteraction() {
        return interaction;
    }

    public static BotConfig getTickets() {
        return tickets;
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
