package ua.mani123;

import lombok.Getter;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.mani123.config.ConfigUtils;
import ua.mani123.config.Objects.GlobalLang;
import ua.mani123.config.Objects.Settings;
import ua.mani123.consoleCommands.consoleUtils;
import ua.mani123.discordModule.DiscordUtils;

@Getter
public class CustomBot {

    private final static Logger logger = LoggerFactory.getLogger(CustomBot.class);

    private static Settings settings;
    private static GlobalLang lang;
    private static DiscordUtils discordUtils;

    public static void main(String[] args) {
        enable();
        Runtime.getRuntime().addShutdownHook(new Thread(CustomBot::disable));
    }

    public static void enable() {
        settings = new ConfigUtils("settings.toml").loadAsFileConfig(new Settings(), true);
        lang = new ConfigUtils(settings.getDefaultConfigFolder() + "/global_lang.toml").loadAsFileConfig(new GlobalLang(), true);
        if (settings.isEnableDiscordBotModule()) {
            logger.info(lang.getDiscordModuleInit());
            discordUtils = new DiscordUtils().init(settings.getDefaultConfigFolder()).enableBots().loadDiscordActions().registerListeners(settings);
        }
        logger.info("Done!");
        new Thread(new consoleUtils()).start();
    }

    public static void disable() {
        discordUtils.getDiscordBots().forEach(ShardManager::shutdown);
        logger.info(lang.getCustomBotDisabled());
    }

    public static Settings getSettings() {
        return settings;
    }

    public static GlobalLang getLang() {
        return lang;
    }

    public static DiscordUtils getDiscordUtils() {
        return discordUtils;
    }
}