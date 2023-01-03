package ua.mani123.discord.action.subActions;

import net.dv8tion.jda.api.interactions.components.ItemComponent;
import ua.mani123.discord.action.TempData;

public interface SubAction {

  ItemComponent getComponent(TempData tempData);

  default int row() {
    return 0;
  }

}
