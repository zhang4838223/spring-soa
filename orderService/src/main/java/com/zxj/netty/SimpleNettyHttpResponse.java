package com.zxj.netty;

public class SimpleNettyHttpResponse {

    private int status;

    private String content;

    public SimpleNettyHttpResponse(int status, String content) {
        this.status = status;
        this.content = content;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
