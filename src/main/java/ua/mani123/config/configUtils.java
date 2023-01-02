package ua.mani123.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.file.FileNotFoundAction;
import java.io.File;
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

  public static CommentedFileConfig initFile(String file, ClassLoader classLoader) {
    CommentedFileConfig config = CommentedFileConfig.builder(file).onFileNotFound(FileNotFoundAction.copyData(
        Objects.requireNonNull(classLoader.getResource(file)))).build();
    config.load();
    return config;
  }

  public static HashMap<String, CConfig> initResourcesFolder(String folder) {
    HashMap<String, CConfig> configs = new HashMap<>();
    File folderFile = new File(folder);
    if (folderFile.mkdirs()) {
      // Copy default cfgs
      //
      //try {
      //  URL url = classLoader.getResource(folderFile.getPath());
      //  File newFolderFile = new File(url.getPath());
      //  CBot.getLog().info(String.valueOf(newFolderFile.isDirectory()));
      //  CBot.getLog().info(newFolderFile.listFiles().toString());
      //  File[] listOfFiles = newFolderFile.listFiles();
      //  for (File file : listOfFiles) {
      //    if (file.isFile()) {
      //      InputStream is = classLoader.getResourceAsStream("/" + folder + file.getName());
      //      Files.copy(is, Paths.get(folder + file.getName()), StandardCopyOption.REPLACE_EXISTING);
      //    }
      //  }
      //} catch (Exception e) {
      //  CBot.getLog().warn(e.toString());
      //}
    }
    for (File file : Objects.requireNonNull(folderFile.listFiles())) {
      if (!file.getName().startsWith("_") && file.getName().endsWith(".toml")) {
        CommentedFileConfig cfg = CommentedFileConfig.builder(file).build();
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
    interactions = initResourcesFolder("interactions");
  }

  public static void updateActions() {
    actions = initResourcesFolder("interactions");
  }

  public static void disableAll() {
    AddonUtils.getAddonMap().forEach((key, value) -> value.getAddon().disable());
    // Save all
    config.getFileCfg().save();
    actions.forEach((s, cConfig) -> cConfig.getFileCfg().save());
    interactions.forEach((s, cConfig) -> cConfig.getFileCfg().save());
    CBot.getLog().info("Saving configs");
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
