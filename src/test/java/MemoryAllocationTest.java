import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

/**
 * 直接内存与堆内存, 理解 Netty的零拷贝
 */
@Slf4j
public class MemoryAllocationTest {
    @Test
    public void heapAccess(){
        long startTime = System.currentTimeMillis();
        //分配堆内存
        ByteBuffer byteBuffer = ByteBuffer.allocate(1000);
        for (int i = 0; i < 100000; i++) {
            for (int j = 0; j < 200; j++) {
                byteBuffer.putInt(j);
            }
            byteBuffer.flip();
            for (int j = 0; j < 200; j++) {
                byteBuffer.getInt(j);
            }
            byteBuffer.clear();
        }
        long endTime = System.currentTimeMillis();
        log.info("堆内存访问耗时:"+(endTime-startTime)+"毫秒");
    }
    @Test
    public void directAccess(){
        long startTime = System.currentTimeMillis();
        //分配直接内存(在JVM堆内存之外的内存条上的剩余的物理内存上直接分配)
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1000);
        for (int i = 0; i < 100000; i++) {
            for (int j = 0; j < 200; j++) {
                byteBuffer.putInt(j);
            }
            byteBuffer.flip();
            for (int j = 0; j < 200; j++) {
                byteBuffer.getInt(j);
            }
            byteBuffer.clear();
        }
        long endTime = System.currentTimeMillis();
        log.info("直接内存访问耗时:"+(endTime-startTime)+"毫秒");
    }
}
