package ua.mani123.consoleCommands;

import ua.mani123.EnableLogger;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ConsoleUtils extends EnableLogger implements Runnable {

    static boolean isStopped = false;

    @Override
    public void run() {
        try {
            do {
                Scanner ins = new Scanner(System.in);
                List<String> parts = List.of(ins.nextLine().split(" "));
                if (parts.get(0).equals("reload")) {
                    new ReloadCommand().run(parts);
                } else if (parts.get(0).equals("stop")) {
                    new StopCommand().run(parts);
                } else {
                    logger.info("Command not found");
                }
            } while (!isStopped);
        } catch (NoSuchElementException ignored) {
            logger.warn("Dont use CTRL+C, use /stop all");
        }
    }
}
