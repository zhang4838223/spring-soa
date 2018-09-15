package com.zxj.registry.service;

import org.springframework.util.CollectionUtils;

import java.util.List;

public abstract class AbsLoadBalance implements  LoadBalanceService{

    public String selectHost(List<String> serviceRepos) {
        if (CollectionUtils.isEmpty(serviceRepos)) {
            return null;
        }

        if (serviceRepos.size() == 1) {
            return serviceRepos.get(0);
        }

        return doSelect(serviceRepos);
    }

    protected abstract String doSelect(List<String> serviceRepos);
}
