package ua.mani123.action.actions;

import com.electronwill.nightconfig.core.Config;
import ua.mani123.action.botAction;
import ua.mani123.utils.ReplyReason;

import java.util.concurrent.atomic.AtomicInteger;

public class CREATE_VOICE_CHAT extends botAction {

    String id;
    String actionName;
    AtomicInteger counter;
    Config config;

    ReplyReason replyReason;

    public CREATE_VOICE_CHAT(String id, String actionName, int counter, Config config, ReplyReason replyReason) {
        this.id = id;
        this.actionName = actionName;
        this.counter = new AtomicInteger(counter);
        this.config = config;
        this.replyReason = replyReason;
    }

    public ReplyReason getReplyReason() {
        return replyReason;
    }

    public Config getConfig() {
        return config;
    }

    public String getActionName() {
        return actionName;
    }

    public AtomicInteger getCounter() {
        return counter;
    }

    public String getId() {
        return id;
    }

    public boolean isOnlyCommand() {
        return false;
    }
}
