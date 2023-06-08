package ua.mani123.config.Objects;

import com.electronwill.nightconfig.core.conversion.Path;
import com.electronwill.nightconfig.core.conversion.SpecNotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class BotConfig extends ConfigWithDefaults {

    @Path("DiscordBot")
    @SpecNotNull
    List<DiscordBot> discordBots = new ArrayList<>();

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
        @SpecNotNull
        @Path("BotId")
        Long botId = 0L;
        @SpecNotNull
        @Path("BotToken")
        String botToken = "NOT_SET";
    }

}
