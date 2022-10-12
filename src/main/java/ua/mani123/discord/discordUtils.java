package ua.mani123.discord;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import ua.mani123.discord.event.SlashCommandInteraction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class discordUtils {
    public static Map<String, JDA> initBots(CommentedFileConfig cfg) {
        Map<String, JDA> bots = new HashMap<>();
        List<CommentedConfig> discordBotConfigs = cfg.get("discord-bot");
        for (CommentedConfig config : discordBotConfigs) {
            String token = config.get("token");
            if (token != null) {
                JDA jda = JDABuilder.createDefault(token).build();
                String id = jda.getSelfUser().getId();
                config.set("id", id);
                bots.put(id, jda);
                if (config.getOrElse("enable-command-events", true)){
                    jda.addEventListener(new SlashCommandInteraction());
                }
            }
        }
        return bots;
    }

}