package ua.mani123.Listeners;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import ua.mani123.DTBot;
import ua.mani123.ticket.TicketButton;
import ua.mani123.utils.Utils;

public class UseCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
       if (event.getName().equals(DTBot.getLang().getString("commands.ticketembed.name", "ticketembed"))){
           TicketButton ticketButton = (TicketButton) Utils.getTicketById(event.getOption("id", OptionMapping::getAsString), DTBot.getTickets());
           if (ticketButton != null){
               event.getInteraction().getChannel().sendMessageEmbeds(ticketButton.getEmbed()).setActionRow(ticketButton.getButtons()).queue();
           }
       }
    }
}
