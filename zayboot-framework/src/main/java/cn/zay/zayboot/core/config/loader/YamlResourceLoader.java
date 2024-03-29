package cn.zay.zayboot.core.config.loader;

import org.yaml.snakeyaml.Yaml;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 参考 https://www.cnblogs.com/xiaoqi/p/SnakeYAML.html
 * @author ZAY
 * .yml 或 .yaml 文件加载读取
 */
public class YamlResourceLoader extends AbstractResourceLoader{
    /**
     * 读取 path路径下的 .yml或 .yaml文件, 生成类似于:
     * {server.port=8080, server.application.name=ZAYdemo,
     *  server.application.url=http://zaydemo.com, student.name=张傲宇, student.age=21}
     * 这样的 Map
     */
    @Override
    protected Map<String, String> loadResources(Path path) throws IOException {
        Map<String, String> result = new LinkedHashMap<>();
        InputStream stream = null;
        Reader reader = null;
        try {
            Yaml yaml = new Yaml();
            stream = Files.newInputStream(path);
            reader = new InputStreamReader(stream);
            Map<String, Object> map = asMap(yaml.load(reader));
            buildFlattenedMap(result, map, null);
        } finally {
            if (stream != null) {
                stream.close();
            }
            if (reader != null) {
                reader.close();
            }
        }
        return result;
    }

    private void buildFlattenedMap(Map<String, String> result, Map<String, Object> source, String path) {
        source.forEach((key, value) -> {
            if (path != null && !path.isEmpty()) {
                key = path + '.' + key;
            }
            if (value instanceof String) {
                result.put(key, String.valueOf(value));
            } else if (value instanceof Map) {
                Map<String, Object> map = (Map<String, Object>) value;
                buildFlattenedMap(result, map, key);
            } else {
                result.put(key, (value != null ? String.valueOf(value) : ""));
            }
        });
    }

    private Map<String, Object> asMap(Object object) {
        Map<String, Object> result = new LinkedHashMap<>();
        Map<Object, Object> map = (Map<Object, Object>) object;
        map.forEach((key, value) -> {
            if (value instanceof Map) {
                value = asMap(value);
            }
            result.put(key.toString(), value);
        });
        return result;
    }
}
