package ua.mani123.discord.event;

import java.util.ArrayList;
import java.util.Map;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;
import ua.mani123.CBot;
import ua.mani123.discord.action.TempData;
import ua.mani123.discord.action.filter.filterUtils;
import ua.mani123.discord.interaction.InteractionUtils;
import ua.mani123.discord.interaction.interactions.CommandInteraction;

public class ReadyBot extends ListenerAdapter {

  @Override
  public void onReady(@NotNull ReadyEvent event) {
    ArrayList<CommandData> commandData = new ArrayList<>();
    TempData tempData = new TempData();
    for (Map.Entry<String, CommandInteraction> command : InteractionUtils.getCommands().entrySet()) {
      if (!command.getValue().isOnlyGuild() && filterUtils.filterCheck(command.getValue().getFilters(), event, tempData)) {
        commandData.add(command.getValue().getCommandData());
      }
    }
    event.getJDA().updateCommands().addCommands(commandData).queue();
    CBot.getLog().info(commandData.size() + " global commands added to " + event.getJDA().getSelfUser().getName());
  }
}
