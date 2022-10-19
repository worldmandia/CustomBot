package ua.mani123.consoleCommands;

import ua.mani123.CBot;

import java.util.List;
import java.util.Scanner;

public class consoleUtils implements Runnable{

    static boolean isStopped = false;

    @Override
    public void run() {
        do {
            Scanner ins = new Scanner(System.in);
            List<String> parts = List.of(ins.nextLine().split(" "));
            if (parts.get(0).equals("reload")) {
                reloadCommand.use(parts);
            } else if (parts.get(0).equals("stop")) {
                stopCommand.use(parts);
            } else {
                CBot.getLog().info("Command not found");
            }
        } while (!isStopped);
    }
}
