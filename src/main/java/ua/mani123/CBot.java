package ua.mani123;

import ch.qos.logback.classic.Logger;
import ua.mani123.addon.AddonUtils;
import ua.mani123.config.configUtils;
import ua.mani123.consoleCommands.consoleUtils;
import ua.mani123.discord.action.actionUtils;
import ua.mani123.discord.interaction.interactionUtils;

public class CBot {

    private static Logger log;

    public static void main(String[] args) {
        log = Utils.initLogger();
        getLog().info("Starting CustomBot");
        configUtils.init();
        actionUtils.init(configUtils.getActions());
        interactionUtils.initCmd(configUtils.getCommandInteraction().getList("command"));
        interactionUtils.initButton(configUtils.getButtonInteraction().getList("button"));
        AddonUtils.loadAddons("addons");
        AddonUtils.enableAddons(AddonUtils.addonMap);
        Runtime.getRuntime().addShutdownHook(new Thread(configUtils::saveAll, "Shutdown-thread"));
        getLog().info("Done!");
        initConsole();
    }

    private static void initConsole() {
        Thread thread = new Thread(new consoleUtils());
        thread.start();
    }

    public static Logger getLog() {
        return log;
    }

}
