package cn.onecolour.netty.time.udp;

import cn.onecolour.utils.TimeUtils;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author yang
 * @date 2022/6/3
 * @description
 */
public class UDPTimeServerTest {
    @Test
    public void serverStartTest() throws InterruptedException {
        TimeProtocolUdpServer.start();
    }

    @Test
    public void clientStartTest() throws InterruptedException {
        TimeProtocolUdpClient.run((ctx, msg) -> {
            long timestamp = msg.content().readUnsignedInt();
            String timeString = TimeUtils.timeFormat(timestamp - 2208988800L, TimeUnit.SECONDS);
            System.out.printf("Response: %s——%s\n", timestamp, timeString);
        });
    }
}
