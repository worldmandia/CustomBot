package ua.mani123.discord.action;

import ua.mani123.config.CConfig;
import ua.mani123.discord.action.actions.SEND_MESSAGE;

import java.util.HashMap;
import java.util.Map;

public class actionUtils {

    static Map<String, Action> actionMap = new HashMap<>();

    public static void init(Map<String, CConfig> configs) {
        for (Map.Entry<String, CConfig> entry : configs.entrySet()) {
            String type = entry.getValue().getFileCfg().get("type");
            if (type != null) {
                type = type.toUpperCase().trim();
                if (type.equals("SEND_MESSAGE")) {
                    actionMap.put(entry.getKey(), new SEND_MESSAGE(entry.getValue().getFileCfg()));
                }
            }
        }
    }

    public static Map<String, Action> getActionMap() {
        return actionMap;
    }
}
