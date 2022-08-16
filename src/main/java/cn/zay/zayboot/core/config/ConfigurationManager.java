package cn.zay.zayboot.core.config;

import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * @author ZAY
 */
@Slf4j
public class ConfigurationManager implements Configuration{
    private static final String PROPERTIES_FILE_EXTENSION = ".properties";
    private static final String YAML_FILE_EXTENSION = ".yaml";
    private static final String YML_FILE_EXTENSION = ".yml";

    @Override
    public int getInt(String id) {
        return 0;
    }

    @Override
    public String getString(String id) {
        return null;
    }

    @Override
    public Boolean getBoolean(String id) {
        return null;
    }

    @Override
    public void put(String id, String content) {
        Configuration.super.put(id, content);
    }

    @Override
    public void putAll(Map<String, String> maps) {
        Configuration.super.putAll(maps);
    }

    @Override
    public void loadResources(List<Path> resourcePaths) {
        Configuration.super.loadResources(resourcePaths);
    }
}
