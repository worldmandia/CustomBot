package ua.mani123.discord.event;

import java.util.ArrayList;
import java.util.Map;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;
import ua.mani123.CBot;
import ua.mani123.discord.action.filter.filterUtils;
import ua.mani123.discord.interaction.InteractionUtils;
import ua.mani123.discord.interaction.interactions.CommandInteraction;

public class GuildEvent extends ListenerAdapter {

  @Override
  public void onGuildReady(@NotNull GuildReadyEvent event) {
    ArrayList<CommandData> commandData = new ArrayList<>();
    for (Map.Entry<String, CommandInteraction> command : InteractionUtils.getCommands().entrySet()) {
      if (command.getValue().isOnlyGuild() && filterUtils.filterCheck(command.getValue().getFilters(), event)) {
        commandData.add(command.getValue().getCommand());
      }
    }
    if (commandData.size() > 0) {
      event.getGuild().updateCommands().addCommands(commandData).queue();
      CBot.getLog().info(commandData.size() + " commands added to guild: " + event.getGuild().getName());
    }
  }
}
