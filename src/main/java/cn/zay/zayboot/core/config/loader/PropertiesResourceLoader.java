package cn.zay.zayboot.core.config.loader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author ZAY
 * .proerties文件加载读取
 */
public class PropertiesResourceLoader extends AbstractResourceLoader{
    /**
     * 例子:
     * 读取 simplelogger.properties
     * 输出 {org.slf4j.simpleLogger.showDateTime=true,
     *     org.slf4j.simpleLogger.showLogName=true,
     *     org.slf4j.simpleLogger.dateTimeFormat=yyyy-MM-dd HH:mm:ss,
     *     org.slf4j.simpleLogger.defaultLogLevel=DEBUG}
     */
    @Override
    protected Map<String, String> loadResources(Path path) throws IOException {
        //参考: https://blog.csdn.net/cece_2012/article/details/7522964
        Properties properties = new Properties();
        try (InputStream stream = Files.newInputStream(path); Reader reader = new InputStreamReader(stream)) {
            properties.load(reader);
        }
        Map<String, String> resource = new HashMap<>(properties.size());
        /*
          参考: https://www.jianshu.com/p/52f8ad17d54a
          例子:
          .properties文件中:
          Book=500
          Mobile=5000
          Pen=10
          Clothes=400
          则properties.entrySet()生成的 Set为:
          [Book=500, Mobile=5000, Pen=10, Clothes=400]
         */
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            resource.put(entry.getKey().toString(), entry.getValue().toString());
        }
        return resource;
    }
}
