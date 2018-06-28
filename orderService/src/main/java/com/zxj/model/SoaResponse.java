package com.zxj.model;

/**
 * @Description:
 * @Author:zhangxiaojun
 * @Date:Created in 16:12 2018/6/26
 */
public class SoaResponse {

    private int msgCode;
    private String msgText;

    public SoaResponse() {
    }

    public SoaResponse(int msgCode, String msgText) {
        this.msgCode = msgCode;
        this.msgText = msgText;
    }

    public int getMsgCode() {
        return msgCode;
    }

    public void setMsgCode(int msgCode) {
        this.msgCode = msgCode;
    }

    public String getMsgText() {
        return msgText;
    }

    public void setMsgText(String msgText) {
        this.msgText = msgText;
    }
}
