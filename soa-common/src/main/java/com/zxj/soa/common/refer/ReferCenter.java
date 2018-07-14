package com.zxj.soa.common.refer;

import com.zxj.soa.common.model.RegistryPO;
import com.zxj.soa.common.model.SoaServiceResponse;
import com.zxj.soa.common.netty.NettyHttpClientTemplate;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private long defaultInterval = 3000L;
    /**
     * applicationNmae与IP:port对应关系
     */
    private final static ConcurrentHashMap<String, List<String>> addrMap= new ConcurrentHashMap();
    /**
     * applicationNmae与URI对应关系
     */
    private final static ConcurrentHashMap<String, List<String>> uriMap= new ConcurrentHashMap();

    private static ReentrantLock locker = new ReentrantLock();
    //NettyTemplate
    @Autowired
    private NettyHttpClientTemplate template;

    //获取注册服务
    private static final String LOAD_SERVICE_PATH = "/soa/regist/getAllService";

    //初始化加载注册中心服务到本地内存
    public void init() {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.schedule(new ReferTask(registerAddr, template), defaultInterval, TimeUnit.MILLISECONDS);
    }

    public static class ReferTask implements Runnable {

        private String registryAddr;
        private NettyHttpClientTemplate template;
        public void run() {
            System.out.println("--------->load registry service");
            String[] addrs = registryAddr.split(";");
            for (String addr : addrs) {
                StringBuilder sb = new StringBuilder();
                sb.append("http://").append(addr).append(LOAD_SERVICE_PATH);

                try {
                    SoaServiceResponse response = template.postForEntity(sb.toString(), null, SoaServiceResponse.class);
                    if (null != response && response.getMsgCode() == 200){
                        List<RegistryPO> list = response.getList();
                        if (CollectionUtils.isEmpty(list)) {
                            return;
                        }

                        if (addrMap.size() == 0 || uriMap.size() == 0) {
                            for (RegistryPO po : list) {
                                String uri = po.getUri();
                            }
                        }

                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
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
