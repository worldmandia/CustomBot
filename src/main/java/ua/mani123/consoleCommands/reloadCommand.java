package ua.mani123.consoleCommands;

import java.util.List;
import java.util.Map;
import ua.mani123.CBot;
import ua.mani123.addon.AddonData;
import ua.mani123.addon.AddonUtils;
import ua.mani123.config.configUtils;
import ua.mani123.discord.action.ActionUtils;
import ua.mani123.discord.interaction.InteractionUtils;

public class reloadCommand {

  public static void use(List<String> parts) {
    if (parts.size() > 1) {
      switch (parts.get(1)) {
        case "interactions" -> {
          configUtils.updateInteractions();
          InteractionUtils.initInteractions();
          CBot.getLog().info("Interactions reloaded");
          return;
        }
        case "actions" -> {
          configUtils.updateActions();
          ActionUtils.init(configUtils.getActions());
          CBot.getLog().info("Actions reloaded");
          return;
        }
        case "addons" -> {
          for (Map.Entry<String, AddonData> addon : AddonUtils.getAddonMap().entrySet()) {
            addon.getValue().getAddon().reload();
          }
          CBot.getLog().info("Addons reloaded");
          return;
        }
      }
    }
    CBot.getLog().info("Usage: reload [actions, interactions, addons]");
  }
}
