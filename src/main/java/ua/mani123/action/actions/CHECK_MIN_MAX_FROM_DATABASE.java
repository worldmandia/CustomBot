package ua.mani123.action.actions;

import ua.mani123.action.Action;

public class CHECK_MIN_MAX_FROM_DATABASE implements Action {

    String id;
    int min;
    int max;
    String section;

    public CHECK_MIN_MAX_FROM_DATABASE(String id, int min, int max, String section) {
        this.id = id;
        this.min = min;
        this.max = max;
        this.section = section;
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
