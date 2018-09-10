package com.wjyup.coolq.util;

import java.util.Locale;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 线程中取Spring管理的Bean的工具类
 * @author WJY
 */
@Component
public class SpringContext implements ApplicationContextAware{
	private static ApplicationContext context = null;
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContext.context = applicationContext;
    }

    @SuppressWarnings("unchecked")
	public static <T> T getBean(String beanName){
        return (T) context.getBean(beanName);
    }

    public static <T> T getBean(Class<T> cls) {
        return context.getBean(cls);
    }

    public static String getMessage(String key){
        return context.getMessage(key, null, Locale.getDefault());
    }

    /**
     * 获取ConfigCache
     * @return ConfigCache.class
     */
    public static ConfigCache getConfigCache() {
        return getBean(ConfigCache.class);
    }

    public static ApplicationContext getContext() {
        return context;
    }
}
