package cn.zay.demo.java.pojo;

import cn.zay.zayboot.annotation.config.Value;
import cn.zay.zayboot.annotation.ioc.Component;

@Component
public class Server {
    @Value("${server.port}")
    private int port;
    @Value("${server.application.url}")
    private String url;
    @Override
    public String toString() {
        return "Server{" +
                "port=" + port +
                ", url='" + url + '\'' +
                '}';
    }
}
