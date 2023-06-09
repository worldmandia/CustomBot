package ua.mani123.config.Objects;

import com.electronwill.nightconfig.core.conversion.Path;
import com.electronwill.nightconfig.core.conversion.SpecNotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.mani123.config.ConfigDefaults;

@Getter
@Setter
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
    @Path("Actions_Folder")
    @SpecNotNull
    String defaultActionsFolder = "actions";
}
