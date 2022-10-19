package ua.mani123.discord.action.filter;

import com.electronwill.nightconfig.core.CommentedConfig;
import ua.mani123.discord.action.filter.filters.ROLE;
import ua.mani123.discord.action.filter.filters.USER;

import java.util.ArrayList;
import java.util.List;

public class filterUtils {

    public static List<Filter> enable(List<CommentedConfig> configs) {
        if (!configs.isEmpty()) {
            List<Filter> filterList = new ArrayList<>();
            for (CommentedConfig config : configs) {
                String type = config.get("type");
                if (type != null) {
                    type = type.toUpperCase().trim();
                    switch (type) {
                        case "ROLE" -> filterList.add(new ROLE(config));
                        case "USER" -> filterList.add(new USER(config));
                    }
                }
            }
            return filterList;
        }
        return new ArrayList<>();
    }

}
