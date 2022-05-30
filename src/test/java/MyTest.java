import cn.zay.zayboot.server.HttpServer;
import org.junit.jupiter.api.Test;

public class MyTest {
    @Test
    public void reflectionTest(){

    }
    @Test
    public void ServerTest(){
        HttpServer server=new HttpServer();
        server.start();
    }
}
