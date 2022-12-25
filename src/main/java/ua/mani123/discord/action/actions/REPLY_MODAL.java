package ua.mani123.discord.action.actions;

import java.util.ArrayList;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.apache.commons.text.StringSubstitutor;
import ua.mani123.CBot;
import ua.mani123.discord.action.Action;
import ua.mani123.discord.action.TempData;
import ua.mani123.discord.action.filter.Filter;

// TODO REPLY_MODAL
public class REPLY_MODAL implements Action {

  @Override
  public void run(GenericInteractionCreateEvent event, TempData tempData) {
    TextInput subject = TextInput.create("subject", "Subject", TextInputStyle.SHORT)
        .setPlaceholder("Subject of this ticket")
        .setMinLength(10)
        .setMaxLength(100) // or setRequiredRange(10, 100)
        .build();

    TextInput body = TextInput.create("body", "Body", TextInputStyle.PARAGRAPH)
        .setPlaceholder("Your concerns go here")
        .setMinLength(30)
        .setMaxLength(1000)
        .build();

    Modal modal = Modal.create("modmail", "Modmail")
        .addActionRows(ActionRow.of(subject), ActionRow.of(body))
        .build();
    if (event instanceof SlashCommandInteractionEvent slashCommandInteractionEvent) {
      slashCommandInteractionEvent.replyModal(modal).queue();
    } else if (event instanceof ButtonInteractionEvent buttonInteractionEvent) {
      buttonInteractionEvent.replyModal(modal).queue();
    } else {
      CBot.getLog().warn(event.getType().name() + " - not support modals");
    }
  }

  @Override
  public void runWithPlaceholders(GenericInteractionCreateEvent event, StringSubstitutor str, TempData tempData) {

  }

  @Override
  public ArrayList<Filter> getFilters() {
    return Action.super.getFilters();
  }
}
