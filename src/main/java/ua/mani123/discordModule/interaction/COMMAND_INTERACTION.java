package ua.mani123.discordModule.interaction;

import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.UserSnowflake;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import ua.mani123.config.Objects.DiscordConfigs;
import ua.mani123.discordModule.TempData;
import ua.mani123.discordModule.Utils;
import ua.mani123.discordModule.additions.COMMAND_OPTION;
import ua.mani123.discordModule.listeners.ReadyListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@Setter
public class COMMAND_INTERACTION extends DiscordConfigs.Interaction {
    private String commandDescription;
    private ArrayList<DiscordConfigs.Order> orders;
    private ArrayList<DiscordConfigs.Addition> additions;
    private ArrayList<String> allowedGuilds;
    private ArrayList<String> allowedBots;
    private boolean guildOnly;
    private boolean nsfw;

    public COMMAND_INTERACTION(String type, String id, String commandDescription, ArrayList<DiscordConfigs.Order> orders, ArrayList<DiscordConfigs.Addition> additions, ArrayList<String> allowedGuilds, ArrayList<String> allowedBots, boolean guildOnly, boolean nsfw) {
        super(type, id);
        this.commandDescription = commandDescription;
        this.orders = orders;
        this.additions = additions;
        this.allowedGuilds = allowedGuilds;
        this.allowedBots = allowedBots;
        this.guildOnly = guildOnly;
        this.nsfw = nsfw;
    }

    @Override
    public void init(JDA jda) {
        SlashCommandData command = Commands.slash(getId().toLowerCase(), commandDescription).setGuildOnly(guildOnly).setNSFW(nsfw);
        command.addOptions(additions.stream().filter(addition -> addition instanceof COMMAND_OPTION).map(option -> ((COMMAND_OPTION) option).get()).filter(Objects::nonNull).collect(Collectors.toList()));
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
    public void run(GenericEvent event, TempData tempData) {
        if (event instanceof SlashCommandInteraction slashCommandInteraction) {
            if (slashCommandInteraction.getName().equalsIgnoreCase(getId())) {
                for (OptionMapping option : slashCommandInteraction.getOptions()) {
                    switch (option.getType()) {
                        case ROLE -> tempData.getRoles().add(option.getAsRole());
                        case STRING -> tempData.getStrings().put(option.getName(), option.getAsString());
                        case USER -> tempData.getUsers().add(option.getAsUser());
                        case NUMBER -> tempData.getNumbers().put(option.getName(), option.getAsInt());
                        case BOOLEAN -> tempData.getBooleans().put(option.getName(), option.getAsBoolean());
                        case MENTIONABLE -> {
                            if (option.getAsMentionable() instanceof UserSnowflake userSnowflake) tempData.getUsers().add(userSnowflake);
                            else if (option.getAsMentionable() instanceof Role role) tempData.getRoles().add(role);
                        }
                        case CHANNEL -> tempData.getChannels().put(option.getName(), option.getAsChannel());
                        default -> getLogger().warn("Option: " + option.getName() + " with type " + option.getType() + " dont support");
                    }
                }
                Utils.runOrdersWithFilterSystem(event, orders, tempData);
            }
        }
    }
}
