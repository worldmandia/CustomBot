package ua.mani123;

import ua.mani123.config.ConfigUtils;
import ua.mani123.config.Objects.BotConfig;

public class Main {
    public static void main(String[] args) {
        BotConfig botConfig = new ConfigUtils().loadFileConfig("test/config.toml", new BotConfig());
    }



}