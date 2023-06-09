package ua.mani123.config.Objects;

import com.electronwill.nightconfig.core.conversion.Path;
import com.electronwill.nightconfig.core.conversion.SpecNotNull;
import lombok.*;
import ua.mani123.config.ConfigDefaults;

import java.util.ArrayList;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class BotConfig extends ConfigDefaults {

    @Path("DiscordBot")
    @SpecNotNull
    ArrayList<DiscordBot> discordBots = new ArrayList<>();

    @Override
    public void addDefaults() {
        discordBots.add(new DiscordBot("0", "TOKEN_CHANGE_IT_1"));
        discordBots.add(new DiscordBot("1", "TOKEN_CHANGE_IT_2"));
        discordBots.add(new DiscordBot("2", "TOKEN_CHANGE_IT_3"));
    }

    @NoArgsConstructor
    @Getter
    @Setter
    @AllArgsConstructor
    public static class DiscordBot {
        @SpecNotNull
        @Path("BotId")
        String botId = "0";
        @SpecNotNull
        @Path("BotToken")
        String botToken = "NOT_SET";
    }
}
