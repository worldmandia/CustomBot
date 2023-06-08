package ua.mani123;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.mani123.config.ConfigUtils;
import ua.mani123.config.Objects.Settings;
import ua.mani123.discordModule.DiscordUtils;

public class CustomBot {
    private final static Logger logger = LoggerFactory.getLogger(DiscordUtils.class);
    public static void main(String[] args) {
        enable();
    }

    public static void enable() {
        Settings settings = new ConfigUtils().loadFileConfig("settings.toml", new Settings());
        if (settings.isEnableDiscordBotModule()) {
            DiscordUtils discordUtils = new DiscordUtils().init(settings.getDefaultConfigFolder());
        }
    }

    public static void disable() {

    }

}