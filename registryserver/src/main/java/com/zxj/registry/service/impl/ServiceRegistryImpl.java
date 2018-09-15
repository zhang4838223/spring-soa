package com.zxj.registry.service.impl;

import com.zxj.registry.service.ServiceRegistry;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.io.IOException;

/**
 * 服务注册
 */
public class ServiceRegistryImpl implements ServiceRegistry {
    private CuratorFramework curatorFramework = null;
    private static final String REGISTRY_ROOT = "/registry";

    {
        curatorFramework = CuratorFrameworkFactory.
                builder().connectString("192.168.167.133:2181")
                .sessionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 0))
                .build();
        curatorFramework.start();
    }

    public void register(String serviceName, String serviceAddr) {
        String servicePath = REGISTRY_ROOT + "/" +serviceName;
        try {
            // 创建服务节点
            if (curatorFramework.checkExists().forPath(servicePath) == null){
                curatorFramework.create().
                        creatingParentsIfNeeded().
                        withMode(CreateMode.PERSISTENT).forPath(servicePath);
            }
            // 创建协议协议
            String addressPath = servicePath + "/" + serviceAddr;
            curatorFramework.create().withMode(CreateMode.EPHEMERAL).forPath(addressPath);
            System.out.println("节点注册成功：" +addressPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        ServiceRegistry registry = new ServiceRegistryImpl();
        registry.register("provider-service", "192.168.1.111:21811");
        System.in.read();
    }
}
