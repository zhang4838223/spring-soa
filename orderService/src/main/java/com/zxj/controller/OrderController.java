package com.zxj.controller;

import com.alibaba.druid.support.json.JSONUtils;
import com.zxj.model.OrderRequest;
import com.zxj.model.SoaResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description:
 * @Author:zhangxiaojun
 * @Date:Created in 10:25 2018/6/28
 */
@Controller
public class OrderController {

    @RequestMapping(value = "order/getOrderByCode", method = RequestMethod.POST)
    @ResponseBody
    public SoaResponse getOrderInfo(@RequestBody OrderRequest request){
        System.out.println(JSONUtils.toJSONString(request));
        return new SoaResponse(200, "succ");
    }
}
