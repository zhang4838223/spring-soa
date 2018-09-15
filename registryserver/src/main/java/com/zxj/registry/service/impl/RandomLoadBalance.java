package com.zxj.registry.service.impl;

import com.zxj.registry.service.AbsLoadBalance;

import java.util.List;
import java.util.Random;

public class RandomLoadBalance extends AbsLoadBalance {
    protected String doSelect(List<String> serviceRepos) {
        int size = serviceRepos.size();
        Random random = new Random();

        return serviceRepos.get(random.nextInt(size));
    }
}
