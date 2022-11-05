package ua.mani123;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import ua.mani123.addon.Addon;
import ua.mani123.addon.AddonData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

public class AddonUtils {

    public static Map<String, AddonData> addonMap = new HashMap<>();

    public static void loadAddons(String path) {
        File folder = new File(path);
        if (folder.exists() && folder.isDirectory()) {
            for (File jarFile : folder.listFiles()) {
                if (!jarFile.getName().startsWith("_") && jarFile.getName().endsWith(".jar")) {
                    try {
                        ClassLoader classLoader = URLClassLoader.newInstance(new URL[]{jarFile.toURI().toURL()});
                        File tempFile = File.createTempFile("temp", ".toml");
                        OutputStream output = new FileOutputStream(tempFile);
                        classLoader.getResourceAsStream("addon.toml").transferTo(output);
                        CommentedFileConfig addonConfig = CommentedFileConfig.of(tempFile);
                        addonConfig.load();
                        if (classLoader.loadClass(addonConfig.get("main")).getDeclaredConstructor().newInstance() instanceof Addon addon) {
                            AddonData addonData = new AddonData(addonConfig, addon);
                            addonMap.put(addonData.getName(), addonData);
                        }
                        tempFile.delete();
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
        addonMap.forEach((key, value) -> {
            value.getAddon().disable();
            CBot.getLog().info("Loading " + value.getName() + " by " + value.getAuthor());
        });
    }

}
