package com.zxj.registry.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @Description:
 * @Author:zhangxiaojun
 * @Date:Created in 15:02 2018/6/26
 */
public class SpringContextContainer implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextContainer.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static <T> T getBean(String name) {
        T bean = (T)applicationContext.getBean(name);
        if (null == bean) {
            bean = (T)applicationContext.getParent().getBean(name);
        }

        return bean;
    }

    public static <T> T getBean(Class<T> clzz) {
        T bean = applicationContext.getBean(clzz);
        if (null == bean) {
            bean = applicationContext.getParent().getBean(clzz);
        }

        return bean;
    }
}
