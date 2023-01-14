package cn.zay.zayboot.core.boot;

import cn.zay.zayboot.core.config.ConfigurationManager;
import cn.zay.zayboot.core.ioc.BeanFactory;
import cn.zay.zayboot.exception.IllegalParameterFormatException;
import cn.zay.zayboot.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * ZAYBOOT 标题输出, 在 banner.txt中
 * @author ZAY
 */
@Slf4j
public class Banner {
    /**
     * banner在 https://www.bootschool.net/ascii 中制作
     */
    public static final String CUSTOM_BANNER_NAME = "banner.txt";
    public static final String PATTERN_STR = "\\$\\{[a-zA-Z0-9.]+\\}";
    public static void print() throws Exception{
        //获取当前线程的 classpath的绝对路径的 url
        URL url = Thread.currentThread().getContextClassLoader().getResource(CUSTOM_BANNER_NAME);
        if (url != null) {
            Path path = Paths.get(url.toURI());
            Files.lines(path).forEach(str -> {
                List<String> list = StringUtil.simpleMatch(PATTERN_STR,str);
                for (String s : list) {
                    try {
                        String keyStr = StringUtil.injectionFormatToValuePath(s);
                        for (String configFile : ConfigurationManager.DEFAULT_CONFIG_FILENAMES) {
                            if (BeanFactory.BEANS.get(configFile) != null) {
                                str = str.replaceAll(PATTERN_STR, (String) BeanFactory.BEANS.get(keyStr));
                            }
                        }
                    } catch (IllegalParameterFormatException e) {
                        log.error("异常!!",e);
                    }
                }
                System.out.println(str);
            });
        } else {
            log.info("找不到banner.txt文件!");
        }
    }
}
