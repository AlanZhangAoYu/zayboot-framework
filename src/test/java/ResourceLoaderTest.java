import cn.zay.zayboot.core.config.ConfigurationManager;
import cn.zay.zayboot.core.config.loader.PropertiesResourceLoader;
import cn.zay.zayboot.core.config.loader.YamlResourceLoader;
import cn.zay.zayboot.core.ioc.BeanFactory;
import cn.zay.zayboot.exception.NoSuchBeanDefinitionException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class ResourceLoaderTest {
    @Test
    public void propertiesLoaderTest(){
        try {
            Path path = Paths.get("E:\\MyWork\\zayboot-framework","src\\main\\resources\\simplelogger.properties");
            PropertiesResourceLoader propertiesResourceLoader=new PropertiesResourceLoader();
            Map<String, String> map = propertiesResourceLoader.loadResource(path);
            log.debug(map.toString());
            log.debug(map.get("org.slf4j.simpleLogger.defaultLogLevel"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void yamlLoaderTest(){
        try {
            Path path = Paths.get("E:\\MyWork\\zayboot-framework\\src\\main\\java\\cn\\zay\\demo\\resources\\application.yml");
            YamlResourceLoader yamlResourceLoader=new YamlResourceLoader();
            Map<String, String> map = yamlResourceLoader.loadResource(path);
            log.debug(map.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void configurationTest(){
        BeanFactory.loadClass(new String[]{"cn.zay.demo.java.pojo"});
        BeanFactory.loadBeans();
        ConfigurationManager configurationManager =new ConfigurationManager();
        List<Path> pathList=new ArrayList<>();
        pathList.add(Paths.get("E:\\MyWork\\zayboot-framework\\src\\main\\java\\cn\\zay\\demo\\resources\\application.yml"));
        pathList.add(Paths.get("E:\\MyWork\\zayboot-framework\\src\\main\\resources\\simplelogger.properties"));
        configurationManager.loadResources(pathList);
        log.debug(BeanFactory.BEANS.toString());
        try {
            log.debug(BeanFactory.getBean("application.yml").toString());
        } catch (NoSuchBeanDefinitionException e) {
            e.printStackTrace();
        }
    }
}
