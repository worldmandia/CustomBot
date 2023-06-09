package ua.mani123.config.Objects;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.conversion.IgnoreValue;
import com.electronwill.nightconfig.core.conversion.Path;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.dv8tion.jda.api.events.GenericEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.mani123.config.ConfigDefaults;
import ua.mani123.discordModule.DiscordUtils;

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
        private final Logger logger = LoggerFactory.getLogger(this.getClass());
        String type;
        String id;
        public abstract void run(GenericEvent event);
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public abstract static class Filter {
        private final Logger logger = LoggerFactory.getLogger(this.getClass());
        String type;
        String id;
        public abstract boolean canNext(GenericEvent event);
    }
}
