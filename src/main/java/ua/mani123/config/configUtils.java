package ua.mani123.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.file.FileNotFoundAction;
import net.dv8tion.jda.api.JDA;
import ua.mani123.CBot;
import ua.mani123.discord.discordUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class configUtils {

    static CConfig config;
    static CConfig commandInteraction;
    static CConfig buttonInteraction;
    static Map<String, JDA> DiscordBotsData;
    static Map<String, CConfig> actions;

    public static CommentedFileConfig initCfg(String file, String path) {
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        CommentedFileConfig config = CommentedFileConfig.builder(path + file).autosave().onFileNotFound(FileNotFoundAction.copyData(CBot.class.getClassLoader().getResourceAsStream(path + file))).build();
        config.load();
        return config;
    }

    public static Map<String, CConfig> initFolderCfg(String path) {
        Map<String, CConfig> configs = new HashMap<>();
        File folder = new File(path);
        if (folder.exists() && folder.isDirectory()) {
            for (File file : folder.listFiles()) {
                if (!file.getName().startsWith("_")) {
                    CommentedFileConfig cfg = CommentedFileConfig.builder(file).autosave().build();
                    cfg.load();
                    configs.put(file.getName(), new CConfig(cfg));
                }
            }
        } else {
            folder.mkdirs();
        }
        return configs;
    }

    public static void init() {
        config = new CConfig(configUtils.initCfg("config.toml", ""));
        DiscordBotsData = discordUtils.initBots(config);
        actions = configUtils.initFolderCfg("actions/");
        commandInteraction = new CConfig(configUtils.initCfg("commandInteraction.toml", "interactions/"));
        buttonInteraction = new CConfig(configUtils.initCfg("buttonInteraction.toml", "interactions/"));
    }

    public static void saveAll() {
        CBot.getLog().info("Shutting down the bot, saving configs");
        config.getFileCfg().save();
        commandInteraction.getFileCfg().save();
        buttonInteraction.getFileCfg().save();
        for (Map.Entry<String, CConfig> cfg : actions.entrySet()) {
            cfg.getValue().getFileCfg().save();
        }
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
