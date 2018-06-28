package com.zxj.model;

/**
 * @Description:
 * @Author:zhangxiaojun
 * @Date:Created in 10:33 2018/6/28
 */
public class OrderRequest {

    private Long orderId;
    private String orderCode;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }
}
