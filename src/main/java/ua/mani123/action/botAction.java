package ua.mani123.action;

public abstract class botAction {
    String id = "BLANK_ACTION";
    boolean onlyCommand = false;
    public String getId() {
        return id;
    }

    public boolean isOnlyCommand() {
        return onlyCommand;
    }
}
