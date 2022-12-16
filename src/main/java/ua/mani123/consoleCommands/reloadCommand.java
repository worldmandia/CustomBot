package ua.mani123.consoleCommands;

import java.util.List;
import java.util.Map;
import ua.mani123.CBot;
import ua.mani123.addon.AddonData;
import ua.mani123.addon.AddonUtils;
import ua.mani123.config.configUtils;
import ua.mani123.discord.action.actionUtils;
import ua.mani123.discord.interaction.interactionUtils;

public class reloadCommand {

  public static void use(List<String> parts) {
    if (parts.size() > 1) {
      switch (parts.get(1)) {
        case "commands" -> {
          configUtils.updateCommandInteractions();
          interactionUtils.initCmd(configUtils.getCommandInteraction().getList("interaction"));
          CBot.getLog().info("commandInteraction reloaded");
          return;
        }
        case "actions" -> {
          configUtils.updateActions();
          actionUtils.init(configUtils.getActions());
          CBot.getLog().info("actions reloaded");
          return;
        }
        case "addons" -> {
          for (Map.Entry<String, AddonData> addon : AddonUtils.getAddonMap().entrySet()) {
            addon.getValue().getAddon().reload();
          }
          CBot.getLog().info("addons reloaded");
          return;
        }
      }
    }
    CBot.getLog().info("Usage: reload [actions, commands, addons]");
  }
}
