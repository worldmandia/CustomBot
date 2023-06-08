package ua.mani123;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.mani123.config.ConfigUtils;
import ua.mani123.config.Objects.GlobalLang;
import ua.mani123.config.Objects.Settings;
import ua.mani123.discordModule.DiscordUtils;

@Getter
public class CustomBot {
    private final static Logger logger = LoggerFactory.getLogger(DiscordUtils.class);

    private static Settings settings;
    private static GlobalLang lang;
    public static void main(String[] args) {
        enable();
    }

    public static void enable() {
        settings = new ConfigUtils().loadFileConfig("settings.toml", new Settings());
        lang = new ConfigUtils().loadFileConfig(settings.getDefaultConfigFolder() + "/global_lang.toml", new GlobalLang());
        if (settings.isEnableDiscordBotModule()) {
            logger.info(lang.getDiscordModuleInit());
            DiscordUtils discordUtils = new DiscordUtils().init(settings.getDefaultConfigFolder()).enableBots();
        }
    }

    public static void disable() {

    }

    public static Settings getSettings() {
        return settings;
    }

    public static GlobalLang getLang() {
        return lang;
    }
}