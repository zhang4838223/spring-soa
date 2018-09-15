package com.zxj.registry.service;

import com.zxj.registry.service.impl.ServiceRegistryImpl;

import java.io.IOException;

public class ZookeeperDemo {
    public static void main(String[] args) throws IOException {
        ServiceRegistry registry = new ServiceRegistryImpl();
        registry.register("provider-service", "192.168.2.2:21811");
        System.in.read();
    }
}
