package com.zxj.registry;

import com.alibaba.druid.support.json.JSONUtils;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.zxj.common.HttpUtil;
import com.zxj.common.NettyClient;
import com.zxj.model.RegistryPO;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Description:
 * @Author:zhangxiaojun
 * @Date:Created in 10:55 2018/6/28
 */
public class RegistryMonitor {
    private String registryAddr;

    private String applicationPort;

    private String applicationName;

    private String registryPath = "/soa/regist/loadService";

    private Gson gson = new Gson();

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    public void registry() {
        List<RegistryPO> exports = getExporters();
//        regist(exports);
        doRegist(exports);
    }

    private void doRegist(List<RegistryPO> exports) {
        for (RegistryPO po : exports) {
            HttpUtil.httpPostByCatchException(registryAddr, po);
        }
    }

    private void regist(List<RegistryPO> exports) {
        try {
            Channel channel = NettyClient.getChannel(registryAddr);
            for (RegistryPO po : exports) {
                DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1,
                        HttpMethod.POST,
                        new URI(registryAddr+registryPath).toASCIIString(),
                        Unpooled.wrappedBuffer(gson.toJson(po).getBytes()));
                request.headers().set(HttpHeaders.Names.CONTENT_TYPE, "application/json");
                request.headers().set(HttpHeaders.Names.CONTENT_LENGTH, request.content().readableBytes());
                channel.writeAndFlush(request);
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private List<RegistryPO> getExporters() {
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
        Set<Map.Entry<RequestMappingInfo, HandlerMethod>> entries = handlerMethods.entrySet();
        List<RegistryPO> list = Lists.newArrayList();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : entries) {
            RequestMappingInfo key = entry.getKey();
            HandlerMethod value = entry.getValue();

            String localIp = null;
            try {
                localIp = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }

            Set<String> urls = key.getPatternsCondition().getPatterns();
            for (String url : urls) {
                StringBuilder sb = new StringBuilder();
                sb.append(localIp).append(":").append(applicationPort).append("/").append(applicationName).append(url);

                RegistryPO registryPO = new RegistryPO();
                registryPO.setAddr(localIp);
//                registryPO.setLoadBalanceType(0);
//                registryPO.setTimeout();
                Set<RequestMethod> methods = key.getMethodsCondition().getMethods();
                registryPO.setSoaType(new ArrayList<RequestMethod>(methods).get(0).toString());
                registryPO.setUri(sb.toString());
                list.add(registryPO);
            }
        }
        return list;
    }

    public String getRegistryAddr() {
        return registryAddr;
    }

    public void setRegistryAddr(String registryAddr) {
        this.registryAddr = registryAddr;
    }

    public String getApplicationPort() {
        return applicationPort;
    }

    public void setApplicationPort(String applicationPort) {
        this.applicationPort = applicationPort;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }
}
