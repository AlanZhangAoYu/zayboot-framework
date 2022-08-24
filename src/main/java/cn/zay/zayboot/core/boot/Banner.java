package cn.zay.zayboot.core.boot;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * ZAYBOOT 标题输出, 在 banner.txt中
 * @author ZAY
 */
@Slf4j
public class Banner {
    // banner在 https://www.bootschool.net/ascii 中制作
    public static final String CUSTOM_BANNER_NAME = "banner.txt";
    public static void print() {
        URL url = Thread.currentThread().getContextClassLoader().getResource(CUSTOM_BANNER_NAME);
        if (url != null) {
            try {
                Path path = Paths.get(url.toURI());
                Files.lines(path).forEach(System.out::println);
            } catch (URISyntaxException | IOException e) {
                log.error("异常:{}",e.getMessage());
            }
        } else {
            log.error("找不到banner.txt文件!");
        }
    }
}
