package ua.mani123.action.actions;

import com.electronwill.nightconfig.core.Config;
import ua.mani123.action.Action;

import java.util.concurrent.atomic.AtomicInteger;

public class CREATE_TEXT_CHAT implements Action {
    String id;
    String actionName;
    String actionDescription;
    AtomicInteger counter;
    String categoryName;
    Config config;

    public CREATE_TEXT_CHAT(String id, String actionName, String actionDescription, int counter, String categoryName, Config config) {
        this.id = id;
        this.actionName = actionName;
        this.actionDescription = actionDescription;
        this.counter = new AtomicInteger(counter);
        this.categoryName = categoryName;
        this.config = config;
    }

    public Config getConfig() {
        return config;
    }

    public String getActionName() {
        return actionName;
    }

    public String getActionDescription() {
        return actionDescription;
    }

    public AtomicInteger getCounter() {
        return counter;
    }

    public String getCategoryName() {
        return categoryName;
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
