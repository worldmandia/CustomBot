package ua.mani123.consoleCommands;

import ua.mani123.EnableLogger;

import java.util.List;
import java.util.Scanner;

public class consoleUtils extends EnableLogger implements Runnable  {

  static boolean isStopped = false;

  @Override
  public void run() {
    do {
      Scanner ins = new Scanner(System.in);
      List<String> parts = List.of(ins.nextLine().split(" "));
      if (parts.get(0).equals("reload")) {
        new reloadCommand().run(parts);
      } else if (parts.get(0).equals("stop")) {
        new stopCommand().run(parts);
      } else {
        logger.info("Command not found");
      }
    } while (!isStopped);
  }
}
