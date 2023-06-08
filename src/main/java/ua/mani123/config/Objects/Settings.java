package ua.mani123.config.Objects;

import com.electronwill.nightconfig.core.conversion.Path;
import com.electronwill.nightconfig.core.conversion.SpecNotNull;
import lombok.Getter;

@Getter
public class Settings extends ConfigWithDefaults {

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
}
