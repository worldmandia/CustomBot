package ua.mani123.discord.action.subActions;

import net.dv8tion.jda.api.interactions.components.ItemComponent;

public interface SubAction {

  default ItemComponent getComponent() {
    return null;
  }

}
