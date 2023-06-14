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
import ua.mani123.discordModule.actions.SEND_MESSAGE;
import ua.mani123.discordModule.additions.COMMAND_OPTION;
import ua.mani123.discordModule.filters.DISCORD_BOT;
import ua.mani123.discordModule.filters.ROLE;
import ua.mani123.discordModule.filters.USER;
import ua.mani123.discordModule.interaction.COMMAND_INTERACTION;
import ua.mani123.discordModule.listeners.GenericListener;
import ua.mani123.discordModule.listeners.ReadyListener;
import ua.mani123.discordModule.listeners.SlashCommandInteractionListener;

import java.util.ArrayList;

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

        if (discordConfigs.getAdditionConfigs() != null) {
            discordConfigs.getAdditionConfigs().forEach(commentedConfig -> {
                String type = commentedConfig.getOrElse("type", "").toUpperCase();
                String id = commentedConfig.getOrElse("id", "not_set");

                switch (type) {
                    case "COMMAND_OPTION" -> discordConfigs.getAdditions().add(new COMMAND_OPTION(
                            type,
                            id,
                            commentedConfig.getOrElse("choices", new ArrayList<>()),
                            commentedConfig.getOrElse("optionType", "STRING"),
                            commentedConfig.getOrElse("description", "not_set"),
                            commentedConfig.getOrElse("required", false),
                            commentedConfig.getOrElse("autoComplete", false)
                    ));
                }
            });
            logger.info(String.format(CustomBot.getLang().getLoadedAdditions(), discordConfigs.getAdditions().size()));
        }

        if (discordConfigs.getActionConfigs() != null) {
            discordConfigs.getActionConfigs().forEach(commentedConfig -> {
                String type = commentedConfig.getOrElse("type", "").toUpperCase();
                String id = commentedConfig.getOrElse("id", "not_set");
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
                    case "SEND_MESSAGE" -> discordConfigs.getActions().add(new SEND_MESSAGE(
                            type,
                            id,
                            commentedConfig.getOrElse("message", ""),
                            commentedConfig.getOrElse("reply", false),
                            commentedConfig.getOrElse("ephemeral", false)
                    ));
                    case "ROLE" -> discordConfigs.getActions().add(new ua.mani123.discordModule.actions.ROLE(
                            type,
                            id,
                            commentedConfig.getOrElse("rolesIds", new ArrayList<>()),
                            commentedConfig.getOrElse("remove", false),
                            commentedConfig.getOrElse("removeIfHave", false),
                            commentedConfig.getOrElse("useTempDataRoles", false),
                            commentedConfig.getOrElse("replace", false)
                    ));
                    default -> logger.error(String.format(CustomBot.getLang().getErrorLoadActions(), id));
                }
            });
            logger.info(String.format(CustomBot.getLang().getLoadedActions(), discordConfigs.getActions().size()));
        }

        ArrayList<DiscordConfigs.Order> allOrders = new ArrayList<>(discordConfigs.getActions());

        if (discordConfigs.getFilterConfigs() != null) {
            discordConfigs.getFilterConfigs().forEach(commentedConfig -> {
                String type = commentedConfig.getOrElse("type", "").toUpperCase();
                String id = commentedConfig.getOrElse("id", "not_set");
                ArrayList<String> denyActionsIds = commentedConfig.getOrElse("denyActionsIds", new ArrayList<>());

                switch (type) {
                    case "DISCORD_BOT" -> discordConfigs.getFilters().add(new DISCORD_BOT(
                            type,
                            id,
                            commentedConfig.getOrElse("discordBotIds", new ArrayList<>()),
                            commentedConfig.getOrElse("whitelist", false),
                            getOrdersByIds(denyActionsIds, allOrders)
                    ));
                    case "ROLE" -> discordConfigs.getFilters().add(new ROLE(
                            type,
                            id,
                            commentedConfig.getOrElse("roles", new ArrayList<>()),
                            commentedConfig.getOrElse("whitelist", false),
                            commentedConfig.getOrElse("containsALL", false),
                            getOrdersByIds(denyActionsIds, allOrders)
                    ));
                    case "USER" -> discordConfigs.getFilters().add(new USER(
                            type,
                            id,
                            commentedConfig.getOrElse("users", new ArrayList<>()),
                            commentedConfig.getOrElse("whitelist", false),
                            getOrdersByIds(denyActionsIds, allOrders)
                    ));
                    default -> logger.error(String.format(CustomBot.getLang().getErrorLoadFilters(), id));
                }
            });
            logger.info(String.format(CustomBot.getLang().getLoadedFilters(), discordConfigs.getFilters().size()));
        }

        allOrders.addAll(discordConfigs.getFilters());

        if (discordConfigs.getInteractionConfigs() != null) {
            discordConfigs.getInteractionConfigs().forEach(commentedConfig -> {
                String type = commentedConfig.getOrElse("type", "").toUpperCase();
                String id = commentedConfig.getOrElse("id", "not_set");
                ArrayList<String> actionIds = commentedConfig.getOrElse("actionIds", new ArrayList<>());
                ArrayList<String> additionIds = commentedConfig.getOrElse("additionIds", new ArrayList<>());

                switch (type) {
                    case "COMMAND_INTERACTION" -> discordConfigs.getInteractions().add(new COMMAND_INTERACTION(
                            type,
                            id,
                            commentedConfig.getOrElse("commandDescription", "not_set"),
                            getOrdersByIds(actionIds, allOrders),
                            getAdditionsByIds(additionIds, discordConfigs.getAdditions()),
                            commentedConfig.getOrElse("allowedGuilds", new ArrayList<>()),
                            commentedConfig.getOrElse("allowedBots", new ArrayList<>()),
                            commentedConfig.getOrElse("guildOnly", true),
                            commentedConfig.getOrElse("nsfw", false)
                    ));
                    default -> logger.error(String.format(CustomBot.getLang().getErrorLoadInteractions(), id));
                }
            });
            logger.info(String.format(CustomBot.getLang().getLoadedInteractions(), discordConfigs.getInteractions().size()));
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

    private ArrayList<DiscordConfigs.Order> getOrdersByIds(ArrayList<String> ids, ArrayList<DiscordConfigs.Order> allOrders) {
        ArrayList<DiscordConfigs.Order> filteredOrders = new ArrayList<>();

        for (String id : ids) {
            DiscordConfigs.Order resultOrder = null;
            for (DiscordConfigs.Order order : allOrders) {
                if (order.getId().equals(id)) resultOrder = order;
            }
            if (resultOrder != null) filteredOrders.add(resultOrder);
            else logger.error(String.format(CustomBot.getLang().getErrorLoadActionInInteraction(), id));
        }

        return filteredOrders;
    }

    private ArrayList<DiscordConfigs.Addition> getAdditionsByIds(ArrayList<String> ids, ArrayList<DiscordConfigs.Addition> allAdditions) {
        ArrayList<DiscordConfigs.Addition> filteredAdditions = new ArrayList<>();

        for (String id : ids) {
            DiscordConfigs.Addition resultAddition = null;
            for (DiscordConfigs.Addition addition : allAdditions) {
                if (addition.getId().equals(id)) resultAddition = addition;
            }
            if (resultAddition != null) filteredAdditions.add(resultAddition);
            else logger.error(String.format(CustomBot.getLang().getErrorLoadActionInInteraction(), id));
        }

        return filteredAdditions;
    }

}
