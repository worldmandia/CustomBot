package ua.mani123.activity;

import com.electronwill.nightconfig.core.Config;
import net.dv8tion.jda.api.entities.Activity;
import ua.mani123.DTBot;

import java.util.ArrayList;
import java.util.List;

public class ActivityUtils {

    protected static ArrayList<activity> allActivities = new ArrayList<>();

    public static void load() {
        List<Config> allActivityConfigs = DTBot.getActivities().getFileConfig().get("activity");
        for (Config activity : allActivityConfigs) {
            String type = activity.get("activity-type");
            String url = activity.getOrElse("url", "null");
            allActivities.add(new activity(
                    Activity.ActivityType.valueOf(type.trim().toUpperCase()),
                    activity.getOrElse("activity-text", "Not set"),
                    url
            ));
        }
    }

    public static ArrayList<activity> getAllActivities() {
        return allActivities;
    }
}
