package ua.mani123.discord.action.subActions.SActions;

import com.electronwill.nightconfig.core.CommentedConfig;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import org.apache.commons.text.StringSubstitutor;
import ua.mani123.discord.action.TempData;
import ua.mani123.discord.action.subActions.SubAction;

public class ADD_MODAL_SECTION implements SubAction {
  String id;
  String inputStyle;
  String label;
  String placeholder;
  ItemComponent itemComponent;
  int row = -1;
  int minLength;
  int maxLength;
  boolean required;

  public ADD_MODAL_SECTION(CommentedConfig config) {
    this.id = config.getOrElse("id", null);
    this.label = config.getOrElse("label", "");
    this.placeholder = config.getOrElse("placeholder", "");
    this.minLength = config.getOrElse("minLength", 10);
    this.maxLength = config.getOrElse("maxLength", 100);
    this.required = config.getOrElse("required", false);
    //this.row = config.getOrElse("next-row", 0);
    this.inputStyle = config.getOrElse("inputStyle", "SHORT");
  }


  @Override
  public ItemComponent getComponent(TempData tempData) {
    StringSubstitutor str = new StringSubstitutor(tempData.getPlaceholders());
    itemComponent = TextInput.create(id, str.replace(label), TextInputStyle.valueOf(inputStyle.trim().toUpperCase()))
        .setPlaceholder(str.replace(placeholder))
        .setMinLength(minLength)
        .setMaxLength(maxLength)
        .setRequired(required)
        .build();
    return this.itemComponent;
  }

  @Override
  public int row() {
    return this.row;
  }

}
