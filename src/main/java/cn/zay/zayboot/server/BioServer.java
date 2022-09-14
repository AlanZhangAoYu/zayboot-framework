package cn.zay.zayboot.server;

import lombok.extern.slf4j.Slf4j;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author ZAY
 * BIO (Blocking I/O)同步阻塞
 */
@Slf4j
public class BioServer {
    private static final int CORE_POOL_SIZE = 5;
    private static final int MAX_POOL_SIZE = 10;
    private static final int QUEUE_CAPACITY = 100;
    private static final Long KEEP_ALIVE_TIME = 1L;
    public static void main(String[] args) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAX_POOL_SIZE,
                KEEP_ALIVE_TIME,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(QUEUE_CAPACITY),
                new ThreadPoolExecutor.CallerRunsPolicy());
        try {
            ServerSocket serverSocket = new ServerSocket(9090);
            while(true){
                log.info("等待连接...");
                //这是一个阻塞方法, 会在一个 clientSocket连接建立后未释放前一直阻塞, 其它 clientSocket不能同时响应
                //通过建立线程以及线程池可以解决, 但很耗费服务器性能, 不是最优方案, 最优方案为 NIO, 详见 NioServer
                Socket clientSocket = serverSocket.accept();
                log.info("有客户端连接了");
                executor.execute(() -> {
                    try {
                        while (true){
                            if(handler(clientSocket) == -1){
                                break;
                            }
                        }
                    } catch (IOException e) {
                        log.error("异常!", e);
                    } finally {
                        try {
                            log.info("该客户端退出");
                            clientSocket.close();
                        } catch (IOException e) {
                            log.error("异常!",e);
                        }
                    }
                });
            }
        } catch (IOException e) {
            log.error("异常!",e);
        } finally {
            executor.shutdown();
        }
    }
    private static int handler(Socket clientSocket) throws IOException{
        byte[] bytes = new byte[1024];
        log.info("准备读取传入的数据");
        int read = clientSocket.getInputStream().read(bytes);
        log.info("读取完毕");
        String info = new String(bytes,0,read);
        if(read != -1){
            log.info("接收到客户端的数据"+info);
        }
        if("q".equals(info)){
            return -1;
        }
        return 0;
    }
}
