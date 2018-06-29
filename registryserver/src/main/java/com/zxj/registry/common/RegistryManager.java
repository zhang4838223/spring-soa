package com.zxj.registry.common;

import com.google.common.collect.Lists;
import com.zxj.registry.model.RegistryPO;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description:
 * @Author:zhangxiaojun
 * @Date:Created in 17:23 2018/6/26
 */
public class RegistryManager {

    private static ConcurrentHashMap<String, List<RegistryPO>> urlMap = new ConcurrentHashMap<String, List<RegistryPO>>();
    private static RegistryManager instance = new RegistryManager();

    private RegistryManager(){}

    public static RegistryManager getInstance() {
        return instance;
    }

    public void loadService(RegistryPO registryPO) {
        List<RegistryPO> addrs = urlMap.get(registryPO.getUri());
        if (CollectionUtils.isEmpty(addrs)) {
            urlMap.put(registryPO.getUri(), Lists.newArrayList(registryPO));
        } else {
            for (RegistryPO po : addrs) {
                if (po.getUri().equals(registryPO.getUri())) {
                    addrs.add(registryPO);
                }
            }
        }
    }

    public List<RegistryPO> getAllService() {
       Collection<List<RegistryPO>> values = urlMap.values();
        ArrayList<RegistryPO> result = Lists.newArrayList();
        for (List<RegistryPO> list : values) {
            for (RegistryPO po : list) {
                result.add(po);
            }
       }
        return result;
    }
}
