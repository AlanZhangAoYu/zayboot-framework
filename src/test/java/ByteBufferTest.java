import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

@Slf4j
public class ByteBufferTest {
    public static void main(String[] args) {
        try {
            FileChannel channel = new FileInputStream("README.md").getChannel();
            ByteBuffer buffer=ByteBuffer.allocate(100);
            while (true){
                int len = channel.read(buffer);
                log.info("读到字节数：{}", len);
                if (len == -1) {
                    break;
                }
                // 切换 buffer 读模式
                buffer.flip();
                while(buffer.hasRemaining()) {
                    log.info("{}", (char)buffer.get());
                }
                // 切换 buffer 写模式
                buffer.clear();
            }
        }catch (IOException e){
            log.error("IO异常:",e);
        }
    }
}
