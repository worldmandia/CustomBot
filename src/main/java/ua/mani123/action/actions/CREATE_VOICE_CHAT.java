package ua.mani123.action.actions;

import com.electronwill.nightconfig.core.Config;
import ua.mani123.action.Action;
import ua.mani123.utils.ReplyReason;

import java.util.concurrent.atomic.AtomicInteger;

public class CREATE_VOICE_CHAT implements Action {

    String id;
    String actionName;
    AtomicInteger counter;
    String categoryName;
    Config config;

    ReplyReason replyReason;

    public CREATE_VOICE_CHAT(String id, String actionName, int counter, String categoryName, Config config, ReplyReason replyReason) {
        this.id = id;
        this.actionName = actionName;
        this.counter = new AtomicInteger(counter);
        this.categoryName = categoryName;
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

    public String getCategoryName() {
        return categoryName;
    }
    @Override
    public String getId() {
        return null;
    }

    @Override
    public boolean isOnlyCommand() {
        return false;
    }
}
