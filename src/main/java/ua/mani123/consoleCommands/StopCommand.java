package ua.mani123.consoleCommands;

import ua.mani123.EnableLogger;

import java.util.List;

public class StopCommand extends EnableLogger {

    public void run(List<String> parts) {
        if (parts.size() > 1) {
            if (parts.get(1).equals("all")) {
                ConsoleUtils.isStopped = true;
                System.exit(0);
                return;
            }
        }
        logger.info("Usage: stop [all]");
    }
}
