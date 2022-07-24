package ua.mani123;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.utils.Compression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.mani123.Command.RegisterCommands;
import ua.mani123.Listeners.AutoComplete;
import ua.mani123.Listeners.ButtonListener;
import ua.mani123.Listeners.UseCommand;
import ua.mani123.config.BotConfig;
import ua.mani123.config.BotFilesManager;
import ua.mani123.ticket.Ticket;
import ua.mani123.ticket.TicketType;
import ua.mani123.utils.Utils;

import javax.security.auth.login.LoginException;
import java.util.List;
import java.util.Map;

public class DTBot {
    protected static BotConfig config;
    protected static BotConfig lang;
    protected static BotConfig button;
    protected static Map<TicketType, Ticket> tickets;
    protected static Map<TicketType, List<String>> idsByType;
    protected static String TOKEN;
    protected static Logger LOGGER = LoggerFactory.getLogger(DTBot.class);
    protected static JDA BotApi;

    public static void main(String[] args) {
        createConfigs();
        loadConfigs();
        startBot();
        loadUtils();
        new RegisterCommands(getBotApi(), getLang());
    }

    static void createConfigs() {
        BotFilesManager.createFile(Constants.DEFAULT_TOKEN_CONFIG_NAME);
        BotFilesManager.createResourceFile("default-config.toml", Constants.DEFAULT_CONFIG_NAME);
        BotFilesManager.createResourceFile("default-lang.toml", Constants.DEFAULT_LANG_NAME);
        BotFilesManager.createResourceFile("default-button.toml", Constants.DEFAULT_BUTTON_NAME);
    }

    static void loadConfigs() {
        try {
            TOKEN = BotFilesManager.readFile(Constants.DEFAULT_TOKEN_CONFIG_NAME).readLine();
            config = new BotConfig(Constants.DEFAULT_CONFIG_NAME);
            lang = new BotConfig(Constants.DEFAULT_LANG_NAME);
            button = new BotConfig(Constants.DEFAULT_BUTTON_NAME);
        } catch (Exception e) {
            getLOGGER().error(e.getMessage() + ", check or reset cfg files");
        }
    }

    static void startBot() {
        try {
            BotApi = JDABuilder.createDefault(TOKEN)
                    .setStatus(OnlineStatus.valueOf(config.getString("bot-custom.status".toUpperCase(), "ONLINE")))
                    .setActivity(Activity.of(Activity.ActivityType.valueOf(config.getString("bot-custom.activity".toUpperCase(), "PLAYING")),
                            config.getString("bot-custom.activity-text", "tickets %tickets%").replace("%tickets%", String.valueOf(tickets.size()))))
                    .setCompression(Compression.ZLIB)
                    .addEventListeners(
                            new AutoComplete(),
                            new ButtonListener(),
                            new UseCommand()
                    )
                    .build();
        } catch (LoginException e) {
            getLOGGER().error("LoginException, wrong TOKEN");
        }
    }

    static void loadUtils() {
        tickets = Utils.ticketSorter("ticket");
        idsByType = Utils.getSortedMapIds(tickets);
    }

    public static Map<TicketType, Ticket> getTickets() {
        return tickets;
    }

    public static Map<TicketType, List<String>> getIdsByType() {
        return idsByType;
    }

    public static BotConfig getConfig() {
        return config;
    }

    public static BotConfig getLang() {
        return lang;
    }

    public static BotConfig getButton() {
        return button;
    }

    public static Logger getLOGGER() {
        return LOGGER;
    }

    public static JDA getBotApi() {
        return BotApi;
    }

}
