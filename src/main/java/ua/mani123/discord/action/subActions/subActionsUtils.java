package ua.mani123.discord.action.subActions;

import com.electronwill.nightconfig.core.CommentedConfig;
import java.util.ArrayList;
import ua.mani123.discord.action.subActions.SActions.ADD_BUTTON;
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
            case ("ADD_BUTTON") -> subActions.add(new ADD_BUTTON(cfg));
            case ("ADD_SELECT_MENU") -> subActions.add(new ADD_SELECT_MENU(cfg));
          }
        }
      });
    }
    return subActions;
  }
}
