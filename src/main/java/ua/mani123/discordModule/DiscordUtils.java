package ua.mani123.discordModule;

import lombok.Getter;
import lombok.NoArgsConstructor;
import net.dv8tion.jda.api.exceptions.InvalidTokenException;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.mani123.CustomBot;
import ua.mani123.config.ConfigUtils;
import ua.mani123.config.Objects.BotConfig;
import ua.mani123.config.Objects.DiscordConfigs;
import ua.mani123.discordModule.actions.SEND_EMBED;
import ua.mani123.discordModule.filters.DISCORD_BOT;
import ua.mani123.discordModule.interaction.COMMAND_INTERACTION;

import java.util.ArrayList;

@Getter
@NoArgsConstructor
public class DiscordUtils {

    private final static Logger logger = LoggerFactory.getLogger(DiscordUtils.class);

    BotConfig botConfig;
    DiscordConfigs discordConfigs;

    ArrayList<ShardManager> discordBots = new ArrayList<>();

    public DiscordUtils init(String defaultFolder) {
        botConfig = new ConfigUtils(defaultFolder + "/BotConfig.toml").loadAsFileConfig(new BotConfig(), false);
        discordConfigs = new ConfigUtils(defaultFolder + "/" + CustomBot.getSettings().getDefaultDiscordConfigFolder()).loadAsFolder(new DiscordConfigs());
        return this;
    }

    public DiscordUtils enableBots() {
        botConfig.getDiscordBots().forEach(discordBot -> {
            try {
                ShardManager shardManager = DefaultShardManagerBuilder.createDefault(discordBot.getBotToken()).build();
                discordBots.add(shardManager);
                discordBot.setBotId(shardManager.getShards().get(0).getSelfUser().getId());
            } catch (InvalidTokenException ignored) {
                logger.error(String.format(CustomBot.getLang().getFailedLoadDiscordBot(), discordBot.getBotId(), discordBot.getBotToken()));
            }
        });
        botConfig.getUtils().updateConfig(botConfig, 0);
        logger.info(String.format(CustomBot.getLang().getEnabledDiscordBot(), discordBots.size()));
        return this;
    }

    public DiscordUtils loadDiscordActions() {

        if (discordConfigs.getActionConfigs() != null) {
            discordConfigs.getActionConfigs().forEach(commentedConfig -> {
                final String type = commentedConfig.getOrElse("type", "").toUpperCase();
                final String id = commentedConfig.getOrElse("id", "not_set");
                switch (type) {
                    case "SEND_EMBED" -> discordConfigs.getActions().add(new SEND_EMBED(
                            type,
                            id,
                            commentedConfig.getOrElse("url", ""),
                            commentedConfig.getOrElse("title", ""),
                            commentedConfig.getOrElse("description", ""),
                            commentedConfig.getOrElse("timestamp", ""),
                            commentedConfig.getOrElse("color", ""),
                            commentedConfig.getOrElse("thumbnail", ""),
                            commentedConfig.getOrElse("author", ""),
                            commentedConfig.getOrElse("footer", ""),
                            commentedConfig.getOrElse("image", ""),
                            new ArrayList<>(),
                            commentedConfig.getOrElse("reply", false),
                            commentedConfig.getOrElse("ephemeral", false)

                    ));
                    default -> logger.error(String.format(CustomBot.getLang().getErrorLoadActions(), id));
                }
            });
            logger.info(String.format(CustomBot.getLang().getLoadedActions(), discordConfigs.getActions().size()));
        }

        if (discordConfigs.getFilterConfigs() != null) {
            discordConfigs.getFilterConfigs().forEach(commentedConfig -> {
                final String type = commentedConfig.getOrElse("type", "").toUpperCase();
                final String id = commentedConfig.getOrElse("id", "not_set");
                switch (type) {
                    case "DISCORD_BOT" -> discordConfigs.getFilters().add(new DISCORD_BOT(
                            type,
                            id,
                            commentedConfig.getOrElse("discordBotIds", new ArrayList<>()),
                            commentedConfig.getOrElse("whitelist", false)
                    ));
                    default -> logger.error(String.format(CustomBot.getLang().getErrorLoadFilters(), id));
                }
            });
            logger.info(String.format(CustomBot.getLang().getLoadedFilters(), discordConfigs.getFilters().size()));
        }

        if (discordConfigs.getInteractions() != null) {
            discordConfigs.getInteractionConfigs().forEach(commentedConfig -> {
                final String type = commentedConfig.getOrElse("type", "").toUpperCase();
                final String id = commentedConfig.getOrElse("id", "not_set");
                switch (type) {
                    case "COMMAND_INTERACTION" -> discordConfigs.getInteractions().add(new COMMAND_INTERACTION(
                            type,
                            id
                    ));
                    default -> logger.error(String.format(CustomBot.getLang().getErrorLoadInteractions(), id));
                }
            });
            logger.info(String.format(CustomBot.getLang().getLoadedInteractions(), discordConfigs.getFilters().size()));
        }

        return this;
    }

}
