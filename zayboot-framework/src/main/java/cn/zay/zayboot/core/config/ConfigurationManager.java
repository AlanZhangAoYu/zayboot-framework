package cn.zay.zayboot.core.config;

import cn.zay.zayboot.core.config.loader.PropertiesResourceLoader;
import cn.zay.zayboot.core.config.loader.ResourceLoader;
import cn.zay.zayboot.core.config.loader.YamlResourceLoader;
import cn.zay.zayboot.core.ioc.BeanFactory;
import lombok.extern.slf4j.Slf4j;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * @author ZAY
 * 配置管理器
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

    /**
     * 加载 resource目录下的配置文件, 生成的 Map放在 BEANS中, 形式为 文件名(key) -> Map(value)
     * @param resourcePaths resource目录下配置文件列表
     */
    @Override
    public void loadResources(List<Path> resourcePaths) {
        try {
            for (Path resourcePath : resourcePaths) {
                String fileName = resourcePath.getFileName().toString();
                if (fileName.endsWith(PROPERTIES_FILE_EXTENSION)) {
                    ResourceLoader resourceLoader = new PropertiesResourceLoader();
                    BeanFactory.BEANS.put(fileName,resourceLoader.loadResource(resourcePath));
                } else if (fileName.endsWith(YML_FILE_EXTENSION) || fileName.endsWith(YAML_FILE_EXTENSION)) {
                    ResourceLoader resourceLoader = new YamlResourceLoader();
                    BeanFactory.BEANS.put(fileName,resourceLoader.loadResource(resourcePath));
                }
            }
        } catch (Exception ex) {
            log.error("加载配置文件出错!",ex);
            System.exit(-1);
        }
    }
}
