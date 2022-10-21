package ua.mani123.addon;

import com.electronwill.nightconfig.core.CommentedConfig;

public class Addon {
    String name;
    String main;
    String version;
    String author;
    AddonImpl addon;

    public Addon(CommentedConfig config, AddonImpl addon) {
        this.name = config.get("name");
        this.version = config.get("version");
        this.main = config.get("main");
        this.author = config.get("author");
        this.addon = addon;
    }

    public String getMain() {
        return main;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getAuthor() {
        return author;
    }

    public AddonImpl getAddon() {
        return addon;
    }
}
