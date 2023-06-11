package ua.mani123.discordModule.interaction;

import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import ua.mani123.config.Objects.DiscordConfigs;
import ua.mani123.discordModule.Utils;
import ua.mani123.listeners.ReadyListener;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class COMMAND_INTERACTION extends DiscordConfigs.Interaction {
    private String commandDescription;
    private ArrayList<DiscordConfigs.Order> orders;
    private ArrayList<String> allowedGuilds;
    private ArrayList<String> allowedBots;
    private boolean guildOnly;
    private boolean nsfw;

    public COMMAND_INTERACTION(String type, String id, String commandDescription, ArrayList<DiscordConfigs.Order> orders, ArrayList<String> allowedGuilds, ArrayList<String> allowedBots, boolean guildOnly, boolean nsfw) {
        super(type, id);
        this.commandDescription = commandDescription;
        this.orders = orders;
        this.allowedGuilds = allowedGuilds;
        this.allowedBots = allowedBots;
        this.guildOnly = guildOnly;
        this.nsfw = nsfw;
    }

    @Override
    public void init(JDA jda) {
        SlashCommandData command = Commands.slash(getId().toLowerCase(), commandDescription).setGuildOnly(guildOnly).setNSFW(nsfw);
        if (!allowedGuilds.isEmpty()) {
            jda.getGuilds().stream().filter(guild -> allowedGuilds.contains(guild.getId())).forEach(guild -> {
                if (ReadyListener.commandsPerGuild.containsKey(guild.getId())) {
                    ReadyListener.commandsPerGuild.get(guild.getId()).add(command);
                } else {
                    ReadyListener.commandsPerGuild.put(guild.getId(), new ArrayList<>(List.of(command)));
                }
            });
        }
        if (!ReadyListener.commandsPerBot.containsKey(jda.getSelfUser().getId())) {
            ReadyListener.commandsPerBot.put(jda.getSelfUser().getId(), new ArrayList<>());
        }
        if (!allowedBots.isEmpty()) {
            if (allowedBots.contains(jda.getSelfUser().getId())) {
                ReadyListener.commandsPerBot.get(jda.getSelfUser().getId()).add(command);
            }
        } else {
            ReadyListener.commandsPerBot.get(jda.getSelfUser().getId()).add(command);
        }
    }

    @Override
    public void run(GenericEvent event) {
        if (event instanceof SlashCommandInteraction) {
            Utils.runOrdersWithFilterSystem(event, orders);
        }
    }
}
