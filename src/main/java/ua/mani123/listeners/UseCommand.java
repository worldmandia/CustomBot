package ua.mani123.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.jetbrains.annotations.NotNull;
import ua.mani123.DTBot;
import ua.mani123.command.CommandActions;
import ua.mani123.command.CustomCommand;
import ua.mani123.ticket.TicketButton;
import ua.mani123.ticket.TicketType;
import ua.mani123.ticket.TicketUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UseCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        for (CustomCommand cmd : DTBot.getCommands().get(CommandActions.CREATE_TICKET_EMBED)) {
            if (event.getName().equals(cmd.getName())) {
                if (cmd.getPermission() == null || event.getUser().getAsTag().equals(cmd.getPermission().trim())) {
                    switch (TicketType.valueOf(event.getOption("ticket_type", OptionMapping::getAsString))) {
                        case TICKET_BUTTON -> {
                            try {
                                TicketButton ticketButton = (TicketButton) TicketUtils.getTicketById(event.getOption("ticket_type", OptionMapping::getAsString),
                                        event.getOption("ticket_id", OptionMapping::getAsString),
                                        DTBot.getTickets());
                                MessageAction action = event.getInteraction().getChannel().sendMessageEmbeds(ticketButton.getEmbed()).setActionRow(ticketButton.getButtons());
                                action.queue(
                                        (success) -> event.replyEmbeds(new EmbedBuilder()
                                                .setAuthor(getWithPlaceholders(DTBot.getLang().getString("embeds.success-title", "Not found **embeds.success-title**"), event, "command"))
                                                .setDescription(getWithPlaceholders(DTBot.getLang().getString("embeds.success-description", "Not found **embeds.success-title**"), event, "command (" + event.getName() + ")"))
                                                .build()).setEphemeral(true).queue(),
                                        (error) -> DTBot.getLogger().error(error.getMessage()));
                                return;
                            } catch (Exception e) {
                                DTBot.getLogger().error("Commands error: " + e.getMessage());
                            }
                        }
                    }
                } else {
                    event.replyEmbeds(new EmbedBuilder()
                            .setAuthor(getWithPlaceholders(DTBot.getLang().getString("embeds.not-perms-title", "Not found **embeds.not-perms-title**"), event, "perms"))
                            .setDescription(getWithPlaceholders(DTBot.getLang().getString("embeds.not-perms-description", "Not found **embeds.not-perms-description**"), event, cmd.getPermission()))
                            .build()).setEphemeral(true).queue();
                }
            }
        }
    }

    public String getWithPlaceholders(String s, GenericInteractionCreateEvent event, String action) {
        return s
                .replaceAll("%username%", event.getUser().getAsMention())
                .replaceAll("%data%", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                .replaceAll("%action%", action);
    }
}
