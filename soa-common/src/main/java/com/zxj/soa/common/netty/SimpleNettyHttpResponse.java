package com.zxj.soa.common.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.util.CharsetUtil;

public class SimpleNettyHttpResponse {

    private ChannelHandlerContext context;

    private FullHttpResponse response;

    public SimpleNettyHttpResponse(FullHttpResponse response, ChannelHandlerContext context) {
        this.context = context;
        this.response = response;
        response.retain();
    }

    public int getStatus() {
        return response.getStatus().code();
    }

    public String getContent() {
        return response.content().toString(CharsetUtil.UTF_8);
    }

    public void close() {
        response.release();
        context.close();
    }

}
