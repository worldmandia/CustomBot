package ua.mani123.utils;

import ua.mani123.action.Action;
import ua.mani123.interaction.Interaction;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Utils {
    public static Color decode(String hex) {
        if (!hex.startsWith("#")) {
            hex = "#" + hex;
        }
        return Color.decode(hex);
    }

    public static String placeholder(String string, Map<String, String> placeholders) {
        for (Map.Entry<String, String> entry: placeholders.entrySet()) {
            if (string != null) {
                string = string.replaceAll(entry.getKey(), entry.getValue());
            } else {
                string = "Not found string in file";
            }
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
