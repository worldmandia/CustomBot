package ua.mani123.discord.action.subActions;

import com.electronwill.nightconfig.core.CommentedConfig;
import java.util.ArrayList;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.interactions.components.LayoutComponent;
import ua.mani123.discord.action.TempData;
import ua.mani123.discord.action.subActions.SActions.ADD_BUTTON;
import ua.mani123.discord.action.subActions.SActions.ADD_MODAL_SECTION;
import ua.mani123.discord.action.subActions.SActions.ADD_SELECT_MENU;

public class subActionsUtils {

  public static ArrayList<SubAction> enable(ArrayList<String> subActionIds, CommentedConfig config) {
    ArrayList<SubAction> subActions = new ArrayList<>();
    if (!subActionIds.isEmpty()) {
      subActionIds.forEach(id -> {
        CommentedConfig cfg = config.get("sub-action" + id);
        if (cfg != null) {
          String type = cfg.get("type");
          switch (type.trim().toUpperCase()) {
            case "ADD_BUTTON" -> subActions.add(new ADD_BUTTON(cfg));
            case "ADD_SELECT_MENU" -> subActions.add(new ADD_SELECT_MENU(cfg));
            case "ADD_MODAL_SECTION" -> subActions.add(new ADD_MODAL_SECTION(cfg));
          }
        }
      });
    }
    return subActions;
  }

  public static ArrayList<LayoutComponent> getRows(ArrayList<SubAction> subActions, TempData tempData) {
    ArrayList<LayoutComponent> actionRows = new ArrayList<>();
    if (!subActions.isEmpty()) {
      ArrayList<ArrayList<ItemComponent>> itemComponent = new ArrayList<>(5);
      subActions.forEach(subAction -> {
        if (subAction.row() <= 4 || subAction.row() > -1) {
          if (subAction.row() <= itemComponent.size()) {
            itemComponent.add(subAction.row(), new ArrayList<>());
          }
          itemComponent.get(subAction.row()).add(subAction.getComponent(tempData));
        }
      });
      itemComponent.forEach(row -> actionRows.add(ActionRow.of(row)));
    }
    return actionRows;
  }

}
