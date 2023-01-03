package ua.mani123.discord.event.CustomEvents;

import java.util.HashSet;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;
import ua.mani123.CBot;
import ua.mani123.discord.action.TempData;
import ua.mani123.discord.action.filter.filterUtils;
import ua.mani123.discord.interaction.InteractionTypes;
import ua.mani123.discord.interaction.InteractionUtils;
import ua.mani123.discord.interaction.interactions.CommandInteraction;

public class GuildEvent extends ListenerAdapter {

  @Override
  public void onGuildReady(@NotNull GuildReadyEvent event) {
    HashSet<CommandData> commandData = new HashSet<>();
    InteractionUtils.getInteractions().get(InteractionTypes.COMMAND).forEach(interaction -> {
      if (interaction instanceof CommandInteraction commandInteraction) {
        TempData tempData = new TempData();
        if (commandInteraction.isOnlyGuild() && filterUtils.filterCheck(commandInteraction.getFilters(), event, tempData)) {
          commandData.add(commandInteraction.getCommandData());
        }
      }
    });
    if (commandData.size() > 0) {
      event.getGuild().updateCommands().addCommands(commandData).queue();
      CBot.getLog().info(commandData.size() + " commands added to guild: " + event.getGuild().getName());
    }
  }
}
