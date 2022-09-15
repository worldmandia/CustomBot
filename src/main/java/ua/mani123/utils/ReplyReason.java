package ua.mani123.utils;

import net.dv8tion.jda.api.requests.RestAction;

public class ReplyReason {
    String title;
    String description;
    boolean ephemeral;
    RestAction<?> restAction;

    public ReplyReason(String title, String description, boolean ephemeral) {
        this.title = title;
        this.description = description;
        this.ephemeral = ephemeral;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEphemeral(boolean ephemeral) {
        this.ephemeral = ephemeral;
    }

    public void setRestAction(RestAction<?> restAction) {
        this.restAction = restAction;
    }

    public RestAction<?> getRestAction() {return restAction;}
    public boolean isEphemeral() {return ephemeral;}
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
}
