package ua.mani123.consoleCommands;

import java.util.List;
import net.dv8tion.jda.api.JDA;
import ua.mani123.CBot;
import ua.mani123.config.configUtils;

public class stopCommand {

  public static void use(List<String> parts) {
    if (parts.size() > 1) {
      if (parts.get(1).equals("bot")) {
        if (parts.size() > 2) {
          JDA jda = configUtils.getDiscordBotsData().get(parts.get(2));
          jda.shutdown();
        }
        return;
      } else if (parts.get(1).equals("all")) {
        configUtils.disableAll();
        consoleUtils.isStopped = true;
        System.exit(0);
        return;
      }
    }
    CBot.getLog().info("Usage: stop [bot, all]");
  }
}
