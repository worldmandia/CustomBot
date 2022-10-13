package ua.mani123;

import ua.mani123.config.configUtils;
import ua.mani123.discord.interaction.interactionUtils;

import java.util.logging.Logger;

public class CBot {

    public static void main(String[] args) {
        getLog().info("Starting CustomBot");
        configUtils.init();
        interactionUtils.initCmd(configUtils.getCommandInteraction().getList("interaction"));
        Runtime.getRuntime().addShutdownHook(new Thread(configUtils::saveAll, "Shutdown-thread"));
    }

    public static Logger getLog() {
        return Logger.getGlobal();
    }
}
