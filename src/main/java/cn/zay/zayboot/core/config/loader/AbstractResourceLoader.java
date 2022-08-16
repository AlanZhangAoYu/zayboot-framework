package cn.zay.zayboot.core.config.loader;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

/**
 * @author ZAY
 */
public abstract class AbstractResourceLoader implements ResourceLoader{
    @Override
    public Map<String, String> loadResource(Path path) throws IOException {
        return loadResources(path);
    }
    protected abstract Map<String, String> loadResources(Path path) throws IOException;
}
