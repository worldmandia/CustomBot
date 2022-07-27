package ua.mani123.config;

import com.electronwill.nightconfig.core.file.FileConfig;
public class BotConfig {
    protected FileConfig fileConfig;

    public BotConfig(String file) {
        this.fileConfig = FileConfig.of(file);
        fileConfig.load();
    }

    public String getString(String path, String defaultValue) {
        if (fileConfig.get(path) == null || fileConfig.get(path).equals("")) return defaultValue;
        return fileConfig.getOrElse(path, defaultValue).toString();
    }

    public FileConfig getFileConfig() {
        return fileConfig;
    }

    public void reload() {
        fileConfig.load();
    }

    public void save() {
        fileConfig.save();
    }
}
