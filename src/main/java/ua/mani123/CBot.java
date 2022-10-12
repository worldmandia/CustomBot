package ua.mani123;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import net.dv8tion.jda.api.JDA;
import ua.mani123.config.configUtils;
import ua.mani123.discord.discordUtils;

import java.util.Map;
import java.util.logging.Logger;

public class CBot {

    static Logger log = Logger.getGlobal();

    static CommentedFileConfig config;

    public static void main(String[] args) {
        log.info("Starting CustomBot");

        config = configUtils.initCfg("config.toml");
        Map<String, JDA> botsData = discordUtils.initBots(config);
        Map<String, CommentedFileConfig> actions = configUtils.initFolderCfg("actions/");
        Map<String, CommentedFileConfig> interactions = configUtils.initFolderCfg("interactions/");
    }

}
