package ua.mani123.config.Objects;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.conversion.IgnoreValue;
import com.electronwill.nightconfig.core.conversion.Path;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.mani123.config.ConfigDefaults;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
public class DiscordActionConfig extends ConfigDefaults {

    @Path("Action")
    ArrayList<CommentedConfig> actionConfigs = new ArrayList<>();
    @Path("Filter")
    ArrayList<CommentedConfig> filterConfigs = new ArrayList<>();

    @IgnoreValue
    ArrayList<Action> actions = new ArrayList<>();

    @IgnoreValue
    ArrayList<Filter> filters = new ArrayList<>();

    @Getter
    @Setter
    @AllArgsConstructor
    public abstract static class Action {
        String type;
        String id;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public abstract static class Filter {
        String type;
        String id;
    }
}
