package ua.mani123.config.Objects;

import com.electronwill.nightconfig.core.conversion.Path;
import com.electronwill.nightconfig.core.conversion.SpecNotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GlobalLang extends ConfigWithDefaults {
    @Path("Discord_Module_Init")
    @SpecNotNull
    String discordModuleInit = "Start init discord module";
    @Path("Filed_Load_File")
    @SpecNotNull
    String filedLoadFile = "Filed load file: %s";

    @Override
    public void addDefaults() {

    }
}
