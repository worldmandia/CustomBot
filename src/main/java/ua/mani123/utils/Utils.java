package ua.mani123.utils;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.file.FileConfig;

import java.awt.*;
import java.util.List;

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
