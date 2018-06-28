package com.zxj.common;

import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by anchu.zhang on 2016/10/26.
 */
public class HttpUtil {

    static final String APPLICATION_JSON = "application/json";
    private static Gson gson =  new Gson();
    private static HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
    //HttpClient
    private static CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
    //设置超时时间
    private static RequestConfig config = RequestConfig.custom()
            .setConnectTimeout(15000)
            .build();
    private static HttpPost request = null;
    /**
     * http  post请求
     * 对异常进行处理封装为response
     * @throws Exception
     */
    public static String httpPostByCatchException(String url, Object object) {
        String jsonParam = gson.toJson(object);
        String result = "";
        if (StringUtils.isEmpty(url)) {
            return result;
        }
        InputStream input = null;//输入流
        InputStreamReader isr = null;
        BufferedReader buffer = null;
        StringBuffer stringBuffer = null;
        String line = null;
        try {

            /*post向服务器请求数据*/
            if (request == null) {
                request = new HttpPost(url);
                request.setHeader("content-Type", APPLICATION_JSON);
                request.setConfig(config);
            }
            StringEntity se = new StringEntity(jsonParam, HTTP.UTF_8);
            request.setEntity(se);

            HttpResponse response = closeableHttpClient.execute(request);
            int code = response.getStatusLine().getStatusCode();
            // 若状态值为200，则ok
            if (code == HttpStatus.SC_OK) {
                //从服务器获得输入流
                input = response.getEntity().getContent();
                isr = new InputStreamReader(input, HTTP.UTF_8);
                buffer = new BufferedReader(isr, 10 * 1024);

                stringBuffer = new StringBuffer();
                while ((line = buffer.readLine()) != null) {
                    stringBuffer.append(line);
                }
                result = stringBuffer.toString();
            } else {
                return gson.toJson(response);
            }
        }catch (Exception e){
            return e.toString();
        }finally {
            try {
                if (buffer != null) {
                    buffer.close();
                    buffer = null;
                }
                if (isr != null) {
                    isr.close();
                    isr = null;
                }
                if (input != null) {
                    input.close();
                    input = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }


}
