package ua.mani123.discordModule;

import net.dv8tion.jda.api.events.GenericEvent;
import ua.mani123.config.Objects.DiscordConfigs;

import java.util.ArrayList;

public class Utils {

    public static void runOrdersWithFilterSystem(GenericEvent event, ArrayList<DiscordConfigs.Order> orders) {
        int countActions = 0;
        boolean canRun = true;

        for (DiscordConfigs.Order order : orders) {
            if (order instanceof DiscordConfigs.Filter filter) {
                if (!filter.canNext(event)) {
                    if (filter.isDenyAllOrdersAfterFilter()) {
                        return;
                    } else {
                        canRun = false;
                        countActions = filter.getDenyOrdersAfterFilter();
                    }
                }
            } else if (order instanceof DiscordConfigs.Action action) {
                if (!canRun) {
                    if (countActions > 0) {
                        action.run(event);
                        countActions--;
                    } else {
                        return;
                    }
                } else {
                    action.run(event);
                }
            }
        }
    }

}
