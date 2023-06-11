package ua.mani123.config;

import com.electronwill.nightconfig.core.conversion.IgnoreValue;

public abstract class ConfigDefaults {

    @IgnoreValue
    protected ConfigUtils configUtils;

    public void addDefaults() {

    }

    public ConfigUtils getUtils() {
        return configUtils;
    }

    public void setUtils(ConfigUtils configUtils) {
        this.configUtils = configUtils;
    }

}
