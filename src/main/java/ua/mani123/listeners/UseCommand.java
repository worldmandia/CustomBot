package ua.mani123.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import ua.mani123.DTBot;
import ua.mani123.ticket.TicketButton;
import ua.mani123.ticket.TicketUtils;

public class UseCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals(DTBot.getLang().getString("commands.ticketembed.name", "ticketembed"))) {
            TicketButton ticketButton = (TicketButton) TicketUtils.getTicketById(event.getOption("type", OptionMapping::getAsString),
                    event.getOption("id", OptionMapping::getAsString),
                    DTBot.getTickets());
            if (ticketButton != null) {
                try {
                    event.getInteraction().getChannel().sendMessageEmbeds(ticketButton.getEmbed()).setActionRow(ticketButton.getButtons()).queue();
                    event.replyEmbeds(new EmbedBuilder()
                            .setAuthor(DTBot.getLang().getString("embeds.success.cmd.title", "Not found **embeds.success.cmd.title**"))
                            .setDescription(DTBot.getLang().getString("embeds.success.cmd.description", "Not found **embeds.success.cmd.description**"))
                            .build()).setEphemeral(true).queue();
                } catch (Exception e) {
                    DTBot.getLogger().error("Commands error: " + e.getMessage());
                }
            }
        }
    }
}
