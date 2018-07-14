package com.zxj.registry.controller;

import com.zxj.registry.common.RegistryManager;
import com.zxj.soa.common.model.RegistryPO;
import com.zxj.soa.common.model.SoaRequest;
import com.zxj.soa.common.model.SoaResponse;
import com.zxj.soa.common.model.SoaServiceResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Description:
 * @Author:zhangxiaojun
 * @Date:Created in 16:00 2018/6/26
 */
@Controller
public class RegistryController {

    @RequestMapping(value = "regist/loadService", method = RequestMethod.POST)
    @ResponseBody
    public SoaResponse loadService(@RequestBody SoaRequest request) {
        RegistryManager.getInstance().loadService(request.getRegistryPOList());
        for (RegistryPO registry : request.getRegistryPOList()) {
            System.out.println("======================" + registry.getUri() + "服务注册成功======================");
        }
        return new SoaResponse(200, "succ");
    }

    @RequestMapping(value = "regist/getAllService", method = RequestMethod.POST)
    @ResponseBody
    public SoaServiceResponse getAllService() {
        SoaServiceResponse response = new SoaServiceResponse(200, "succ");
        List<RegistryPO> list = RegistryManager.getInstance().getAllService();
        response.setList(list);

        System.out.println("----------> 拉取注册服务");
        return response;
    }

}
