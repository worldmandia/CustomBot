package ua.mani123.discord.interaction;

import com.electronwill.nightconfig.core.CommentedConfig;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ua.mani123.discord.interaction.interactions.ButtonInteraction;
import ua.mani123.discord.interaction.interactions.CommandInteraction;

public class InteractionUtils {

  static Map<String, CommandInteraction> commands = new HashMap<>();
  static Map<String, ButtonInteraction> buttons = new HashMap<>();

  public static void initCmd(List<CommentedConfig> config) {
    for (CommentedConfig cfg : config) {
      CommandInteraction commandInteraction = new CommandInteraction(cfg);
      commands.put(commandInteraction.getName(), commandInteraction);
    }
  }

  public static void initButton(List<CommentedConfig> config) {
    for (CommentedConfig cfg : config) {
      ButtonInteraction buttonInteraction = new ButtonInteraction(cfg);
      buttons.put(buttonInteraction.getId(), buttonInteraction);
    }
  }


  public static Map<String, ButtonInteraction> getButtons() {
    return buttons;
  }

  public static Map<String, CommandInteraction> getCommands() {
    return commands;
  }
}
