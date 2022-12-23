package ua.mani123.discord.action.subActions.SActions;

import com.electronwill.nightconfig.core.CommentedConfig;
import java.util.ArrayList;
import java.util.List;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.interactions.components.selections.EntitySelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import ua.mani123.discord.action.subActions.SubAction;

public class ADD_SELECT_MENU implements SubAction {

  String id;
  ArrayList<String> EntityTypes;
  String MenuType;
  ItemComponent itemComponent;

  public ADD_SELECT_MENU(CommentedConfig config) {
    this.id = config.getOrElse("id", null);
    this.MenuType = config.getOrElse("MenuType", "STRING");
    this.EntityTypes = config.getOrElse("EntityTypes", new ArrayList<>(List.of("ROLE", "USER", "CHANNEL")));
    if (MenuType.equalsIgnoreCase("STRING")) {
      ArrayList<CommentedConfig> options = config.getOrElse("option", new ArrayList<>());
      ArrayList<SelectOption> selectOptions = new ArrayList<>();
      for (CommentedConfig option : options) {
        String label = option.get("label");
        String value = option.get("value");
        String emojiString = config.getOrElse("emoji", "");
        Long emojiId = config.getOrElse("emoji-id", 0L);
        Emoji emoji = null;
        if (!emojiString.isEmpty()) {
          if (emojiId != 0L) {
            emoji = Emoji.fromCustom(emojiString, emojiId, true);
          } else {
            emoji = Emoji.fromFormatted(emojiString);
          }
        }
        String description = option.getOrElse("description", null);
        boolean isDefault = option.getOrElse("isDefault", false);
        SelectOption selectOption = SelectOption.of(label, value);
        if (emoji != null) {
          selectOption = selectOption.withEmoji(emoji);
        }
        if (description != null) {
          selectOption = selectOption.withDescription(description);
        }
        selectOption = selectOption.withDefault(isDefault);
        selectOptions.add(selectOption);
      }
      itemComponent = StringSelectMenu.create(id).addOptions(selectOptions).build();
    } else if (MenuType.equalsIgnoreCase("ENTITY")) {
      ArrayList<EntitySelectMenu.SelectTarget> SelectedTargets = new ArrayList<>();
      EntityTypes.forEach(t -> SelectedTargets.add(EntitySelectMenu.SelectTarget.valueOf(t)));
      itemComponent = EntitySelectMenu.create(id, SelectedTargets).build();
    }
  }


  @Override
  public ItemComponent getComponent() {
    return this.itemComponent;
  }
}
