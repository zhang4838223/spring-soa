package com.zxj.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpMethod;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * NETTY的http请求类
 */
public class Netty4HttpRequest {
    private final static Charset UTF_8 = Charset.forName("utf-8");
    private String url;
    private ByteBufOutputStream body;
    private Bootstrap bootstrap;
    private HttpMethod httpMethod;
    private static Pattern pattern = Pattern.compile("((http|ftp|https)://)(([a-zA-Z0-9._-]+)|([0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}))(([a-zA-Z]{2,6})|(:[0-9]{1,4})?)");
//    private static Pattern pattern = Pattern.compile("(?<=//|)((\\w)+\\.)+\\w+(:\\d{0,5})?");

    public Netty4HttpRequest(String url, String body, Bootstrap bootstrap, HttpMethod httpMethod) {
        this.url = url;
        this.body = new ByteBufOutputStream(Unpooled.copiedBuffer(body, UTF_8));
        this.bootstrap = bootstrap;
        this.httpMethod = httpMethod;
    }

    public NettyFuture<SimpleNettyHttpResponse> execute() throws MalformedURLException {
        //设置future对象用于获取返回值
        final NettyFuture<SimpleNettyHttpResponse> future = new NettyFuture<SimpleNettyHttpResponse>();

        ChannelFutureListener listener = new ChannelFutureListener() {
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()) {
                    Channel channel = channelFuture.channel();
                    channel.pipeline().addLast(new Netty4HttpRequestHandler(future));

                    FullHttpRequest request = createRequest();
                    channel.writeAndFlush(request);
                } else {
                    future.setException(channelFuture.cause());
                }
            }
        };

        //解析url中的ip、port
        String addr = getHost(url);
        String[] args = addr.split(":");
        bootstrap.connect(args[0], Integer.valueOf(args[1])).addListener(listener);
        return future;
    }

    private static String getHost(String url) throws MalformedURLException {
        //非空判断
        if (StringUtils.isBlank(url)) {
            return null;
        }

        String host = "";
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            host = matcher.group();
        }

        if (host.contains("http")) {
            String addr = host.replace("http://", "");
            return addr;
        }

        return host;

    }

    private FullHttpRequest createRequest() throws UnknownHostException {
        io.netty.handler.codec.http.HttpMethod method= io.netty.handler.codec.http.HttpMethod.valueOf(httpMethod.name());
        FullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, method, url, body.buffer());

        HttpHeaders headers = request.headers();
        headers.set(HttpHeaders.Names.HOST, InetAddress.getLocalHost()
                .getHostAddress());
        headers.set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.CLOSE);
        headers.set(HttpHeaders.Names.ACCEPT_ENCODING,
                HttpHeaders.Values.GZIP + ','
                        + HttpHeaders.Values.DEFLATE);
        headers.set(HttpHeaders.Names.ACCEPT_CHARSET,
                "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
        headers.set(HttpHeaders.Names.ACCEPT_LANGUAGE, "zh");
        headers.set(HttpHeaders.Names.USER_AGENT,
                "Netty json Http Client side");
        headers.set(HttpHeaders.Names.ACCEPT,
                "text/html,application/json;q=0.9,*/*;q=0.8");
        headers.set(HttpHeaders.Names.CONTENT_LENGTH, body.buffer().readableBytes());
        headers.set(HttpHeaders.Names.CONTENT_TYPE, "application/json;charset=UTF-8");

        return request;
    }

    private static class Netty4HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpResponse> {

        private NettyFuture<SimpleNettyHttpResponse> future;
        public Netty4HttpRequestHandler(NettyFuture<SimpleNettyHttpResponse> future) {
            this.future = future;
        }

        @Override
        protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpResponse fullHttpResponse) throws Exception {
            future.set(new SimpleNettyHttpResponse(fullHttpResponse, channelHandlerContext));
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            future.setException(cause);
            ctx.close();
        }
    }

    public static void main(String[] args) throws MalformedURLException {
        String addr = getHost("http://localhost:8081/soa/regist/loadService");
        String[] tem = addr.split(":");
        System.out.println(addr);
    }
}
