package com.zxj.registry.model;

import com.google.common.collect.Lists;
import com.zxj.registry.service.LoadBalanceService;
import com.zxj.registry.service.impl.RandomLoadBalance;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.List;

public class ServiceDiscovery {
    // 缓存服务器地址集合
    List<String> serviceResps = Lists.newArrayList();
    LoadBalanceService loadBalanceService;
    private CuratorFramework curatorFramework = null;
    private static final String REGISTRY_ROOT = "/registry";

    {
        loadBalanceService = new RandomLoadBalance();
        curatorFramework = CuratorFrameworkFactory.
                builder().connectString("192.168.167.133:2181")
                .sessionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 0))
                .build();
        curatorFramework.start();
    }

    // 获取注册服务
    public void init(String serviceName) throws Exception {
        String path = REGISTRY_ROOT + "/" + serviceName;
        try {
            serviceResps = curatorFramework.getChildren().forPath(path);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        registerWatcher(path);
    }

    //注册监听
    private void registerWatcher(final String path) throws Exception {
        PathChildrenCache pathChildrenCache = new PathChildrenCache(curatorFramework, path, true);
        PathChildrenCacheListener listener = new PathChildrenCacheListener() {
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                serviceResps = curatorFramework.getChildren().forPath(path);
            }
        };
        pathChildrenCache.getListenable().addListener(listener);
        pathChildrenCache.start();
    }

    public String getServiceEndPoint(){
        return loadBalanceService.selectHost(serviceResps);
    }

    public List<String> getServiceResps(){
        return serviceResps;
    }

    public static void main(String[] args) throws Exception {
        ServiceDiscovery serviceDiscovery =  new ServiceDiscovery();
        serviceDiscovery.init("provider-service");
        while (true){
            Thread.sleep(2000L);
            String serviceEndPoint = serviceDiscovery.getServiceEndPoint();
            System.out.println(serviceEndPoint);
        }
    }
}
