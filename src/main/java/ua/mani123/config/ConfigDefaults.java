package ua.mani123.config;

public abstract class ConfigDefaults {

    protected ConfigUtils configUtils;

    public abstract void addDefaults();
    public abstract ConfigUtils getUtils();
    public abstract void setUtils(ConfigUtils configUtils);

}
