package com.zxj.soa.common.model;

import java.util.List;

public class SoaServiceResponse extends SoaResponse{
    private List<RegistryPO> list;

    public SoaServiceResponse() {
    }

    public SoaServiceResponse(int msgCode, String msgText) {
        super(msgCode, msgText);
    }

    public List<RegistryPO> getList() {
        return list;
    }

    public void setList(List<RegistryPO> list) {
        this.list = list;
    }
}
