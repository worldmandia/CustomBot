package ua.mani123;

import ch.qos.logback.classic.Logger;
import ua.mani123.addon.AddonUtils;
import ua.mani123.config.configUtils;
import ua.mani123.consoleCommands.consoleUtils;
import ua.mani123.discord.action.ActionUtils;
import ua.mani123.discord.interaction.InteractionUtils;

public class CBot {

  private static Logger log;

  public static void main(String[] args) {
    log = Utils.initLogger();
    getLog().info("Starting CustomBot");
    configUtils.init();
    ActionUtils.init(configUtils.getActions());
    InteractionUtils.initInteractions();
    AddonUtils.loadAddons("addons");
    AddonUtils.enableAddons(AddonUtils.getAddonMap());
    Runtime.getRuntime().addShutdownHook(new Thread(configUtils::disableAll, "Shutdown-thread"));
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
