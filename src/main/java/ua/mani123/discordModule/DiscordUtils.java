package ua.mani123.discordModule;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.mani123.config.ConfigUtils;
import ua.mani123.config.Objects.BotConfig;

@Getter
@NoArgsConstructor
public class DiscordUtils {

    private final static Logger logger = LoggerFactory.getLogger(DiscordUtils.class);

    BotConfig botConfig;
    BotConfig botConfigResource;

    public DiscordUtils init(String defaultFolder) {
        botConfig = new ConfigUtils().loadFileConfig(defaultFolder + "/BotConfig.toml", new BotConfig());
        return this;
    }

}
