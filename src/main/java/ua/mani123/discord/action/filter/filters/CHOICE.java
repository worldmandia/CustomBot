package ua.mani123.discord.action.filter.filters;

import com.electronwill.nightconfig.core.CommentedConfig;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import net.dv8tion.jda.api.events.guild.GenericGuildEvent;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.GenericSessionEvent;
import ua.mani123.CBot;
import ua.mani123.discord.action.ActionUtils;
import ua.mani123.discord.action.TempData;
import ua.mani123.discord.action.filter.Filter;

public class CHOICE implements Filter {

  private final ArrayList<String> actionNames;
  HashSet<String> options;
  boolean isBlackList;
  private final ArrayList<String> beforeActionNames;


  public CHOICE(CommentedConfig config) {
    this.options = config.getOrElse("options", new HashSet<>());
    this.isBlackList = config.getOrElse("isBlackList", false);
    this.actionNames = config.getOrElse("filter-actions", new ArrayList<>());
    this.beforeActionNames = config.getOrElse("before-filter-actions", new ArrayList<>());
  }

  @Override
  public boolean canRun(GenericInteractionCreateEvent event, TempData tempData) {
    beforeActionNames.forEach(s -> ActionUtils.getActionMap().get(s).run(event, tempData));
    if (event instanceof SlashCommandInteractionEvent commandInteractionEvent) {
      if (!options.isEmpty()) {
        AtomicBoolean answer = new AtomicBoolean(true);
        HashMap<String, String> mapOptionsIds = new HashMap<>();
        options.forEach(s -> {
          String[] mas = s.split("::", 2);
          mapOptionsIds.put(mas[0], mas[1]);
        });
        mapOptionsIds.forEach((k, v) -> {
              if (answer.get()) {
                answer.set(Objects.requireNonNull(commandInteractionEvent.getInteraction().getOption(k)).getAsString().equals(v));
              }
            }
        );
        if (isBlackList) {
          return !answer.get();
        } else {
          return answer.get();
        }
      }
    } else {
      CBot.getLog().warn("CHOICE feature support only commandInteractionEvent");
    }
    return false;
  }

  @Override
  public boolean canRun(GenericGuildEvent event, TempData tempData) {
    CBot.getLog().warn("GenericGuildEvent not support CHOICE feature");
    return false;
  }

  @Override
  public boolean canRun(GenericSessionEvent event, TempData tempData) {
    CBot.getLog().warn("GenericSessionEvent not support CHOICE feature");
    return false;
  }

  @Override
  public ArrayList<String> getFilterActionIds() {
    return this.actionNames;
  }
}
