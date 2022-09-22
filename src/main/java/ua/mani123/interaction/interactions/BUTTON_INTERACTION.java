package ua.mani123.interaction.interactions;

import ua.mani123.action.botAction;
import ua.mani123.interaction.botInteraction;

import java.util.Collection;

public class BUTTON_INTERACTION implements botInteraction {

    protected String id;
    protected String buttonStyle;
    protected String buttonText;
    protected Collection<botAction> actions;

    public BUTTON_INTERACTION(String id, String buttonStyle, String buttonText, Collection<botAction> actions) {
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

    public Collection<botAction> getActions() {
        return actions;
    }

    @Override
    public String getId() {
        return id;
    }
}
