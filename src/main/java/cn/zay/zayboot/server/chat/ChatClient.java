package cn.zay.zayboot.server.chat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

/**
 * @author ZAY
 * 简易聊天室客户端
 */
@Slf4j
public class ChatClient {
    public static void main(String[] args) {
        EventLoopGroup eventExecutors = new NioEventLoopGroup();
        try {
            //创建bootstrap对象，配置参数
            Bootstrap bootstrap = new Bootstrap();
            //设置线程组
            bootstrap.group(eventExecutors)
                    //设置客户端的通道实现类型
                    .channel(NioSocketChannel.class)
                    //使用匿名内部类初始化通道
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast("decoder",new StringDecoder());
                            pipeline.addLast("encoder",new StringEncoder());
                            //添加客户端通道的处理器
                            ch.pipeline().addLast(new ChatClientHandler());
                        }
                    });
            //连接服务端
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8080).sync();
            //得到服务端的 Channel
            Channel channel = channelFuture.channel();
            System.out.println("==========="+channel.localAddress()+"==========");
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()){
                String msg = scanner.nextLine();
                //通过 channel将数据发送到服务器端
                channel.writeAndFlush(msg);
            }
        }catch (Exception e){
            log.error("异常!",e);
        }finally {
            //关闭线程组
            eventExecutors.shutdownGracefully();
        }
    }
}
