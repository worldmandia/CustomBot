package ua.mani123.activity;

import net.dv8tion.jda.api.entities.Activity;

public class activity {

    Activity.ActivityType type;
    String activityText;
    String url;

    public activity(Activity.ActivityType type, String activityText, String url) {
        this.type = type;
        this.activityText = activityText;
        this.url = url;
    }

    public Activity.ActivityType getType() {
        return type;
    }

    public String getActivityText() {
        return activityText;
    }

    public String getUrl() {
        return url;
    }
}
