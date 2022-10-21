package ua.mani123;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import ua.mani123.addon.AddonData;
import ua.mani123.addon.Addon;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

public class AddonUtils {

    static Map<String, AddonData> addonMap = new HashMap<>();

    public static void loadAddons(String path) {
        File folder = new File(path);
        if (folder.exists() && folder.isDirectory()) {
            for (File file : folder.listFiles()) {
                if (!file.getName().startsWith("_") && file.getName().endsWith(".jar")) {
                    try {
                        ClassLoader classLoader = URLClassLoader.newInstance(new URL[]{file.toURI().toURL()});
                        CommentedFileConfig addonConfig = CommentedFileConfig.of(classLoader.getResource("addon.toml").getFile());
                        addonConfig.load();
                        Addon addon = (Addon) classLoader.loadClass(addonConfig.get("main")).getDeclaredConstructor().newInstance();
                        AddonData addonData = new AddonData(addonConfig, addon);
                        addonMap.put(addonData.getName(), addonData);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        } else {
            if (folder.mkdirs()) {
                loadAddons(path);
            }
        }
    }

    public static void enableAddons(Map<String, AddonData> addonMap) {
        for (Map.Entry<String, AddonData> entry : addonMap.entrySet()) {
            entry.getValue().getAddon().enable();
        }
    }

}
