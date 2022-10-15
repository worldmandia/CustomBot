package ua.mani123.discord;

import com.electronwill.nightconfig.core.CommentedConfig;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import ua.mani123.CBot;
import ua.mani123.config.CConfig;
import ua.mani123.discord.event.CommandAutoCompleteInteraction;
import ua.mani123.discord.event.SlashCommandInteraction;
import ua.mani123.discord.event.readyBot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class discordUtils {
    public static Map<String, JDA> initBots(CConfig cfg) {
        Map<String, JDA> bots = new HashMap<>();
        List<CommentedConfig> discordBotConfigs = cfg.getFileCfg().get("discord-bot");
        for (CommentedConfig config : discordBotConfigs) {
            String token = config.get("token");
            if (token != null) {
                try {
                    JDA jda = JDABuilder.createDefault(token).setContextEnabled(false).build();
                    String id = jda.getSelfUser().getId();
                    config.set("id", id);
                    jda.addEventListener(new readyBot());
                    if (config.getOrElse("enable-command-events", true)) {
                        jda.addEventListener(new SlashCommandInteraction());
                    }
                    if (config.getOrElse("enable-autocomplete-command-events", true)) {
                        jda.addEventListener(new CommandAutoCompleteInteraction());
                    }
                    bots.put(id, jda);
                } catch (Exception e) {
                    CBot.getLog().warn("You get error: " + e.getMessage());
                }
            }
        }
        return bots;
    }

}