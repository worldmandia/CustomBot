package ua.mani123.consoleCommands;

import ua.mani123.CustomBot;
import ua.mani123.EnableLogger;

import java.util.List;

public class reloadCommand extends EnableLogger {

  public void run(List<String> parts) {
    if (parts.size() > 1) {
      switch (parts.get(1)) {
        case "all" -> {
          CustomBot.disable();
          CustomBot.enable();
          logger.info("CustomBot reloaded");
          return;
        }
      }
    }
    logger.info("Usage: reload [all]");
  }
}
