package ua.mani123.config.Objects;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.conversion.IgnoreValue;
import com.electronwill.nightconfig.core.conversion.Path;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.GenericEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.mani123.config.ConfigDefaults;
import ua.mani123.discordModule.TempData;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
public class DiscordConfigs extends ConfigDefaults {

    @Path("Action")
    ArrayList<CommentedConfig> actionConfigs = new ArrayList<>();
    @Path("Filter")
    ArrayList<CommentedConfig> filterConfigs = new ArrayList<>();
    @Path("Interaction")
    ArrayList<CommentedConfig> interactionConfigs = new ArrayList<>();

    @IgnoreValue
    ArrayList<Action> actions = new ArrayList<>();

    @IgnoreValue
    ArrayList<Filter> filters = new ArrayList<>();

    @IgnoreValue
    ArrayList<Interaction> interactions = new ArrayList<>();

    @Getter
    @Setter
    public abstract static class Action extends Order {
        private final Logger logger = LoggerFactory.getLogger(this.getClass());

        public Action(String type, String id) {
            super(type, id);
        }

        public abstract void run(GenericEvent event, TempData tempData);
    }

    @Getter
    @Setter
    public abstract static class Filter extends Order {
        private final Logger logger = LoggerFactory.getLogger(this.getClass());
        private ArrayList<Order> denyOrders = new ArrayList<>();
        private int denyOrdersAfterFilter = 0;

        public Filter(String type, String id) {
            super(type, id);
        }

        public abstract boolean canNext(GenericEvent event, TempData tempData);
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public abstract static class Interaction {
        private final Logger logger = LoggerFactory.getLogger(this.getClass());
        String type;
        String id;
        public abstract void init(JDA jda);
        public abstract void run(GenericEvent event, TempData tempData);
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public abstract static class Order {
        String type;
        String id;
    }
}
