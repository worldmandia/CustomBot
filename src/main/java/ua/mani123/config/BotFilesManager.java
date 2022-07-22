package ua.mani123.config;

import ua.mani123.DTBot;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;

public class BotFilesManager {

    public static void createFile(String name) {
        try {
            File file = new File(name);
            if (file.createNewFile()) {
                DTBot.getLOGGER().info("File created: " + name);
            } else DTBot.getLOGGER().info(name + " found");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void createResourceFile(String resourceName, String newName) {
        try {
            InputStream inputStream = BotFilesManager.class.getClassLoader().getResourceAsStream(resourceName);
            Path of = Path.of(newName);
            if (inputStream == null) {
                DTBot.getLOGGER().error("Not found file in resources: " + resourceName);
            } else if (Files.notExists(of, LinkOption.NOFOLLOW_LINKS)) {
                DTBot.getLOGGER().info("File created: " + newName);
                Files.copy(inputStream, of);
            } else {
                DTBot.getLOGGER().info(newName + " found");
            }
        } catch (IOException e) {
            DTBot.getLOGGER().info(e.getMessage() + " found");
        }
    }

    public static BufferedReader readFile(String fileName) {
        try {
            return new BufferedReader(new FileReader(fileName));
        } catch (FileNotFoundException e) {
            DTBot.getLOGGER().info(e.getMessage() + " Creating " + fileName);
            createFile(fileName);
            try {
                return new BufferedReader(new FileReader(fileName));
            } catch (FileNotFoundException ex) {
                DTBot.getLOGGER().error(e.getMessage() + "lol, how!?");
            }
        }
        // how you go to this return!?
        return readFile(fileName);
    }
}
