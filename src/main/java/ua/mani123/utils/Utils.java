package ua.mani123.utils;

import net.dv8tion.jda.api.entities.Member;
import ua.mani123.DTBot;
import ua.mani123.action.Action;
import ua.mani123.interaction.Interaction;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public static Color decode(String hex) {
        if (!hex.startsWith("#")) {
            hex = "#" + hex;
        }
        return Color.decode(hex);
    }

    public static String placeholder(String string, Map<String, String> placeholders) {
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            if (string != null) {
                string = string.replaceAll(entry.getKey(), entry.getValue());
            } else {
                string = "Not found string in file";
            }
        }
        return string;
    }

    public static String placeholder(String string, Map<String, String> placeholders, Member member) {
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            if (string != null) {
                string = string.replaceAll(entry.getKey(), entry.getValue());
            } else {
                string = "Not found string in file";
            }
        }
        Pattern pattern = Pattern.compile("%database-[a-zA-Z]+%");
        Matcher matcher = pattern.matcher(string);
        for (int i = 0; i < matcher.groupCount(); i++) {
            string = string.replaceAll(matcher.group(i), DTBot.getDatabase().getOrElse(member.getGuild().getId() + "." + member.getUser().getId() + "." + matcher.group(i).replace("database-", "").replaceAll("%", ""), "Not found"));
        }
        return string;
    }

    public static Map<String, Interaction> filterInteraction(Map<String, Interaction> stringMap, ArrayList<String> list) {
        Map<String, Interaction> filteredMap = new HashMap<>();
        list.forEach(s -> {
            if (stringMap.containsKey(s)) {
                filteredMap.put(s, stringMap.get(s));
            }
        });
        return filteredMap;
    }

    public static Map<String, Action> filterAction(Map<String, Action> stringMap, ArrayList<String> list) {
        Map<String, Action> filteredMap = new HashMap<>();
        list.forEach(s -> {
            if (stringMap.containsKey(s)) {
                filteredMap.put(s, stringMap.get(s));
            }
        });
        return filteredMap;
    }
}
