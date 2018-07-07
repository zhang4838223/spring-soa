package com.zxj.netty;


import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.springframework.http.HttpMethod;

import java.net.MalformedURLException;
import java.util.concurrent.ExecutionException;

public class NettyHttpClientTemplate {

    private final static Gson GSON = new Gson();
    public <T> T postForEntity(String url, String body, Class<T> responseType) throws ExecutionException, InterruptedException {

        SimpleNettyHttpResponse response = null;
        try {
            //build request
            Netty4HttpRequest httpRequst = NettyHttpRequstFactory.getFactory().createHttpRequst(url, body, HttpMethod.POST);
            //do request
            response = httpRequst.execute().get();
            //parse response
            String content = response.getContent();
            T t = GSON.fromJson(content, responseType);
            return t;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } finally {
            if (null != response) {
                response.close();
            }
        }

        return null;
    }
}