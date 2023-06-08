package ua.mani123.config.Objects;

import ua.mani123.config.ConfigUtils;

public abstract class ConfigDefaults {

    ConfigUtils configUtils;

    public abstract void addDefaults();
    public abstract ConfigUtils getUtils();
    public abstract void setUtils(ConfigUtils configUtils);

}
