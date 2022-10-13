package ua.mani123;

import ch.qos.logback.classic.Logger;
import ua.mani123.config.configUtils;
import ua.mani123.discord.interaction.interactionUtils;

public class CBot {

    static Logger log;

    public static void main(String[] args) {
        log = utils.initLogger();
        getLog().info("Starting CustomBot");
        configUtils.init();
        interactionUtils.initCmd(configUtils.getCommandInteraction().getList("interaction"));
        Runtime.getRuntime().addShutdownHook(new Thread(configUtils::saveAll, "Shutdown-thread"));
    }

    public static Logger getLog() {
        return log;
    }

}
