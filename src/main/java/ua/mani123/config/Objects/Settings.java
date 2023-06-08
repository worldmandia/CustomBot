package ua.mani123.config.Objects;

import com.electronwill.nightconfig.core.conversion.Path;
import com.electronwill.nightconfig.core.conversion.SpecNotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ua.mani123.config.ConfigUtils;

@Getter
@NoArgsConstructor
public class Settings extends ConfigDefaults {

    @Path("Enable_Discord_Bot_Module")
    @SpecNotNull
    boolean enableDiscordBotModule = true;
    @Path("Enable_Addons")
    @SpecNotNull
    boolean enableAddons = true;
    @Path("Configs_Folder")
    @SpecNotNull
    String defaultConfigFolder = "test";

    @Override
    public void addDefaults() {

    }

    @Override
    public ConfigUtils getUtils() {
        return this.configUtils;
    }

    @Override
    public void setUtils(ConfigUtils configUtils) {
        this.configUtils = configUtils;
    }
}
