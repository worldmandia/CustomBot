package ua.mani123.utils;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.requests.RestAction;

import java.util.Map;

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

    public ReplyReason setRestActionAndReturnReplyReason(RestAction<?> restAction) {
        this.restAction = restAction;
        return this;
    }

    public void enablePlaceholder(Map<String, String> placeholders, Member member) {
            this.title = Utils.placeholder(title, placeholders, member);
            this.description = Utils.placeholder(description, placeholders, member);
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
