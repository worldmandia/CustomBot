package ua.mani123.config;

import com.electronwill.nightconfig.core.file.FileConfig;

import java.util.ArrayList;

public class BotConfig {
    protected FileConfig fileConfig;

    public BotConfig(String file) {
        fileConfig = FileConfig.of(file);
        fileConfig.load();
    }

    public String get(String path, ArrayList<String> placeholder, ArrayList<String> value){
        String s = fileConfig.get(path);
        for (int i = 0; i < placeholder.size(); i++) {
            s = s.replaceAll(placeholder.get(i), value.get(i));
        }
        return s;
    }

    public String get(String path){
        return fileConfig.get(path);
    }

    public String getOrString(String path, String defaultValue) {
        if (fileConfig.get(path) == null) return defaultValue;
        if (fileConfig.get(path).equals("")) return defaultValue;
        return fileConfig.getOrElse(path, defaultValue);
    }

    public FileConfig getFileConfig() {
        return fileConfig;
    }

    public void reload() {
        fileConfig.load();
    }

    public void save() {
        fileConfig.save();
        fileConfig.load();
    }
}
