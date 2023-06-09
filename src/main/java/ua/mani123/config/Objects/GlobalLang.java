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
public class GlobalLang extends ConfigDefaults {
    @Path("Discord_Module_Init")
    @SpecNotNull
    String discordModuleInit = "Start init discord module";
    @Path("Failed_Load_File")
    @SpecNotNull
    String failedLoadFile = "Failed load file: %s";
    @Path("Enabled_Discord_Bots")
    @SpecNotNull
    String enabledDiscordBot = "Enabled discord bots: %s";
    @Path("Failed_Load_Discord_Bot")
    @SpecNotNull
    String failedLoadDiscordBot = "Failed load discord bot %s with token %s";
    @Path("CustomBotDisabled")
    @SpecNotNull
    String customBotDisabled = "CustomBot disabled";
    @Path("LoadedFilesFromDirectory")
    @SpecNotNull
    String loadedFilesFromDirectory = "Loaded %s files from %s";
    @Path("LoadedActions")
    @SpecNotNull
    String loadedActions = "Loaded %s actions";
    @Path("LoadedFilters")
    @SpecNotNull
    String loadedFilters = "Loaded %s filters";

    @Path("ErrorLoadActions")
    @SpecNotNull
    String errorLoadActions = "Failed load action with id: %s";
    @Path("ErrorLoadFilters")
    @SpecNotNull
    String errorLoadFilters = "Failed load filter with id: %s";
}
