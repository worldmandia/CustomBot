package ua.mani123.discord.interaction;

import com.electronwill.nightconfig.core.CommentedConfig;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import ua.mani123.config.configUtils;
import ua.mani123.discord.interaction.interactions.ButtonInteraction;
import ua.mani123.discord.interaction.interactions.CommandInteraction;
import ua.mani123.discord.interaction.interactions.ModalInteraction;

public class InteractionUtils {

  static HashMap<InteractionTypes, HashSet<Interaction>> interactions = new HashMap<>();

  public static void initInteractions() {

    configUtils.getInteractions().forEach((s, cConfig) -> {
      interactions.put(InteractionTypes.BUTTON, new HashSet<>());
      interactions.put(InteractionTypes.COMMAND, new HashSet<>());
      ArrayList<CommentedConfig> commentedConfigs = cConfig.getFileCfg().get("interaction");
      commentedConfigs.forEach(config -> {
        String type = config.get("type");
        if (type != null) {
          switch (InteractionTypes.valueOf(type.trim().toUpperCase())) {
            case COMMAND -> interactions.get(InteractionTypes.COMMAND).add(new CommandInteraction(config));
            case BUTTON -> interactions.get(InteractionTypes.BUTTON).add(new ButtonInteraction(config));
            case MODALS -> interactions.get(InteractionTypes.MODALS).add(new ModalInteraction(config));
          }
        }
      });
    });
  }

  public static HashMap<InteractionTypes, HashSet<Interaction>> getInteractions() {
    return interactions;
  }
}
