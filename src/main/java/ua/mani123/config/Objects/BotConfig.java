package ua.mani123.config.Objects;

import com.electronwill.nightconfig.core.conversion.Path;
import com.electronwill.nightconfig.core.conversion.SpecNotNull;
import lombok.*;

import java.util.ArrayList;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class BotConfig extends ConfigWithDefaults {

    @Path("DiscordBot")
    @SpecNotNull
    ArrayList<DiscordBot> discordBots = new ArrayList<>();

    @Override
    public void addDefaults() {
        discordBots.add(new DiscordBot(0L, "TOKEN_CHANGE_IT_1"));
        discordBots.add(new DiscordBot(1L, "TOKEN_CHANGE_IT_2"));
        discordBots.add(new DiscordBot(2L, "TOKEN_CHANGE_IT_3"));
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @ToString
    public static class DiscordBot {
        Long botId;
        String botToken;
    }

}
