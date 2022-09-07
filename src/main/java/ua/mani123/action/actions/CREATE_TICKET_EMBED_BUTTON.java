package ua.mani123.action.actions;

import ua.mani123.action.Action;

public class CREATE_TICKET_EMBED_BUTTON implements Action {

    String id;

    public CREATE_TICKET_EMBED_BUTTON(String id) {
        this.id = id;
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
