package ua.mani123.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.file.FileNotFoundAction;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import net.dv8tion.jda.api.JDA;
import ua.mani123.CBot;
import ua.mani123.addon.AddonUtils;
import ua.mani123.discord.discordUtils;

public class configUtils {

  private static CConfig config;
  private static HashMap<String, CConfig> interactions;
  private static Map<String, JDA> DiscordBotsData;
  private static Map<String, CConfig> actions;
  private static final FileSystem fileSystem;

  static {
    try {
      fileSystem = FileSystems.newFileSystem(Objects.requireNonNull(CBot.class.getResource("")).toURI(), Map.<String, String>of());
    } catch (IOException | URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

  public static CommentedFileConfig initFile(String file, ClassLoader classLoader) {
    CommentedFileConfig config = CommentedFileConfig.builder(file).autosave().onFileNotFound(FileNotFoundAction.copyData(
        Objects.requireNonNull(classLoader.getResource(file)))).build();
    config.load();
    return config;
  }

  public static HashMap<String, CConfig> initResourcesFolder(String folder, FileSystem thisFileSystem) {
    File folderFile = new File(folder);
    if (folderFile.mkdirs()) {
      try {
        Path jarPath = thisFileSystem.getPath(folder);
        Files.walkFileTree(jarPath, new SimpleFileVisitor<>() {
          @Override
          public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            Path currentTarget = Path.of(folder).resolve(jarPath.relativize(dir).toString());
            Files.createDirectories(currentTarget);
            return FileVisitResult.CONTINUE;
          }

          @Override
          public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            Files.copy(file, Path.of(folder).resolve(jarPath.relativize(file).toString()), StandardCopyOption.REPLACE_EXISTING);
            return FileVisitResult.CONTINUE;
          }
        });
      } catch (Exception e) {
        CBot.getLog().warn(Arrays.toString(e.getStackTrace()));
      }
    }
    HashMap<String, CConfig> configs = new HashMap<>();
    for (File file : Objects.requireNonNull(folderFile.listFiles())) {
      if (!file.getName().startsWith("_") && file.getName().endsWith(".toml")) {
        CommentedFileConfig cfg = CommentedFileConfig.builder(file).autosave().build();
        cfg.load();
        configs.put(file.getName().replace(".toml", ""), new CConfig(cfg));
      }
    }
    return configs;
  }

  public static void init() {
    updateConfig();
    DiscordBotsData = discordUtils.initBots(getConfig());
    updateInteractions();
    updateActions();
  }

  public static void updateConfig() {
    config = new CConfig(configUtils.initFile("config.toml", CBot.class.getClassLoader()));
  }

  public static void updateInteractions() {
    interactions = initResourcesFolder("interactions", fileSystem);
  }

  public static void updateActions() {
    actions = initResourcesFolder("actions", fileSystem);
  }

  public static void disableAll() {
    CBot.getLog().info("Disable all");
    AddonUtils.getAddonMap().forEach((key, value) -> value.getAddon().disable());
    // Save all (Not need)
    //config.getFileCfg().save();
    //actions.forEach((s, cConfig) -> cConfig.getFileCfg().save());
    //interactions.forEach((s, cConfig) -> cConfig.getFileCfg().save());
    CBot.getLog().info("Offline!");
  }

  public static CConfig getConfig() {
    return config;
  }

  public static HashMap<String, CConfig> getInteractions() {
    return interactions;
  }

  public static Map<String, JDA> getDiscordBotsData() {
    return DiscordBotsData;
  }

  public static Map<String, CConfig> getActions() {
    return actions;
  }
}
