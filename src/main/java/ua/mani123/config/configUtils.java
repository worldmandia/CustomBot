package ua.mani123.config;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.file.FileNotFoundAction;
import ua.mani123.CBot;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class configUtils {
    public static CommentedFileConfig initCfg(String filepath) {
        CommentedFileConfig config = CommentedFileConfig.builder(filepath).autosave().onFileNotFound(FileNotFoundAction.copyData(CBot.class.getClassLoader().getResourceAsStream(filepath))).build();
        config.load();
        return config;
    }

    public static Map<String, CommentedFileConfig> initFolderCfg(String path) {
        Map<String, CommentedFileConfig> configs = new HashMap<>();
        File folder = new File(path);
        if (folder.exists() && folder.isDirectory()) {
            for (File file : folder.listFiles()) {
                if (!file.getName().startsWith("_")) {
                    CommentedFileConfig cfg = CommentedFileConfig.builder(file).autosave().build();
                    cfg.load();
                    configs.put(file.getName(), cfg);
                }
            }
        } else {
            folder.mkdirs();
        }
        return configs;
    }

}
