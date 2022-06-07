package cn.onecolour.netty.http.demo;

import cn.onecolour.utils.JsonUtils;
import com.google.common.collect.Maps;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static io.netty.handler.codec.http.HttpHeaderNames.*;
import static io.netty.handler.codec.http.HttpHeaderValues.KEEP_ALIVE;
import static io.netty.handler.codec.http.HttpHeaderValues.*;

/**
 * @author yang
 * @date 2022/6/7
 * @description
 */
public class HttpDemoServerHandler extends SimpleChannelInboundHandler<HttpRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpRequest request) {
        HttpHeaders headers = request.headers();
        List<Map.Entry<String, String>> entries = headers.entries();
        Map<String, String> content = Maps.newHashMap();
        for (Map.Entry<String, String> entry : entries) {
            if (content.containsKey(entry.getKey())) {
                content.put(entry.getKey(), String.format("%s,%s", content.get(entry.getKey()), entry.getValue()));
            } else {
                content.put(entry.getKey(), entry.getValue());
            }
        }

        content.put("HttpMethod", request.method().name());
        content.put("Protocol", request.protocolVersion().text());
        HttpHeaders respHeaders = new DefaultHttpHeaders();
        respHeaders.add("Content-Type", "application/json");
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(request.protocolVersion(), HttpResponseStatus.OK);

        response.content()
                .writeBytes(JsonUtils.toJsonString(content).getBytes(StandardCharsets.UTF_8));
        response.headers()
                .set(CONTENT_TYPE, APPLICATION_JSON)
                .setInt(CONTENT_LENGTH, response.content().readableBytes());

        boolean keepAlive = HttpUtil.isKeepAlive(request);

        if (keepAlive) {
            if (!request.protocolVersion().isKeepAliveDefault()) {
                response.headers().set(CONNECTION, KEEP_ALIVE);
            }
        } else {
            response.headers().set(CONNECTION, CLOSE);
        }

        ChannelFuture f = ctx.writeAndFlush(response);

        if (!keepAlive) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }
}
