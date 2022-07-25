package ua.mani123.interaction;

public enum Actions {
    CREATE_CHAT("CREATE_CHAT");
    private final String key;

    Actions(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

}
