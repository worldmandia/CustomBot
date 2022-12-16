package ua.mani123.discord.interaction.interactions;

import com.electronwill.nightconfig.core.CommentedConfig;
import java.util.ArrayList;
import ua.mani123.discord.interaction.interaction;

public class ButtonInteraction implements interaction {

  private final String id;
  private final ArrayList<String> actionIds;

  public ButtonInteraction(CommentedConfig config) {
    this.id = config.getOrElse("id", "");
    this.actionIds = config.getOrElse("actionsIds", new ArrayList<>());
  }


  public String getId() {
    return id;
  }

  public ArrayList<String> getActionIds() {
    return actionIds;
  }
}
