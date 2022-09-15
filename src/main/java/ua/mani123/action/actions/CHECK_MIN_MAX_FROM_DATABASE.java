package ua.mani123.action.actions;

import ua.mani123.action.Action;
import ua.mani123.utils.ReplyReason;

public class CHECK_MIN_MAX_FROM_DATABASE implements Action {

    String id;
    int min;
    int max;
    String section;

    ReplyReason replyReason;

    public CHECK_MIN_MAX_FROM_DATABASE(String id, int min, int max, String section, ReplyReason replyReason) {
        this.id = id;
        this.min = min;
        this.max = max;
        this.section = section;
        this.replyReason = replyReason;
    }

    public ReplyReason getReplyReason() {
        return replyReason;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public String getSection() {
        return section;
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
