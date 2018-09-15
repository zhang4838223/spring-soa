package com.zxj.registry.service;

import java.util.List;

public interface LoadBalanceService {
    String selectHost(List<String> serviceRepos);
}
