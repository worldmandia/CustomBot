package ua.mani123.discordModule;

import lombok.Getter;
import net.dv8tion.jda.api.exceptions.InvalidTokenException;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import ua.mani123.CustomBot;
import ua.mani123.EnableLogger;
import ua.mani123.config.ConfigUtils;
import ua.mani123.config.Objects.BotConfig;
import ua.mani123.config.Objects.DiscordConfigs;
import ua.mani123.config.Objects.Settings;
import ua.mani123.discordModule.actions.SEND_EMBED;
import ua.mani123.discordModule.filters.DISCORD_BOT;
import ua.mani123.discordModule.interaction.COMMAND_INTERACTION;
import ua.mani123.listeners.GenericListener;
import ua.mani123.listeners.ReadyListener;
import ua.mani123.listeners.SlashCommandInteractionListener;

import java.util.ArrayList;
import java.util.NoSuchElementException;

@Getter
public class DiscordUtils extends EnableLogger {

    BotConfig botConfig;
    DiscordConfigs discordConfigs;

    ArrayList<ShardManager> discordBots = new ArrayList<>();
    String defaultFolder;

    public DiscordUtils init(String defaultFolder) {
        this.defaultFolder = defaultFolder;
        return this;
    }

    public DiscordUtils enableBots() {
        botConfig = new ConfigUtils(defaultFolder + "/BotConfig.toml").loadAsFileConfig(new BotConfig(), false);
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
        discordConfigs = new ConfigUtils(defaultFolder + "/" + CustomBot.getSettings().getDefaultDiscordConfigFolder()).loadAsFolder(new DiscordConfigs());

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
                final ArrayList<String> denyActionsIds = commentedConfig.getOrElse("denyActionsIds", new ArrayList<>());
                final ArrayList<DiscordConfigs.Action> denyActions = new ArrayList<>();
                try {
                    denyActionsIds.forEach(denyActionsId -> discordConfigs.getActions().stream().filter(action -> denyActionsId.equals(action.getId())).findFirst().orElseThrow());
                } catch (NoSuchElementException ignored) {
                    logger.error(String.format(CustomBot.getLang().getErrorLoadActionInFilter(), id));
                }

                switch (type) {
                    case "DISCORD_BOT" -> discordConfigs.getFilters().add(new DISCORD_BOT(
                            type,
                            id,
                            commentedConfig.getOrElse("discordBotIds", new ArrayList<>()),
                            commentedConfig.getOrElse("whitelist", false),
                            denyActions
                    ));
                    default -> logger.error(String.format(CustomBot.getLang().getErrorLoadFilters(), id));
                }
            });
            logger.info(String.format(CustomBot.getLang().getLoadedFilters(), discordConfigs.getFilters().size()));
        }

        if (discordConfigs.getInteractionConfigs() != null) {
            discordConfigs.getInteractionConfigs().forEach(commentedConfig -> {
                final String type = commentedConfig.getOrElse("type", "").toUpperCase();
                final String id = commentedConfig.getOrElse("id", "not_set");
                final ArrayList<String> actionIds = commentedConfig.getOrElse("actionIds", new ArrayList<>());

                final ArrayList<DiscordConfigs.Order> orders = new ArrayList<>();
                final ArrayList<DiscordConfigs.Order> allOrders = new ArrayList<>();
                allOrders.addAll(discordConfigs.getActions());
                allOrders.addAll(discordConfigs.getFilters());
                try {
                    actionIds.forEach(actionId -> orders.add(allOrders.stream().filter(order -> order.getId().equals(actionId)).findFirst().orElseThrow()));
                } catch (NoSuchElementException ignored) {
                    logger.error(String.format(CustomBot.getLang().getErrorLoadActionInInteraction(), id));
                }

                switch (type) {
                    case "COMMAND_INTERACTION" -> discordConfigs.getInteractions().add(new COMMAND_INTERACTION(
                            type,
                            id,
                            commentedConfig.getOrElse("commandDescription", "not_set"),
                            orders,
                            commentedConfig.getOrElse("allowedGuilds", new ArrayList<>())
                    ));
                    default -> logger.error(String.format(CustomBot.getLang().getErrorLoadInteractions(), id));
                }
            });
            logger.info(String.format(CustomBot.getLang().getLoadedInteractions(), discordConfigs.getFilters().size()));
        }

        return this;
    }

    public DiscordUtils registerListeners(Settings settings) {
        if (settings.isRegisterReadyEvents()) {
            discordBots.forEach(shardManager -> shardManager.addEventListener(new ReadyListener()));
        }
        if (settings.isRegisterAllEvents()) {
            discordBots.forEach(shardManager -> shardManager.addEventListener(new GenericListener()));
        } else {
            if (settings.isRegisterSlashCommandEvents()) {
                discordBots.forEach(shardManager -> shardManager.addEventListener(new SlashCommandInteractionListener()));
            }
        }

        return this;
    }

}
