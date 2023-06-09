package ua.mani123.config;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.conversion.InvalidValueException;
import com.electronwill.nightconfig.core.conversion.ObjectConverter;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.file.FileNotFoundAction;
import com.electronwill.nightconfig.core.io.ConfigParser;
import com.electronwill.nightconfig.core.io.ConfigWriter;
import com.electronwill.nightconfig.core.io.ParsingMode;
import com.electronwill.nightconfig.toml.TomlFormat;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.mani123.CustomBot;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

@Getter
public class ConfigUtils {

    private final static Logger logger = LoggerFactory.getLogger(ConfigUtils.class);

    private final static ObjectConverter objectConverter = new ObjectConverter();
    private final static ConfigParser<CommentedConfig> tomlParser = TomlFormat.instance().createParser();
    private final static ConfigWriter tomlWriter = TomlFormat.instance().createWriter();
    private final ArrayList<CommentedFileConfig> commentedConfigs = new ArrayList<>();
    private final Path path;

    public ConfigUtils(String path) {
        this.path = Paths.get(path);
    }

    public <T extends ConfigDefaults> T loadAsFileConfig(T fileObject, boolean mergeFile) {
        fileObject.setUtils(this);
        try {
            File file = new File(path.toUri());
            if (file.createNewFile()) {
                CommentedFileConfig commentedConfig = CommentedFileConfig.builder(file).charset(StandardCharsets.UTF_8).onFileNotFound(FileNotFoundAction.CREATE_EMPTY).build();
                fileObject.addDefaults();
                objectConverter.toConfig(fileObject, commentedConfig);
                commentedConfig.save();
                this.commentedConfigs.add(commentedConfig);
            } else {
                CommentedFileConfig commentedFileConfig = CommentedFileConfig.of(file);
                commentedConfigs.add(commentedFileConfig);
                if (mergeFile) {
                    objectConverter.toConfig(fileObject, commentedFileConfig);
                }
                tomlParser.parse(file, commentedFileConfig, ParsingMode.MERGE, FileNotFoundAction.THROW_ERROR);
                commentedFileConfig.save();
                convertToObject(fileObject, commentedFileConfig);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fileObject;
    }

    public <T extends ConfigDefaults> T loadAsFolder(T fileObject) {
        fileObject.setUtils(this);

        Path directory = Paths.get(path.toUri());
        AtomicInteger counter = new AtomicInteger(0);
        try {
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }
            try (Stream<Path> pathStream = Files.walk(directory)) {
                pathStream.filter(Files::isRegularFile)
                        .filter(path -> path.toString().toLowerCase().endsWith(".toml"))
                        .forEach(file -> {
                            counter.addAndGet(1);
                            CommentedFileConfig config = CommentedFileConfig.of(file);
                            config.load();
                            commentedConfigs.add(config);
                        });
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        logger.info(String.format(CustomBot.getLang().getLoadedFilesFromDirectory(), counter.get(), path));

        return fileObject;
    }

    public <T extends ConfigDefaults> void updateConfig(T fileObject, int configId) {
        try {
            objectConverter.toConfig(fileObject, commentedConfigs.get(configId));
            commentedConfigs.get(configId).save();
        } catch (Exception e) {
            if (CustomBot.getLang() != null) {
                logger.error(String.format(CustomBot.getLang().getFailedLoadFile(), commentedConfigs.get(configId).getFile().getName()));
            } else {
                logger.error(String.format("Filed load file: %s", commentedConfigs.get(configId).getFile().getName()));
            }
        }
    }

    public <T extends ConfigDefaults> T loadAsFileConfig(String resourceFilePath, T fileObject, boolean mergeFile) {
        fileObject.setUtils(this);
        File file = new File(path.toUri());
        CommentedFileConfig commentedFileConfig = CommentedFileConfig.of(file);
        commentedConfigs.add(commentedFileConfig);
        if (mergeFile) {
            objectConverter.toConfig(fileObject, commentedFileConfig);
        }
        tomlParser.parse(file, commentedFileConfig, ParsingMode.MERGE, FileNotFoundAction.copyResource(resourceFilePath));
        convertToObject(fileObject, commentedFileConfig);
        return fileObject;
    }

    private <T extends ConfigDefaults> void convertToObject(T fileObject, CommentedFileConfig commentedFileConfig) {
        try {
            objectConverter.toObject(commentedFileConfig, fileObject);
        } catch (InvalidValueException e) {
            if (CustomBot.getLang() != null) {
                logger.error(String.format(CustomBot.getLang().getFailedLoadFile(), commentedFileConfig.getFile().getName()));
            } else {
                logger.error(String.format("Filed load file: %s", commentedFileConfig.getFile().getName()));
            }
        }
    }

}
