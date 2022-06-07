package cn.onecolour.netty.http;

import cn.onecolour.netty.http.demo.HttpDemoServer;
import org.junit.Test;

/**
 * @author yang
 * @date 2022/6/7
 * @description
 */
public class HttpServerTest {
    @Test
    public void startDemo() throws InterruptedException {
        HttpDemoServer.startUp(9999);
    }
}
