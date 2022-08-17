package cn.zay.zayboot.core.config;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * @author ZAY
 */
public interface Configuration {
    String[] DEFAULT_CONFIG_FILENAMES = {"application.properties", "application.yaml","application.yml"};
    int getInt(String id);
    String getString(String id);
    Boolean getBoolean(String id);
    default void put(String id, String content) {}
    default void putAll(Map<String, String> maps) {}
    default void loadResources(List<Path> resourcePaths) {}
}
