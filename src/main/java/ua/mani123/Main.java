package ua.mani123;

import ua.mani123.discordModule.DiscordUtils;

public class Main {

    static final String defaultFolder = "test";
    public static void main(String[] args) {
        DiscordUtils discordUtils = new DiscordUtils().init(defaultFolder);
    }



}