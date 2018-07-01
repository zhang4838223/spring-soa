package com.zxj.registry.controller;

import com.google.common.collect.Lists;
import com.zxj.registry.common.RegistryManager;
import com.zxj.registry.model.RegistryPO;
import com.zxj.registry.model.SoaRequest;
import com.zxj.registry.model.SoaResponse;
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

    @RequestMapping(value = "regist/getAllService", method = RequestMethod.GET)
    @ResponseBody
    public List<RegistryPO> getAllService() {
        List<RegistryPO> list = RegistryManager.getInstance().getAllService();
        return list;
    }

}
