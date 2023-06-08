package ua.mani123.discordModule;

import lombok.Getter;
import lombok.NoArgsConstructor;
import ua.mani123.config.ConfigUtils;
import ua.mani123.config.Objects.BotConfig;

@Getter
@NoArgsConstructor
public class DiscordUtils {

    BotConfig botConfig;
    BotConfig botConfigResource;

    public DiscordUtils init(String defaultFolder) {
        botConfig = new ConfigUtils().loadFileConfig(defaultFolder + "/botConfig.toml", new BotConfig());
        botConfigResource = new ConfigUtils().loadFileConfig(defaultFolder + "/botConfig.toml", "botConfig.toml", new BotConfig());
        botConfigResource.getDiscordBots().forEach(discordBot -> System.out.println(discordBot.toString()));
        return this;
    }

}
