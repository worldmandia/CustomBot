package ua.mani123;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.utils.Compression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.mani123.command.RegisterCommands;
import ua.mani123.listeners.AutoComplete;
import ua.mani123.listeners.ButtonListener;
import ua.mani123.listeners.UseCommand;
import ua.mani123.config.BotConfig;
import ua.mani123.config.BotFilesManager;
import ua.mani123.interaction.Interaction;
import ua.mani123.interaction.InteractionType;
import ua.mani123.interaction.InteractionUtils;
import ua.mani123.ticket.Ticket;
import ua.mani123.ticket.TicketType;
import ua.mani123.ticket.TicketUtils;

import javax.security.auth.login.LoginException;
import java.util.List;
import java.util.Map;

public class DTBot {
    protected static BotConfig config;
    protected static BotConfig lang;

    protected static BotConfig interaction;
    protected static Map<TicketType, List<Ticket>> tickets;
    protected static Map<InteractionType, List<Interaction>> interactions;
    protected static String TOKEN;
    protected static Logger logger = LoggerFactory.getLogger(DTBot.class);

    protected static Activity activity = Activity.of(Activity.ActivityType.LISTENING, "Loading...");
    protected static JDA BotApi;

    // Main method

    public static void main(String[] args) {
        createConfigs();
        loadConfigs();
        startBot();
        loadUtils();
        new RegisterCommands(getBotApi(), getLang());
    }

    // Methods for start bot

    static void createConfigs() {
        BotFilesManager.createFile(Constants.DEFAULT_TOKEN_CONFIG_NAME);
        BotFilesManager.createResourceFile("default-config.toml", Constants.DEFAULT_CONFIG_NAME);
        BotFilesManager.createResourceFile("default-lang.toml", Constants.DEFAULT_LANG_NAME);
        BotFilesManager.createResourceFile("default-interaction.toml", Constants.DEFAULT_INTERACTION_NAME);
    }

    static void loadConfigs() {
        try {
            TOKEN = BotFilesManager.readFile(Constants.DEFAULT_TOKEN_CONFIG_NAME).readLine();
            config = new BotConfig(Constants.DEFAULT_CONFIG_NAME);
            lang = new BotConfig(Constants.DEFAULT_LANG_NAME);
            interaction = new BotConfig(Constants.DEFAULT_INTERACTION_NAME);
        } catch (Exception e) {
            getLogger().error(e.getMessage() + ", check or reset cfg files");
        }
    }

    static void startBot() {
        try {
            BotApi = JDABuilder.createDefault(TOKEN)
                    .setStatus(OnlineStatus.valueOf(config.getString("bot-custom.status".toUpperCase(), "ONLINE")))
                    .setActivity(activity)
                    .setCompression(Compression.ZLIB)
                    .addEventListeners(
                            new AutoComplete(),
                            new ButtonListener(),
                            new UseCommand()
                    )
                    .build();
        } catch (LoginException e) {
            getLogger().error("LoginException, wrong TOKEN");
        }
    }

    static void loadUtils() {
        tickets = TicketUtils.ticketSorter("ticket");
        interactions = InteractionUtils.interactionSorter("interaction");
        activity = Activity.of(Activity.ActivityType.valueOf(config.getString("bot-custom.activity".toUpperCase(), "PLAYING")),
                config.getString("bot-custom.activity-text", "tickets %tickets%").replace("%tickets%", String.valueOf(tickets.size())));
    }

    // Getters

    public static Map<InteractionType, List<Interaction>> getInteractions() {
        return interactions;
    }

    public static Map<TicketType, List<Ticket>> getTickets() {
        return tickets;
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

    public static Logger getLogger() {
        return logger;
    }

    public static JDA getBotApi() {
        return BotApi;
    }

}
