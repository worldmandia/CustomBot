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
import ua.mani123.config.Objects.ConfigWithDefaults;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Getter
public class ConfigUtils {

    private final static Logger logger = LoggerFactory.getLogger(ConfigUtils.class);

    private final static ObjectConverter objectConverter = new ObjectConverter();
    private final static ConfigParser<CommentedConfig> tomlParser = TomlFormat.instance().createParser();
    private final static ConfigWriter tomlWriter = TomlFormat.instance().createWriter();
    private CommentedConfig commentedConfig;

    public <T extends ConfigWithDefaults> T loadFileConfig(String filePath, T fileObject) {
        File file = new File(filePath);
        try {
            if (file.createNewFile()) {
                CommentedFileConfig commentedConfig = CommentedFileConfig.builder(file).charset(StandardCharsets.UTF_8).build();
                fileObject.addDefaults();
                objectConverter.toConfig(fileObject, commentedConfig);
                commentedConfig.save();
                this.commentedConfig = commentedConfig;
            } else {
                commentedConfig = CommentedConfig.inMemory();
                tomlParser.parse(file, commentedConfig, ParsingMode.ADD, FileNotFoundAction.THROW_ERROR);
                try {
                    objectConverter.toObject(commentedConfig, fileObject);
                } catch (InvalidValueException e) {
                    logger.error(String.format(CustomBot.getLang().getFiledLoadFile(), file.getName()));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fileObject;
    }

    public <T extends ConfigWithDefaults> T loadFileConfig(String filePath, String resourceFilePath, T fileObject) {
        File file = new File(filePath);
        commentedConfig = CommentedConfig.inMemory();
        tomlParser.parse(file, commentedConfig, ParsingMode.ADD, FileNotFoundAction.copyResource(resourceFilePath));
        try {
            objectConverter.toObject(commentedConfig, fileObject);
        } catch (InvalidValueException e) {
            logger.error(String.format(CustomBot.getLang().getFiledLoadFile(), file.getName()));
        }

        return fileObject;
    }

}
