package ua.mani123.utils;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.file.FileConfig;
import ua.mani123.interaction.InteractionType;
import ua.mani123.ticket.TicketType;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {
    public static Color decode(String hex) {
        if (!hex.startsWith("#")) {
            hex = "#" + hex;
        }
        return Color.decode(hex);
    }

    public static List<CommentedConfig> getMap(String path, FileConfig file) {
        return file.get(path);
    }

}
