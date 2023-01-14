package cn.zay.zayboot.core.config.loader;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

/**
 * @author ZAY
 */
public interface ResourceLoader {
    /**
     * 加载配置文件(.properties或.yaml或.yml文件)
     * @param path 配置文件的路径
     * @return 配置文件中键值对(key -> value)的集合
     * @throws IOException 文件读取失败的异常
     */
    Map<String, String> loadResource(Path path) throws IOException;
}
