package com.wjyup.coolq.util;

import java.util.Locale;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
/**
 * 线程中取Spring管理的Bean的工具类
 * @author WJY
 */
public class SpringApplicationContextHolder implements ApplicationContextAware{
	private static ApplicationContext context = null;
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringApplicationContextHolder.context = applicationContext;
    }

    @SuppressWarnings("unchecked")
	public static <T> T getBean(String beanName){
        return (T) context.getBean(beanName);
    }

    public static String getMessage(String key){
        return context.getMessage(key, null, Locale.getDefault());
    }

}
