package ua.mani123.utils;

import net.dv8tion.jda.api.entities.Member;
import ua.mani123.DTBot;
import ua.mani123.action.botAction;
import ua.mani123.interaction.botInteraction;

import java.awt.*;
import java.time.LocalDateTime;
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
        updateGlobalPlaceholders(placeholders);
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
        updateGlobalPlaceholders(placeholders);
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

    static void updateGlobalPlaceholders(Map<String, String> placeholders){
        placeholders.put("%data-month%", String.valueOf(LocalDateTime.now().getMonthValue()));
        placeholders.put("%data-day%", String.valueOf(LocalDateTime.now().getDayOfMonth()));
        placeholders.put("%data-hour%", String.valueOf(LocalDateTime.now().getHour()));
        placeholders.put("%data-year%", String.valueOf(LocalDateTime.now().getYear()));
        placeholders.put("%data-minute%", String.valueOf(LocalDateTime.now().getMinute()));
    }

    public static Map<String, botInteraction> filterInteraction(Map<String, botInteraction> stringMap, ArrayList<String> list) {
        Map<String, botInteraction> filteredMap = new HashMap<>();
        list.forEach(s -> {
            if (stringMap.containsKey(s)) {
                filteredMap.put(s, stringMap.get(s));
            }
        });
        return filteredMap;
    }

    public static Map<String, botAction> filterAction(Map<String, botAction> stringMap, ArrayList<String> list) {
        Map<String, botAction> filteredMap = new HashMap<>();
        list.forEach(s -> {
            if (stringMap.containsKey(s)) {
                filteredMap.put(s, stringMap.get(s));
            }
        });
        return filteredMap;
    }
}
