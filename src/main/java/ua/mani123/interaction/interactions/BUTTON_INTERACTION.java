package ua.mani123.interaction.interactions;

import ua.mani123.interaction.Interaction;

import java.util.ArrayList;

public class BUTTON_INTERACTION implements Interaction {

    protected String id;
    protected String buttonStyle;
    protected String buttonText;
    protected ArrayList<String> actions;

    public BUTTON_INTERACTION(String id, String buttonStyle, String buttonText, ArrayList<String> actions) {
        this.id = id;
        this.buttonStyle = buttonStyle;
        this.buttonText = buttonText;
        this.actions = actions;
    }

    public String getButtonStyle() {
        return buttonStyle;
    }

    public String getButtonText() {
        return buttonText;
    }

    public ArrayList<String> getActions() {
        return actions;
    }

    @Override
    public String getId() {
        return null;
    }
}
