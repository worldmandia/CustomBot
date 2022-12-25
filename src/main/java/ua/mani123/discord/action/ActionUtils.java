package ua.mani123.discord.action;

import com.electronwill.nightconfig.core.CommentedConfig;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.UserSnowflake;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import ua.mani123.CBot;
import ua.mani123.addon.Addon;
import ua.mani123.addon.AddonData;
import ua.mani123.addon.AddonUtils;
import ua.mani123.config.CConfig;
import ua.mani123.discord.action.actions.ADD_MEMBER_ROLE;
import ua.mani123.discord.action.actions.BAN_USER;
import ua.mani123.discord.action.actions.DEAFEN_USER;
import ua.mani123.discord.action.actions.MUTE_USER;
import ua.mani123.discord.action.actions.REMOVE_MEMBER_ROLE;
import ua.mani123.discord.action.actions.SEND_EMBED;
import ua.mani123.discord.action.actions.SEND_MESSAGE;
import ua.mani123.discord.action.actions.TEMP_DATA_ADD;
import ua.mani123.discord.action.actions.TEMP_DATA_REMOVE;
import ua.mani123.discord.action.actions.UNBAN_USER;
import ua.mani123.discord.action.actions.UNDEAFEN_USER;
import ua.mani123.discord.action.actions.UNMUTE_USER;

public class ActionUtils {

  static HashMap<String, ArrayList<Action>> actionMap = new HashMap<>();

  public static void init(Map<String, CConfig> configs) {
    for (Map.Entry<String, CConfig> entry : configs.entrySet()) {
      ArrayList<CommentedConfig> actionsCfg = entry.getValue().getFileCfg().getOrElse("action", new ArrayList<>());
      String key = entry.getKey();
      actionMap.put(key, new ArrayList<>());
      sortActions(key, actionsCfg);
    }
  }

  public static void sortActions(String key, ArrayList<CommentedConfig> config) {
    for (CommentedConfig cfg : config) {
      String type = cfg.get("type");
      switch (type.trim().toUpperCase()) {
        case "TEMP_DATA_ADD" -> actionMap.get(key).add(new TEMP_DATA_ADD(cfg));
        case "TEMP_DATA_REMOVE" -> actionMap.get(key).add(new TEMP_DATA_REMOVE(cfg));
        case "SEND_MESSAGE" -> actionMap.get(key).add(new SEND_MESSAGE(cfg));
        case "MUTE_USER" -> actionMap.get(key).add(new MUTE_USER(cfg));
        case "UNMUTE_USER" -> actionMap.get(key).add(new UNMUTE_USER(cfg));
        case "DEAFEN_USER" -> actionMap.get(key).add(new DEAFEN_USER(cfg));
        case "UNDEAFEN_USER" -> actionMap.get(key).add(new UNDEAFEN_USER(cfg));
        case "SEND_EMBED" -> actionMap.get(key).add(new SEND_EMBED(cfg));
        case "BAN_USER" -> actionMap.get(key).add(new BAN_USER(cfg));
        case "UNBAN_USER" -> actionMap.get(key).add(new UNBAN_USER(cfg));
        case "ADD_MEMBER_ROLE" -> actionMap.get(key).add(new ADD_MEMBER_ROLE(cfg));
        case "REMOVE_MEMBER_ROLE" -> actionMap.get(key).add(new REMOVE_MEMBER_ROLE(cfg));
        default -> {
          for (Map.Entry<String, AddonData> addons : AddonUtils.getAddonMap().entrySet()) {
            Action action = addons.getValue().getAddon().addCustomAction(type, key, cfg);
            if (action != null) {
              actionMap.get(key).add(action);
            }
          }
        }
      }
    }
  }

  public static List<Member> getMembersFromList(GenericInteractionCreateEvent event, ArrayList<String> users) {
    List<Member> members = new ArrayList<>();
    if (!users.isEmpty()) {
      for (String name : users) {
        try {
          Member member = Objects.requireNonNull(event.getGuild()).getMemberByTag(name);
          members.add(member);
        } catch (Exception e) {
          CBot.getLog().warn("Member with name: " + name + " not found");
        }
      }
    }
    return members;
  }

  public static List<UserSnowflake> getUserSnowflakeFromList(GenericInteractionCreateEvent event, ArrayList<String> users) {
    List<UserSnowflake> members = new ArrayList<>();
    if (!users.isEmpty()) {
      for (String name : users) {
        Member member = Objects.requireNonNull(event.getGuild()).getMemberByTag(name);
        if (member != null) {
          members.add(member);
        } else {
          members.add(event.getJDA().getUserByTag(name));
        }
      }
    }
    return members;
  }

  public static List<Member> getMembersFromVoiceChat(GenericInteractionCreateEvent event, ArrayList<String> voiceChats) {
    List<Member> members = new ArrayList<>();
    if (!voiceChats.isEmpty()) {
      for (String chatName : voiceChats) {
        VoiceChannel voiceChannel = Objects.requireNonNull(event.getGuild()).getVoiceChannelsByName(chatName, false).get(0);
        if (voiceChannel != null) {
          members.addAll(voiceChannel.getMembers());
        }
      }
    }
    return members;
  }

  public static List<Member> getMembersFromFocusedOptions(GenericInteractionCreateEvent event, ArrayList<String> focusedOptionIds) {
    List<Member> members = new ArrayList<>();
    if (!focusedOptionIds.isEmpty()) {
      if (event instanceof SlashCommandInteractionEvent commandEvent) {
        for (String id : focusedOptionIds) {
          Member member = Objects.requireNonNull(commandEvent.getOption(id)).getAsMember();
          if (member != null) {
            members.add(member);
          }
        }
      }
    }
    return members;
  }

  public static Set<UserSnowflake> getAllUsers(GenericInteractionCreateEvent event, ArrayList<String> users, ArrayList<String> focusedOptionIds, ArrayList<String> voiceChats, ArrayList<String> members) {
    HashSet<UserSnowflake> UserSnowflake = new HashSet<>();

    UserSnowflake.addAll(getMembersFromList(event, members));
    UserSnowflake.addAll(getMembersFromFocusedOptions(event, focusedOptionIds));
    UserSnowflake.addAll(getMembersFromVoiceChat(event, voiceChats));
    UserSnowflake.addAll(getUserSnowflakeFromList(event, users));

    return UserSnowflake;
  }

  public static Color getHexToColor(String colorName) {
    colorName = colorName.replace("#", "").toUpperCase();
    return switch (colorName.length()) {
      case 6 -> new Color(
          Integer.valueOf(colorName.substring(0, 2), 16),
          Integer.valueOf(colorName.substring(2, 4), 16),
          Integer.valueOf(colorName.substring(4, 6), 16)
      );
      case 8 -> new Color(
          Integer.valueOf(colorName.substring(0, 2), 16),
          Integer.valueOf(colorName.substring(2, 4), 16),
          Integer.valueOf(colorName.substring(4, 6), 16),
          Integer.valueOf(colorName.substring(6, 8), 16)
      );
      default -> Color.BLACK;
    };
  }

  public static HashMap<String, ArrayList<Action>> getActionMap() {
    return actionMap;
  }
}
