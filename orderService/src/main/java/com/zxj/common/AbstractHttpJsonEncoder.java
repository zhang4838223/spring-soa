package com.zxj.common;

import com.google.gson.Gson;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.nio.charset.Charset;
import java.util.List;

/**
 * @Description:
 * @Author:zhangxiaojun
 * @Date:Created in 17:48 2018/6/28
 */
public abstract class AbstractHttpJsonEncoder<T> extends MessageToMessageEncoder<T> {
    private final static Charset UTF_8 = Charset.forName("utf-8");
    private static final Gson gson = new Gson();

    protected ByteBuf encode(ChannelHandlerContext ctx, Object body) throws Exception {
        String json = gson.toJson(body);
        ByteBuf byteBuf = Unpooled.copiedBuffer(json, UTF_8);
        return byteBuf;
    }
}
