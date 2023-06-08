package ua.mani123.config.Objects;

import com.electronwill.nightconfig.core.conversion.Path;
import com.electronwill.nightconfig.core.conversion.SpecNotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ua.mani123.config.ConfigUtils;

@Getter
@NoArgsConstructor
public class GlobalLang extends ConfigDefaults {
    @Path("Discord_Module_Init")
    @SpecNotNull
    String discordModuleInit = "Start init discord module";
    @Path("Filed_Load_File")
    @SpecNotNull
    String filedLoadFile = "Filed load file: %s";
    @Path("Enabled_Discord_Bots")
    @SpecNotNull
    String enabledDiscordBot = "Enabled discord bots: %s";
    @Path("Filed_Load_Discord_Bot")
    @SpecNotNull
    String filedLoadDiscordBot = "Filed load discord bot %s with token %s";

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
