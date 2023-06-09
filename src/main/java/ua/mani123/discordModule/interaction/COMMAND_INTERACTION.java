package ua.mani123.discordModule.interaction;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import ua.mani123.config.Objects.DiscordConfigs;

import java.util.ArrayList;

@NoArgsConstructor
@Getter
@Setter
public class COMMAND_INTERACTION extends DiscordConfigs.Interaction {
    private String commandDescription;
    private ArrayList<DiscordConfigs.Order> orders;
    private ArrayList<String> allowedGuilds;

    public COMMAND_INTERACTION(String type, String id, String commandDescription, ArrayList<DiscordConfigs.Order> orders, ArrayList<String> allowedGuilds) {
        super(type, id);
        this.commandDescription = commandDescription;
        this.orders = orders;
        this.allowedGuilds = allowedGuilds;
    }

    @Override
    public void init(JDA jda) {
        jda.updateCommands().addCommands(Commands.slash(getId().toLowerCase(), commandDescription)).queue();
        //jda.getGuilds().stream().filter(guild -> allowedGuilds.contains(guild.getId())).forEach(guild -> guild.upsertCommand(getId().toLowerCase(), commandDescription).queue());
    }

    @Override
    public void run(GenericEvent event) {
        if (event instanceof SlashCommandInteraction) {
            orders.stream().filter(order -> {
                if (order instanceof DiscordConfigs.Filter filter) return filter.canNext(event);
                else return true;
            }).forEach(order -> {
                if (order instanceof DiscordConfigs.Action action) action.run(event);
            });
        }

    }
}
