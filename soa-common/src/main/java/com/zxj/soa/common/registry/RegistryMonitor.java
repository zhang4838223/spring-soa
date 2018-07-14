package com.zxj.soa.common.registry;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.zxj.soa.common.model.RegistryPO;
import com.zxj.soa.common.model.SoaRequest;
import com.zxj.soa.common.model.SoaResponse;
import com.zxj.soa.common.netty.NettyHttpClientTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * @Description:
 * @Author:zhangxiaojun
 * @Date:Created in 10:55 2018/6/28
 */
public class RegistryMonitor {
    //注册中心ip:port，多个用";"号隔开
    private String registryAddr;
    //当前应用端口
    private String applicationPort;
    //当前应用名
    private String applicationName;
    //NettyTemplate
    @Autowired
    private NettyHttpClientTemplate template;
    //注册中心接口路径
    private String registryPath = "/soa/regist/loadService";

    private Gson gson = new Gson();

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    public void registry() {
        List<RegistryPO> exports = getExporters();
        registWithTemplate(exports);

    }

    /**
     * 当前应用服务需要注册到所有注册中心上
     * @param exports
     */
    private void registWithTemplate(List<RegistryPO> exports) {
        String[] addrs = registryAddr.split(";");
        for (String addr : addrs) {
            StringBuilder sb = new StringBuilder();
            sb.append("http://").append(addr).append(registryPath);
            try {
                SoaRequest request = new SoaRequest();
                request.setRegistryPOList(new ArrayList<RegistryPO>(exports));
                SoaResponse soaResponse = template.postForEntity(sb.toString(), request, SoaResponse.class);
                System.out.println("--------------> 服务注册：" + soaResponse.getMsgText());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
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
                sb.append(localIp).append(":").append(applicationPort);

                RegistryPO registryPO = new RegistryPO();
                registryPO.setAddr(sb.toString());
//                registryPO.setLoadBalanceType(0);
//                registryPO.setTimeout();
                Set<RequestMethod> methods = key.getMethodsCondition().getMethods();
                registryPO.setSoaType(new ArrayList<RequestMethod>(methods).get(0).toString());
                registryPO.setUri(sb.append("/").append(applicationName).append(url).toString());
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
