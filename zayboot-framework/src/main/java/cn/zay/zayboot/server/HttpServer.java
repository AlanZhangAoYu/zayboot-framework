package cn.zay.zayboot.server;

import cn.zay.zayboot.core.config.ConfigurationManager;
import cn.zay.zayboot.core.ioc.BeanFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

/**
 * @author ZAY
 */
@Slf4j
public class HttpServer {
    private static int PORT;
    /**
     * 服务器初始化, 配置一些参数, 比如端口, url
     */
    private static void init(){
        for (String configFile : ConfigurationManager.DEFAULT_CONFIG_FILENAMES) {
            if (BeanFactory.BEANS.get(configFile) != null) {
                HashMap<String,String> configMap = (HashMap<String,String>)BeanFactory.BEANS.get(configFile);
                PORT = Integer.parseInt(configMap.get("server.port"));
            }
        }
    }
    public static void start() {
        init();
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    // TCP默认开启了 Nagle 算法，该算法的作用是尽可能的发送大数据快，减少网络传输。TCP_NODELAY 参数的作用就是控制是否启用 Nagle 算法。
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    // 是否开启 TCP 底层心跳机制
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    //表示系统用于临时存放已完成三次握手的请求的队列的最大长度,如果连接建立频繁，服务器处理创建新连接较慢，可以适当调大这个参数
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast("decoder", new HttpRequestDecoder())
                                    .addLast("encoder", new HttpResponseEncoder())
                                    .addLast("aggregator", new HttpObjectAggregator(512 * 1024))
                                    .addLast("handler", new HttpServerHandler());
                        }
                    });
            log.info("服务器在[{}]端口启动\n",PORT);
            Channel ch = b.bind(PORT).sync().channel();
            //log.info(SystemConstants.LOG_PORT_BANNER, PORT);
            ch.closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("异常!!", e);
        } finally {
            log.error("关闭 bossGroup 和 workerGroup");
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
