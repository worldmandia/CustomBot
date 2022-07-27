package ua.mani123.interaction;

import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

public class ButtonInteraction implements Interaction {

    String id;
    ButtonStyle buttonStyle;
    String buttonText;
    Actions actions;
    String category;

    public ButtonInteraction(String id, ButtonStyle buttonStyle, String buttonText, Actions actions, String category) {
        this.id = id;
        this.buttonStyle = buttonStyle;
        this.buttonText = buttonText;
        this.actions = actions;
        this.category = category;
    }

    public Button getInteraction(){
        return Button.of(buttonStyle, id, buttonText);
    }
    public String getId() {
        return id;
    }

}
