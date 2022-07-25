package ua.mani123.config;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.file.FileConfig;
import ua.mani123.interaction.InteractionType;
import ua.mani123.ticket.TicketType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BotConfig {

    protected FileConfig config;

    public BotConfig(String file) {
        this.config = FileConfig.of(file);
        config.load();
    }

    public String getString(String path, String defaultValue) {
        if (config.get(path) == null) return defaultValue;
        if (config.get(path).equals("")) return defaultValue;
        return config.getOrElse(path, defaultValue).toString();
    }

    public FileConfig get() {
        return config;
    }


    public List<CommentedConfig> getList(String path) {
        return config.get(path);
    }

    public Map<TicketType, Config> getTicketMap(String path) {
        List<CommentedConfig> list = config.get(path);
        Map<TicketType, Config> map = new HashMap<>();
        for (CommentedConfig ticket : list) {
            map.put(TicketType.valueOf(ticket.get("type").toString().toUpperCase()), ticket);
        }
        return map;
    }

    public Map<InteractionType, Config> getInteractionMap(String path) {
        List<CommentedConfig> list = config.get(path);
        Map<InteractionType, Config> map = new HashMap<>();
        for (CommentedConfig interaction : list) {
            map.put(InteractionType.valueOf(interaction.get("type").toString().toUpperCase()), interaction);
        }
        return map;
    }

    public String getString(String path) {
        return config.getOrElse(path, null).toString();
    }

    public void reload() {
        config.load();
    }

    public void save() {
        config.save();
    }
}
