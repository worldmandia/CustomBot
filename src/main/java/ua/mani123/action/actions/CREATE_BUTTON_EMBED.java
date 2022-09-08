package ua.mani123.action.actions;

import ua.mani123.action.Action;
import ua.mani123.interaction.Interaction;

import java.util.Collection;

public class CREATE_BUTTON_EMBED implements Action {

    protected String id;
    protected String embedTitle;
    protected String embedDescription;
    protected String embedColor;
    protected Collection<Interaction> buttons;

    public CREATE_BUTTON_EMBED(String id, String embedTitle, String embedDescription, String embedColor, Collection<Interaction> buttons) {
        this.id = id;
        this.embedTitle = embedTitle;
        this.embedDescription = embedDescription;
        this.embedColor = embedColor;
        this.buttons = buttons;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isOnlyCommand() {
        return true;
    }

    @Override
    public boolean isOnlyTicket() {
        return false;
    }
}
