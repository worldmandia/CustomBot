package ua.mani123.discord;

import com.electronwill.nightconfig.core.CommentedConfig;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import ua.mani123.CBot;
import ua.mani123.config.CConfig;
import ua.mani123.discord.event.CustomEvents.CustomButtonInteractionEvent;
import ua.mani123.discord.event.CustomEvents.CustomCommandAutoCompleteInteraction;
import ua.mani123.discord.event.CustomEvents.CustomModalInteraction;
import ua.mani123.discord.event.CustomEvents.CustomSelectMenuInteraction;
import ua.mani123.discord.event.CustomEvents.CustomSlashCommandInteraction;
import ua.mani123.discord.event.CustomEvents.GuildEvent;
import ua.mani123.discord.event.CustomEvents.ReadyBot;

public class discordUtils {

  public static Map<String, JDA> initBots(CConfig cfg) {
    Map<String, JDA> bots = new HashMap<>();
    List<CommentedConfig> discordBotConfigs = cfg.getFileCfg().getOrElse("discord-bot", new ArrayList<>());
    discordBotConfigs.forEach(config -> {
      String token = config.get("token");
      if (token != null) {
        try {
          JDA jda = JDABuilder.createDefault(token).setContextEnabled(false).build();
          String id = jda.getSelfUser().getId();
          config.set("id", id);
          jda.addEventListener(new ReadyBot());
          if (config.getOrElse("enable-command-events", true)) {
            jda.addEventListener(new CustomSlashCommandInteraction());
          }
          if (config.getOrElse("enable-autocomplete-command-events", true)) {
            jda.addEventListener(new CustomCommandAutoCompleteInteraction());
          }
          if (config.getOrElse("enable-guild-events", true)) {
            jda.addEventListener(new GuildEvent());
          }
          if (config.getOrElse("enable-button-events", true)) {
            jda.addEventListener(new CustomButtonInteractionEvent());
          }
          if (config.getOrElse("enable-menu-events", true)) {
            jda.addEventListener(new CustomSelectMenuInteraction());
          }
          if (config.getOrElse("enable-modal-events", true)) {
            jda.addEventListener(new CustomModalInteraction());
          }
          bots.put(id, jda);
        } catch (Exception e) {
          CBot.getLog().warn("You get error: " + e.getMessage());
        }
      }
    });
    return bots;
  }

}