package com.zxj.common;

import io.netty.handler.codec.http.FullHttpRequest;

/**
 * @Description:
 * @Author:zhangxiaojun
 * @Date:Created in 17:45 2018/6/28
 */
public class HttpJsonRequest {

    private FullHttpRequest request;
    private Object body;
    private String url;

    public HttpJsonRequest() {
    }

    public HttpJsonRequest(FullHttpRequest request, Object body, String url) {
        this.request = request;
        this.body = body;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public FullHttpRequest getRequest() {
        return request;
    }

    public void setRequest(FullHttpRequest request) {
        this.request = request;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "HttpJsonRequest{" +
                "request=" + request +
                ", body=" + body +
                ", url='" + url + '\'' +
                '}';
    }
}
