package cn.zay.zayboot.server;

import lombok.extern.slf4j.Slf4j;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author ZAY
 * NIO 非阻塞 IO
 */
@Slf4j
public class NoiServer {
    private static final List<SocketChannel> channelList = new LinkedList<>();
    public static void main(String[] args) {
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(9090));
            //设置 serverSocketChannel为非阻塞
            serverSocketChannel.configureBlocking(false);
            /*
              配置一个多路复用器, 用来解决一旦客户端连接数过多, 但有效连接(实际在发送消息的连接)过少, 导致
              channelList无效遍历过多次, 性能低下的问题
             */
            //在Linux底层这个方法调用 EPOOL_CREATE函数创建 epool实例
            Selector selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            log.info("NIO服务器启动成功!");
            while (true){
                //阻塞等待需要处理的事件发生
                selector.select();
                //这里是核心!!! selectionKeys获取到的是真正有事件发生的连接的集合, 每次遍历这个集合, 可以的大大提高效率
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                for (SelectionKey selectionKey : selectionKeys) {
                    if(selectionKey.isAcceptable()){
                        //如果是客户端连接事件, 就进行连接获取和事件注册
                        ServerSocketChannel server = (ServerSocketChannel) selectionKey.channel();
                        SocketChannel socketChannel = server.accept();
                        socketChannel.configureBlocking(false);
                        //注册读取客户端发送的数据的读事件, 若需要还可以注册向客户端发送数据的写事件
                        socketChannel.register(selector,SelectionKey.OP_READ);
                        log.info("客户端连接成功!");
                    } else if(selectionKey.isReadable()){
                        //如果是客户端发送消息事件, 就进行读取和打印
                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                        ByteBuffer byteBuffer = ByteBuffer.allocate(128);
                        int len = socketChannel.read(byteBuffer);
                        if(len > 0){
                            log.info("接收到消息:"+ new String(byteBuffer.array()));
                        }else if(len == -1){
                            socketChannel.close();
                            log.info("客户端断开连接");
                        }
                    }
                    //从事件集合里删除本次处理的key, 防止下次selector重复处理
                    selectionKeys.remove(selectionKey);
                }
                /*SocketChannel socketChannel = serverSocketChannel.accept();
                //如果有客户端连接
                if(socketChannel != null){
                    log.info("连接成功!");
                    //设置socketChannel为非阻塞
                    socketChannel.configureBlocking(false);
                    channelList.add(socketChannel);
                }
                for (SocketChannel channel : channelList) {
                    ByteBuffer byteBuffer = ByteBuffer.allocate(128);
                    int len = channel.read(byteBuffer);
                    if(len > 0){
                        log.info("接收到消息:"+ new String(byteBuffer.array()));
                    }else if(len == -1){
                        channelList.remove(channel);
                        log.info("客户端断开连接");
                    }
                }*/
            }
        } catch (IOException e) {
            log.error("异常!",e);
        }
    }
}
