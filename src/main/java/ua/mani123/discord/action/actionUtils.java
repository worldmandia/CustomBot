package ua.mani123.discord.action;

import ua.mani123.config.CConfig;
import ua.mani123.discord.action.actions.MUTE_USER;
import ua.mani123.discord.action.actions.SEND_MESSAGE;
import ua.mani123.discord.action.actions.UNMUTE_USER;

import java.util.HashMap;
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
                }
            }
        }
    }

    public static Map<String, Action> getActionMap() {
        return actionMap;
    }
}
