package ua.mani123.discord.action.subActions.SActions;

import com.electronwill.nightconfig.core.CommentedConfig;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import ua.mani123.discord.action.subActions.SubAction;

public class ADD_BUTTON implements SubAction {

    ItemComponent itemComponent;
    String id;
    String label;
    Emoji emoji;
    Long emojiId;
    String emojiString;
    ButtonStyle buttonStyle;

    public ADD_BUTTON(CommentedConfig config) {
        this.id = config.getOrElse("id", null);
        this.label = config.getOrElse("label", "");
        this.emojiString = config.getOrElse("emoji", null);
        this.emojiId = config.getOrElse("emoji-id", null);
        this.buttonStyle = ButtonStyle.valueOf(config.getOrElse("button-style", "UNKNOWN").toUpperCase());
        if (emojiString != null) {
            if (emojiId != null) {
                this.emoji = Emoji.fromCustom(emojiString, emojiId, true);
            } else {
                this.emoji = Emoji.fromFormatted(emojiString);
            }
        }
        if (emoji != null){
            this.itemComponent = Button.of(buttonStyle, id, label, emoji);
        }
        this.itemComponent = Button.of(buttonStyle, id, label);
    }

    @Override
    public ItemComponent getComponent() {
        return this.itemComponent;
    }
}
