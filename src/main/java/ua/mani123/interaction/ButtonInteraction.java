package ua.mani123.interaction;

import com.electronwill.nightconfig.core.CommentedConfig;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

public class ButtonInteraction implements Interaction {

    String id;
    ButtonStyle buttonStyle;
    String buttonText;
    Actions actions;
    CommentedConfig customs;

    public ButtonInteraction(String id, ButtonStyle buttonStyle, String buttonText, Actions actions, CommentedConfig customs) {
        this.id = id;
        this.buttonStyle = buttonStyle;
        this.buttonText = buttonText;
        this.actions = actions;
        this.customs = customs;
    }

    public Button getInteraction() {
        return Button.of(buttonStyle, id, buttonText);
    }

    public String getId() {
        return id;
    }

    public CommentedConfig getCustoms() {
        return customs;
    }

    public Actions getActions() {
        return actions;
    }
}
