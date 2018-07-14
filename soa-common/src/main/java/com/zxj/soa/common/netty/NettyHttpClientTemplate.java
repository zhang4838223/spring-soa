package com.zxj.soa.common.netty;


import com.google.gson.Gson;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.util.concurrent.ExecutionException;

@Component
public class NettyHttpClientTemplate {

    private final static Gson GSON = new Gson();
    public <T> T postForEntity(String url, Object body, Class<T> responseType) throws ExecutionException, InterruptedException {

        SimpleNettyHttpResponse response = null;
        try {
            //build request
            Netty4HttpRequest httpRequst = NettyHttpRequstFactory.getFactory().createHttpRequst(
                    url, GSON.toJson(body), HttpMethod.POST);
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
