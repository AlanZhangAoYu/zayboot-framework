import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
public class ByteBufferTest {
    private static final Logger logger= LoggerFactory.getLogger(ByteBufferTest.class);
    public static void main(String[] args) {
        try {
            //FileChannel 数据的读取通道
            FileChannel channel = new FileInputStream("README.md").getChannel();
            //文件缓冲区
            ByteBuffer buffer=ByteBuffer.allocate(100);
            while (true){
                int len = channel.read(buffer);
                logger.info("读到字节数：{}", len);
                if (len == -1) {
                    //如果len为-1，代表读到了文件Stream末尾,应该结束循环
                    break;
                }
                // 切换 buffer 读模式
                buffer.flip();
                while(buffer.hasRemaining()) {
                    //当前buffer中是否有剩余的字符,有就输出
                    logger.info("{}", (char)buffer.get());
                }
                // 切换 buffer 写模式
                buffer.clear();
            }
        }catch (IOException e){
            logger.error("IO异常:",e);
        }
    }
}
