package com.zxj.soa.common.model;

/**
 * @Description:
 * @Author:zhangxiaojun
 * @Date:Created in 17:03 2018/6/26
 */
public class RegistryPO {

    private String uri;
    //IP:PORT
    private String addr;
    //超时时间
    private long timeout;
    //负载均衡策略
    private byte loadBalanceType;
    //请求方式:post、get...
    private String soaType;

    public String getSoaType() {
        return soaType;
    }

    public void setSoaType(String soaType) {
        this.soaType = soaType;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public byte getLoadBalanceType() {
        return loadBalanceType;
    }

    public void setLoadBalanceType(byte loadBalanceType) {
        this.loadBalanceType = loadBalanceType;
    }
}
