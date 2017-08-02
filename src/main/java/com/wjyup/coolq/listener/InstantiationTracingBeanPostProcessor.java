package com.wjyup.coolq.listener;

import com.wjyup.coolq.util.ConfigCache;
import com.wjyup.coolq.util.ScanPackage;
import com.wjyup.coolq.util.service.ResolveMessageService;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Set;

/**
 * 当spring 容器初始化完成后加载所有的插件
 * Created by WJY on 2017/8/2.
 */
@Repository
public class InstantiationTracingBeanPostProcessor implements ApplicationListener<ContextRefreshedEvent> {
    private Logger log = Logger.getLogger(getClass());

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("开始加载插件：");
        long now = System.currentTimeMillis();

        //遍历指定包下的所有类
        Set<Class<?>> list = null;
        try {
            list = ScanPackage.getClasses(ConfigCache.PLUGIN_PACKAGE_PATH);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }

        if(list != null){
            ArrayList<String> pluginList = new ArrayList<>(list.size());

            //Spring获取bean并加入集合缓存中
            list.forEach(v -> pluginList.add(v.getSimpleName()));

            //获取所有bean的名称
            String [] beans= event.getApplicationContext().getBeanDefinitionNames();
            for(String beanName:beans){
                System.out.println(beanName);
                for (String s : pluginList) {
                    if(s.equalsIgnoreCase(beanName)){
                        ResolveMessageService rms = (ResolveMessageService) event.getApplicationContext().getBean(beanName);
                        ConfigCache.MSG_PLUGIN_LIST.add(rms);
                    }
                }
            }
        }

        log.info("插件加载完毕！耗时"+(System.currentTimeMillis() - now)+"毫秒！");
    }
}
