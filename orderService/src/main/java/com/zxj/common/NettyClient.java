package com.zxj.common;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import org.apache.commons.lang.StringUtils;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description:
 * @Author:zhangxiaojun
 * @Date:Created in 14:27 2018/6/28
 */
public class NettyClient {

    private static ConcurrentHashMap<String, Channel> addressToChannel = new ConcurrentHashMap<String, Channel>();

    //注册中心地址
    public static Channel getChannel(String address) throws InterruptedException {
        if (StringUtils.isBlank(address)) {
            return null;
        }

        Channel channel = addressToChannel.get(address);
        if (null != channel && channel.isActive()) {
            return channel;
        }

        String[] strs = address.split(":");
        String host = strs[0];
        int port = Integer.valueOf(strs[1]);
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioSocketChannel.class)
                .option(ChannelOption.SO_REUSEADDR, Boolean.TRUE)
                .option(ChannelOption.SO_KEEPALIVE, Boolean.TRUE)
                .handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new HttpResponseDecoder())
                    .addLast(new HttpRequestEncoder())
                    .addLast(new HttpJsonRequestEncoder())
                    .addLast(new HttpClientInboundHandler());
            }
        });

        channel = bootstrap.connect(host, port).sync().channel();
        if (channel != null && channel.isActive()) {
            addressToChannel.put(address, channel);
        }
        return channel;
    }
}
