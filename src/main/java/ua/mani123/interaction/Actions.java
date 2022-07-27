package ua.mani123.interaction;

public enum Actions {
    CREATE_TEXT_CHAT("CREATE_CHAT"),
    CREATE_VOICE_CHAT("CREATE_VOICE_CHAT");
    private final String key;

    Actions(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

}
