package ua.mani123.interaction;

public enum InteractionType {
    BUTTON("BUTTON");
    private final String key;

    InteractionType(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
