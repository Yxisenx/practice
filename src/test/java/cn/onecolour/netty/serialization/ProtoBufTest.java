package cn.onecolour.netty.serialization;


import cn.onecolour.utils.HexUtils;
import com.google.protobuf.util.JsonFormat;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

import static cn.onecolour.netty.pojo.DemoProto.Demo;

/**
 * @author yang
 * @date 2022/6/6
 * @description
 */
public class ProtoBufTest {

    @Test
    public void compareJsonAndProtoSize() throws IOException {
        Demo demo = Demo.newBuilder()
                .setId(1)
                .setName("test")
                .setDesc("test_desc")
                .setUpdateTime(1657617869284L)
                .setCreateTime(1657617869284L)
                .setVersion(1)
                .build();

//        JsonFormat.printer().print(demo);
        String json = "{\"id\":1,\"name\":\"test\",\"desc\":\"test_desc\",\"updateTime\":\"1657617869284\",\"createTime\":\"1657617869284\",\"version\":1}";
        byte[] jsonBytes = json.getBytes(StandardCharsets.UTF_8);
        byte[] protoBytes = demo.toByteArray();
        System.out.println("Json size: " + jsonBytes.length);
        System.out.println("Proto size: " + protoBytes.length);
        System.out.println("Proto Hex: " + HexUtils.byteArrayToHexString(protoBytes));
        System.out.println("Proto Base64: " + Base64.getEncoder().encodeToString(protoBytes));
        System.out.println("Base64 Decode Result: " + Arrays.equals(protoBytes, Base64.getDecoder().decode("CAESBHRlc3QaCXRlc3RfZGVzYyDk0+WNnzAo5NPljZ8wMAE=")));

        Demo.Builder builder = Demo.newBuilder();
        JsonFormat.parser().merge(json, builder);
        Demo newDemo = builder.build();
        System.out.println(newDemo.equals(demo));
//        Demo parse = Demo.parseFrom(protoBytes);
//        System.out.println(parse.equals(demo));
    }


}
