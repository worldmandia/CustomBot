package ua.mani123.discordModule.interaction;

import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import ua.mani123.config.Objects.DiscordConfigs;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

@Getter
@Setter
public class COMMAND_INTERACTION extends DiscordConfigs.Interaction {
    private String commandDescription;
    private ArrayList<DiscordConfigs.Order> orders;
    private ArrayList<String> allowedGuilds;
    private boolean guildOnly;
    private boolean nsfw;

    public COMMAND_INTERACTION(String type, String id, String commandDescription, ArrayList<DiscordConfigs.Order> orders, ArrayList<String> allowedGuilds, boolean guildOnly, boolean nsfw) {
        super(type, id);
        this.commandDescription = commandDescription;
        this.orders = orders;
        this.allowedGuilds = allowedGuilds;
        this.guildOnly = guildOnly;
        this.nsfw = nsfw;
    }

    @Override
    public void init(JDA jda) {
        jda.updateCommands().addCommands(Commands.slash(getId().toLowerCase(), commandDescription).setGuildOnly(guildOnly).setNSFW(nsfw)).queue();
        //jda.getGuilds().stream().filter(guild -> allowedGuilds.contains(guild.getId())).forEach(guild -> guild.upsertCommand(getId().toLowerCase(), commandDescription).queue());
    }

    @Override
    public void run(GenericEvent event) {
        if (event instanceof SlashCommandInteraction) {
            AtomicBoolean orderExecuted = new AtomicBoolean(true);

            orders.stream()
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

    }
}
