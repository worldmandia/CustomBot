package ua.mani123.discordModule.filters;

import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.events.GenericEvent;
import ua.mani123.config.Objects.DiscordConfigs;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

@Getter
@Setter
public class DISCORD_BOT extends DiscordConfigs.Filter {

    private ArrayList<String> discordBotIds;
    private boolean whitelist;
    public DISCORD_BOT(String type, String id, ArrayList<String> discordBotIds, boolean whitelist, ArrayList<DiscordConfigs.Order> actions) {
        super(type, id);
        this.discordBotIds = discordBotIds;
        this.whitelist = whitelist;
        this.getDenyOrders().addAll(actions);
    }


    @Override
    public boolean canNext(GenericEvent event) {
        boolean answer = whitelist == discordBotIds.contains(event.getJDA().getSelfUser().getId());
        if (!answer) {
            AtomicBoolean orderExecuted = new AtomicBoolean(true);

            getDenyOrders().stream()
                    .filter(order -> {
                        if (order instanceof DiscordConfigs.Filter filter) {
                            boolean canNext = filter.canNext(event);
                            orderExecuted.set(canNext);
                            return true;
                        }
                        return orderExecuted.get();
                    })
                    .filter(order -> order instanceof DiscordConfigs.Action && orderExecuted.get())
                    .map(order -> (DiscordConfigs.Action) order)
                    .forEach(action -> {
                        action.run(event);
                        orderExecuted.set(true);
                    });
        }
        return answer;
    }
}
