package ua.mani123.action.actions;

import ua.mani123.action.botAction;
import ua.mani123.utils.ReplyReason;

public class CHECK_MIN_MAX_FROM_DATABASE extends botAction {

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


    public String getId() {
        return id;
    }


    public boolean isOnlyCommand() {
        return false;
    }
}
