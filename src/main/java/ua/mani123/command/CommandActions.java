package ua.mani123.command;

public enum CommandActions {
    CREATE_TICKET_EMBED("CREATE_TICKET_EMBED");
    private final String key;
    CommandActions(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
