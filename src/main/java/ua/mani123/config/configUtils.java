package ua.mani123.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.file.FileNotFoundAction;
import net.dv8tion.jda.api.JDA;
import ua.mani123.AddonUtils;
import ua.mani123.CBot;
import ua.mani123.discord.discordUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class configUtils {

    private static CConfig config;
    private static CConfig commandInteraction;
    private static CConfig buttonInteraction;
    private static Map<String, JDA> DiscordBotsData;
    private static Map<String, CConfig> actions;

    public static CommentedFileConfig initResourceCfg(String file, ClassLoader classLoader) {
        CommentedFileConfig config = CommentedFileConfig.builder(file).onFileNotFound(FileNotFoundAction.copyData(classLoader.getResource(file))).build();
        config.load();
        return config;
    }

    public static CommentedFileConfig initCfg(String file, String path) {
        File folder = new File(path);
        if (!folder.exists()) {
            if (folder.mkdirs()) {
                CommentedFileConfig config = CommentedFileConfig.builder(path + file).build();
                config.load();
                return config;
            }
        }
        return null;
    }


    public static Map<String, CConfig> initFolderCfg(String path) {
        Map<String, CConfig> configs = new HashMap<>();
        File folder = new File(path);
        if (folder.exists() && folder.isDirectory()) {
            for (File file : folder.listFiles()) {
                if (!file.getName().startsWith("_") && file.getName().endsWith(".toml")) {
                    CommentedFileConfig cfg = CommentedFileConfig.builder(file).autosave().build();
                    cfg.load();
                    if (cfg.get("type") != null) {
                        configs.put(file.getName().replace(".toml", ""), new CConfig(cfg));
                    } else {
                        cfg.close();
                    }
                }
            }
        } else {
            if (folder.mkdirs()) {
                initFolderCfg(path);
            }
        }
        return configs;
    }

    public static void init() {
        updateConfig();
        DiscordBotsData = discordUtils.initBots(getConfig());
        updateActions();
        updateCommandInteractions();
        updateButtonInteraction();
    }

    public static void updateConfig() {
        config = new CConfig(configUtils.initResourceCfg("config.toml",  CBot.class.getClassLoader()));
    }

    public static void updateButtonInteraction() {
        buttonInteraction = new CConfig(configUtils.initResourceCfg("interactions/buttonInteraction.toml", CBot.class.getClassLoader()));
    }

    public static void updateActions() {
        actions = configUtils.initFolderCfg("actions/");
    }

    public static void updateCommandInteractions() {
        commandInteraction = new CConfig(configUtils.initResourceCfg("interactions/commandInteraction.toml", CBot.class.getClassLoader()));
    }

    public static void saveAll() {
        AddonUtils.addonMap.forEach((key, value) -> value.getAddon().disable());
        config.getFileCfg().save();
        commandInteraction.getFileCfg().save();
        buttonInteraction.getFileCfg().save();
        for (Map.Entry<String, CConfig> cfg : actions.entrySet()) {
            cfg.getValue().getFileCfg().save();
        }
        CBot.getLog().info("Saving configs");
    }

    public static CConfig getConfig() {
        return config;
    }

    public static CConfig getCommandInteraction() {
        return commandInteraction;
    }

    public static CConfig getButtonInteraction() {
        return buttonInteraction;
    }

    public static Map<String, JDA> getDiscordBotsData() {
        return DiscordBotsData;
    }

    public static Map<String, CConfig> getActions() {
        return actions;
    }
}
