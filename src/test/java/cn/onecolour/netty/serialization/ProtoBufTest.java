package cn.onecolour.netty.serialization;


import cn.onecolour.netty.pojo.DemoProto;
import com.google.protobuf.InvalidProtocolBufferException;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;

import static cn.onecolour.netty.pojo.DemoProto.*;

/**
 * @author yang
 * @date 2022/6/6
 * @description
 */
public class ProtoBufTest {

    @Test
    public void toByteArray() throws InvalidProtocolBufferException {
        Demo demo = Demo.newBuilder()
                .setId("1")
                .setName("test")
                .setDesc("test_desc")
                .setUpdateTime(System.currentTimeMillis())
                .setCreateTime(System.currentTimeMillis())
                .setVersion(1)
                .build();
        byte[] bytes = demo.toByteArray();
        Demo parse = Demo.parseFrom(bytes);
        System.out.println(parse.equals(demo));
    }
}
