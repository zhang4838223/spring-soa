package com.zxj.soa.common.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import org.springframework.http.HttpMethod;

/**
 * netty的http请求工厂类
 */
public class NettyHttpRequstFactory {

    private Bootstrap bootstrap;
    private EventLoopGroup eventLoopGroup;

    private final static int MAX_RESPONSE_SIZE = 1024 * 1024 * 10 ;
    private static NettyHttpRequstFactory factory = new NettyHttpRequstFactory();

    private NettyHttpRequstFactory(){
        if (eventLoopGroup == null) {
            eventLoopGroup = new NioEventLoopGroup();
        }
    }

    public static NettyHttpRequstFactory getFactory() {
        return factory;
    }

    private Bootstrap buildBootstrap() {
        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
                .option(ChannelOption.SO_REUSEADDR, Boolean.TRUE)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, Boolean.TRUE)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new HttpResponseDecoder())
                                .addLast(new HttpRequestEncoder())
                                .addLast(new HttpObjectAggregator(MAX_RESPONSE_SIZE));
                    }
                });
        return bootstrap;
    }

    public Netty4HttpRequest createHttpRequst(String url, String body, HttpMethod method){

        return new Netty4HttpRequest(url, body, getBootstrap(), method);
    }

    private Bootstrap getBootstrap() {
        if (bootstrap == null) {
            bootstrap = buildBootstrap();
        }
        return bootstrap;
    }
}
