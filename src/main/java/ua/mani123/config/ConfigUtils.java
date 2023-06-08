package ua.mani123.config;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.conversion.ObjectConverter;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import lombok.Getter;
import ua.mani123.config.Objects.ConfigWithDefaults;

import java.io.File;
import java.io.IOException;

@Getter
public class ConfigUtils {

    ObjectConverter objectConverter = new ObjectConverter();

    public <T extends ConfigWithDefaults> T loadFileConfig(String configPath, T configObject) {
        File file = new File(configPath);
        try {
            if (file.createNewFile()) {
                CommentedFileConfig commentedConfig = CommentedFileConfig.builder(file).build();
                configObject.addDefaults();
                objectConverter.toConfig(configObject, commentedConfig);
                commentedConfig.save();
            } else {
                CommentedFileConfig commentedConfig = CommentedFileConfig.builder(file).build();
                objectConverter.toObject(commentedConfig, configObject);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return configObject;
    }

    public <T extends ConfigWithDefaults> T loadFileConfig(String configPath, String resourceFileName, T configObject) {
        CommentedConfig commentedConfig = CommentedFileConfig.builder(new File(configPath)).defaultResource(resourceFileName).autosave().build();
        objectConverter.toObject(commentedConfig, configObject);

        return configObject;
    }

}
