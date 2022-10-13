package ua.mani123.discord;

import com.electronwill.nightconfig.core.CommentedConfig;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.exceptions.InvalidTokenException;
import ua.mani123.CBot;
import ua.mani123.config.CConfig;
import ua.mani123.discord.event.readyBot;
import ua.mani123.discord.event.SlashCommandInteraction;

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
                    bots.put(id, jda);
                } catch (InvalidTokenException e) {
                    CBot.getLog().warn("You get error: " + e.getMessage());
                }
            }
        }
        return bots;
    }

}