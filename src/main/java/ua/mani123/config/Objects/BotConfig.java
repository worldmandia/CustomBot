package ua.mani123.config.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@NoArgsConstructor
@Getter
@Setter
public class BotConfig extends ConfigWithDefaults {


    ArrayList<DiscordBot> discordBot = new ArrayList<>();

    @Override
    public void addDefaults() {
        discordBot.add(new DiscordBot(0L, "TEST_CHANGE_IT"));
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class DiscordBot {
        Long botId;
        String botToken;
    }

}
