package com.zxj.registry.controller;

import com.zxj.registry.common.RegistryManager;
import com.zxj.registry.model.RegistryPO;
import com.zxj.registry.model.SoaResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description:
 * @Author:zhangxiaojun
 * @Date:Created in 16:00 2018/6/26
 */
@Controller
public class RegistryController {

    @RequestMapping(value = "regist/loadService", method = RequestMethod.POST)
    @ResponseBody
    public SoaResponse loadService(@RequestBody RegistryPO registry) {
        RegistryManager.getInstance().loadService(registry);
        return new SoaResponse(200, "succ");
    }
}