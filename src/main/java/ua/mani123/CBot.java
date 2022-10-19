package ua.mani123;

import ch.qos.logback.classic.Logger;
import ua.mani123.config.configUtils;
import ua.mani123.consoleCommands.consoleUtils;
import ua.mani123.discord.action.actionUtils;
import ua.mani123.discord.interaction.interactionUtils;

public class CBot {

    private static Logger log;

    public static void main(String[] args) {
        log = Utils.initLogger();
        try {
            getLog().info("Starting CustomBot");
            configUtils.init();
            Utils.initPlaceholders();
            actionUtils.init(configUtils.getActions());
            interactionUtils.initCmd(configUtils.getCommandInteraction().getList("interaction"));
            Runtime.getRuntime().addShutdownHook(new Thread(configUtils::saveAll, "Shutdown-thread"));
            getLog().info("Done!");
            initConsole();
        } catch (Exception e) {
            log.warn("You received exception: " + e.getMessage());
        }
    }

    private static void initConsole() {
        Thread thread = new Thread(new consoleUtils());
        thread.start();
    }

    public static Logger getLog() {
        return log;
    }
}
