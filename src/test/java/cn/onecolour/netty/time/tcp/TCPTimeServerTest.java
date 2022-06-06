package cn.onecolour.netty.time.tcp;

import org.junit.Test;

/**
 * @author yang
 * @date 2022/6/6
 * @description
 */
public class TCPTimeServerTest {
    @Test
    public void serverStartTest() throws InterruptedException {
        TimeProtocolTcpServer.start();
    }

    @Test
    public void clientStartTest() throws InterruptedException {
        TimeProtocolTcpClient.run();
    }
}
