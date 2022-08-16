package cn.zay.zayboot.core.config.loader;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

/**
 * @author ZAY
 */
public interface ResourceLoader {
    Map<String, String> loadResource(Path path) throws IOException;
}
