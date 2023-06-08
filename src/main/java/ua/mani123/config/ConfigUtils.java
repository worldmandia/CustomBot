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

@Getter
public class ConfigUtils {

    private final static Logger logger = LoggerFactory.getLogger(ConfigUtils.class);

    private final static ObjectConverter objectConverter = new ObjectConverter();
    private final static ConfigParser<CommentedConfig> tomlParser = TomlFormat.instance().createParser();
    private final static ConfigWriter tomlWriter = TomlFormat.instance().createWriter();
    private CommentedFileConfig commentedConfig;
    private final String filePath;

    public ConfigUtils(String filePath) {
        this.filePath = filePath;
    }

    public <T extends ConfigDefaults> T loadFileConfig(T fileObject, boolean mergeFile) {
        File file = new File(filePath);
        try {
            if (file.createNewFile()) {
                CommentedFileConfig commentedConfig = CommentedFileConfig.builder(file).charset(StandardCharsets.UTF_8).onFileNotFound(FileNotFoundAction.CREATE_EMPTY).build();
                fileObject.addDefaults();
                objectConverter.toConfig(fileObject, commentedConfig);
                commentedConfig.save();
                this.commentedConfig = commentedConfig;
            } else {
                commentedConfig = CommentedFileConfig.of(file);
                if (mergeFile) {
                    objectConverter.toConfig(fileObject, commentedConfig);
                }
                tomlParser.parse(file, commentedConfig, ParsingMode.MERGE, FileNotFoundAction.THROW_ERROR);
                commentedConfig.save();
                try {
                    objectConverter.toObject(commentedConfig, fileObject);
                } catch (InvalidValueException e) {
                    if (CustomBot.getLang() != null) {
                        logger.error(String.format(CustomBot.getLang().getFiledLoadFile(), file.getName()));
                    } else {
                        logger.error(String.format("Filed load file: %s", file.getName()));
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        fileObject.setUtils(this);
        return fileObject;
    }

    public <T extends ConfigDefaults> void updateConfig(T fileObject) {
        try {
            objectConverter.toConfig(fileObject, commentedConfig);
            commentedConfig.save();
        } catch (Exception e) {
            if (CustomBot.getLang() != null) {
                logger.error(String.format(CustomBot.getLang().getFiledLoadFile(), commentedConfig.getFile().getName()));
            } else {
                logger.error(String.format("Filed load file: %s", commentedConfig.getFile().getName()));
            }
        }
    }

    public <T extends ConfigDefaults> T loadFileConfig(String resourceFilePath, T fileObject, boolean mergeFile) {
        File file = new File(filePath);
        commentedConfig = CommentedFileConfig.of(file);
        if (mergeFile) {
            objectConverter.toConfig(fileObject, commentedConfig);
        }
        tomlParser.parse(file, commentedConfig, ParsingMode.MERGE, FileNotFoundAction.copyResource(resourceFilePath));
        try {
            objectConverter.toObject(commentedConfig, fileObject);
        } catch (InvalidValueException e) {
            if (CustomBot.getLang() != null) {
                logger.error(String.format(CustomBot.getLang().getFiledLoadFile(), file.getName()));
            } else {
                logger.error(String.format("Filed load file: %s", file.getName()));
            }
        }
        fileObject.setUtils(this);
        return fileObject;
    }

}
