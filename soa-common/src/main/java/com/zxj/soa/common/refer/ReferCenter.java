package com.zxj.soa.common.refer;

import com.google.common.collect.Sets;
import com.zxj.soa.common.model.RegistryPO;
import com.zxj.soa.common.model.SoaServiceResponse;
import com.zxj.soa.common.netty.NettyHttpClientTemplate;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

public class ReferCenter {

    /**
     * 注册中心ip:port，多个用";"号隔开，多个地址用‘;’分割
     */
    private String registerAddr;
    /**
     * 默认定时拉取注册中心服务间隔时间
     */
    private long defaultInterval = 3L;
    /**
     * applicationNmae与IP:port对应关系
     */
    private ConcurrentHashMap<String, Set<String>> addrMap= new ConcurrentHashMap();
    /**
     * applicationNmae与URI对应关系
     */
    private ConcurrentHashMap<String, Set<String>> uriMap= new ConcurrentHashMap();

    private ReentrantLock locker = new ReentrantLock();
    //NettyTemplate
    @Autowired
    private NettyHttpClientTemplate template;

    //获取注册服务
    private static final String LOAD_SERVICE_PATH = "/soa/regist/getAllService";

    //初始化加载注册中心服务到本地内存
    public void init() {
        Executors.newScheduledThreadPool(1)
                .scheduleAtFixedRate(new ReferTask(registerAddr, template),3 , defaultInterval, TimeUnit.SECONDS);
    }

    public class ReferTask implements Runnable {

        private String registryAddr;
        private NettyHttpClientTemplate template;
        public void run() {
            System.out.println("--------->load registry service");
            String[] addrs = registryAddr.split(";");
            for (String addr : addrs) {
                StringBuilder sb = new StringBuilder();
                sb.append("http://").append(addr).append(LOAD_SERVICE_PATH);

                try {
                    SoaServiceResponse response = template.postForEntity(sb.toString(), StringUtils.EMPTY, SoaServiceResponse.class);
                    if (null != response && response.getMsgCode() == 200){
                        List<RegistryPO> list = response.getList();
                        if (CollectionUtils.isEmpty(list)) {
                            return;
                        }

                        locker.tryLock();
                        try {
                            for (RegistryPO po : list) {
                                //加载注册中心服务到本地缓存
                                String uri = po.getUri();
                                String[] args = uri.split("/");
                                String ipNet = args[0];
                                String appName = args[1];
                                String serviceUri = createUri(args, 2);
                                if (addrMap.containsKey(appName)) {
                                    addrMap.get(appName).add(ipNet);
                                } else {
                                    addrMap.put(appName, Sets.newHashSet(ipNet));
                                }

                                if (uriMap.containsKey(appName)) {
                                    uriMap.get(appName).add(serviceUri);
                                } else {
                                    uriMap.put(appName, Sets.newHashSet(serviceUri));
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            if (locker.isLocked()) {
                                locker.unlock();
                            }
                        }
                        System.out.println("--------->load registry service");
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }

        private String createUri(String[] args, int i) {
            StringBuffer sb = new StringBuffer();
            for (int index = i; index < args.length; index++) {
                sb.append(args[index]);
            }
            return sb.toString();
        }

        public ReferTask(String registryAddr, NettyHttpClientTemplate template) {
            this.registryAddr = registryAddr;
            this.template = template;
        }
    }

    public String getRegisterAddr() {
        return registerAddr;
    }

    public void setRegisterAddr(String registerAddr) {
        this.registerAddr = registerAddr;
    }

    public long getDefaultInterval() {
        return defaultInterval;
    }

    public void setDefaultInterval(long defaultInterval) {
        this.defaultInterval = defaultInterval;
    }


}
