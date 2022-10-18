package ua.mani123.discord.action;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import ua.mani123.config.CConfig;
import ua.mani123.discord.action.actions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class actionUtils {

    static Map<String, Action> actionMap = new HashMap<>();

    public static void init(Map<String, CConfig> configs) {
        for (Map.Entry<String, CConfig> entry : configs.entrySet()) {
            String type = entry.getValue().getFileCfg().get("type");
            if (type != null) {
                type = type.toUpperCase().trim();
                switch (type) {
                    case "SEND_MESSAGE" ->
                            actionMap.put(entry.getKey(), new SEND_MESSAGE(entry.getValue().getFileCfg()));
                    case "MUTE_USER" -> actionMap.put(entry.getKey(), new MUTE_USER(entry.getValue().getFileCfg()));
                    case "UNMUTE_USER" -> actionMap.put(entry.getKey(), new UNMUTE_USER(entry.getValue().getFileCfg()));
                    case "DEAFEN_USER" -> actionMap.put(entry.getKey(), new DEAFEN_USER(entry.getValue().getFileCfg()));
                    case "UNDEAFEN_USER" -> actionMap.put(entry.getKey(), new UNDEAFEN_USER(entry.getValue().getFileCfg()));
                    case "SEND_EMBED" -> actionMap.put(entry.getKey(), new SEND_EMBED(entry.getValue().getFileCfg()));
                }
            }
        }
    }

    public static List<Member> getMembersFromList(GenericInteractionCreateEvent event, List<String> users){
        List<Member> members = new ArrayList<>();
        if (!users.isEmpty()) {
            for (String name : users) {
                Member member = event.getGuild().getMemberByTag(name);
                if (member != null) {
                    members.add(member);
                }
            }
        }
        return members;
    }

    public static List<Member> getMembersFromVoiceChat(GenericInteractionCreateEvent event, List<String> voiceChats){
        List<Member> members = new ArrayList<>();
        if (!voiceChats.isEmpty()) {
            for (String chatName: voiceChats) {
                VoiceChannel voiceChannel = event.getGuild().getVoiceChannelsByName(chatName, false).get(0);
                if (voiceChannel != null) {
                    members.addAll(voiceChannel.getMembers());
                }
            }
        }
        return members;
    }

    public static List<Member> getMembersFromFocusedOptions(GenericInteractionCreateEvent event, List<String> focusedOptionIds){
        List<Member> members = new ArrayList<>();
        if (!focusedOptionIds.isEmpty()) {
            if (event instanceof SlashCommandInteractionEvent commandEvent){
                for (String id: focusedOptionIds) {
                    Member member = commandEvent.getOption(id).getAsMember();
                    if (member != null) {
                        members.add(member);
                    }
                }
            }
        }
        return members;
    }

    public static Map<String, Action> getActionMap() {
        return actionMap;
    }
}
