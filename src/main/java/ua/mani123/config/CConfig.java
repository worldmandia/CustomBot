package ua.mani123.config;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import java.util.List;

public class CConfig {

  private final CommentedFileConfig commentedFileConfig;

  public CConfig(CommentedFileConfig commentedFileConfig) {
    this.commentedFileConfig = commentedFileConfig;
  }

  public CommentedFileConfig getFileCfg() {
    return commentedFileConfig;
  }

  public List<CommentedConfig> getList(String path) {
    return commentedFileConfig.get(path);
  }
}
