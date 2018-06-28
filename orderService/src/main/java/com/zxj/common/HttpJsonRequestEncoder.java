package com.zxj.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;

import java.net.InetAddress;
import java.util.List;

/**
 * @Description:
 * @Author:zhangxiaojun
 * @Date:Created in 17:42 2018/6/28
 */
public class HttpJsonRequestEncoder extends AbstractHttpJsonEncoder<HttpJsonRequest> {


    @Override
    protected void encode(ChannelHandlerContext ctx, HttpJsonRequest msg, List<Object> out) throws Exception {
        //(1)调用父类的encode0，将业务需要发送的对象转换为Json
        ByteBuf body = encode(ctx, msg.getBody());
        //(2) 如果业务自定义了HTTP消息头，则使用业务的消息头，否则在这里构造HTTP消息头
        // 这里使用硬编码的方式来写消息头，实际中可以写入配置文件
        FullHttpRequest request = msg.getRequest();
        if (request == null) {
            request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1,
                    HttpMethod.POST, msg.getUrl(), body);
            HttpHeaders headers = request.headers();
            headers.set(HttpHeaders.Names.HOST, InetAddress.getLocalHost()
                    .getHostAddress());
            headers.set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.CLOSE);
            headers.set(HttpHeaders.Names.ACCEPT_ENCODING,
                    HttpHeaders.Values.GZIP.toString() + ','
                            + HttpHeaders.Values.DEFLATE.toString());
            headers.set(HttpHeaders.Names.ACCEPT_CHARSET,
                    "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
            headers.set(HttpHeaders.Names.ACCEPT_LANGUAGE, "zh");
            headers.set(HttpHeaders.Names.USER_AGENT,
                    "Netty json Http Client side");
            headers.set(HttpHeaders.Names.ACCEPT,
                    "text/html,application/json;q=0.9,*/*;q=0.8");
            headers.set(HttpHeaders.Names.CONTENT_LENGTH, body.readableBytes());
            headers.set(HttpHeaders.Names.CONTENT_TYPE, "application/json;charset=UTF-8");
        }
//        HttpUtil.setContentLength(request, body.readableBytes());
        // (3) 编码后的对象
        out.add(request);
    }
}
