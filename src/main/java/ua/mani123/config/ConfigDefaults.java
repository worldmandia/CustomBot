package ua.mani123.config;

import com.electronwill.nightconfig.core.conversion.IgnoreValue;

public abstract class ConfigDefaults {

    @IgnoreValue
    protected ConfigUtils configUtils;

    public abstract void addDefaults();
    public abstract ConfigUtils getUtils();
    public abstract void setUtils(ConfigUtils configUtils);

}
