package ua.mani123.Listeners;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import ua.mani123.DTBot;
import ua.mani123.ticket.TicketButton;
import ua.mani123.ticket.TicketType;

public class UseCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals(DTBot.getLang().getString("commands.embed.name", "ticketembed"))){
            TicketButton ticketButton = (TicketButton) DTBot.getTickets().get(TicketType.valueOf(event.getOption("type").getName()));
            event.replyEmbeds(ticketButton.getEmbed()).addActionRow(ticketButton.getButton()).queue();
        }
    }
}
