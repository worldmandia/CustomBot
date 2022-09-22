package ua.mani123.action.actions;

import ua.mani123.action.botAction;

public class SELECT_CATEGORY extends botAction {

    String id;
    String category;

    public SELECT_CATEGORY(String id, String category) {
        this.id = id;
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isOnlyCommand() {
        return false;
    }
}
