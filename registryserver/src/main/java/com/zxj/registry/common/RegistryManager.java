package com.zxj.registry.common;

import com.google.common.collect.Lists;
import com.zxj.registry.model.RegistryPO;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description:
 * @Author:zhangxiaojun
 * @Date:Created in 17:23 2018/6/26
 */
public class RegistryManager {

    private static ConcurrentHashMap<String, List<String>> urlMap = new ConcurrentHashMap<String, List<String>>();
    private static RegistryManager instance = new RegistryManager();

    private RegistryManager(){}

    public static RegistryManager getInstance() {
        return instance;
    }

    public void loadService(RegistryPO registryPO) {
        List<String> addrs = urlMap.get(registryPO.getUri());
        if (CollectionUtils.isEmpty(addrs)) {
            urlMap.put(registryPO.getUri(), Lists.newArrayList(registryPO.getAddr()));
        } else {
            addrs.add(registryPO.getAddr());
        }
    }
}
