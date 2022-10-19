package ua.mani123.consoleCommands;

import ua.mani123.CBot;
import ua.mani123.config.configUtils;
import ua.mani123.discord.action.actionUtils;
import ua.mani123.discord.interaction.interactionUtils;

import java.util.List;

public class reloadCommand {
    public static void use(List<String> parts) {
        if (parts.size() > 1) {
            if (parts.get(1).equals("commands")) {
                configUtils.updateCommandInteractions();
                interactionUtils.initCmd(configUtils.getCommandInteraction().getList("interaction"));
                CBot.getLog().info("commandInteraction reloaded");
                return;
            } else if (parts.get(1).equals("actions")) {
                configUtils.updateActions();
                actionUtils.init(configUtils.getActions());
                CBot.getLog().info("actions reloaded");
                return;
            }
        }
        CBot.getLog().info("Usage: reload [actions, commands]");
    }
}
