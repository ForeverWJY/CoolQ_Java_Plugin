package com.wjyup.coolq.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class InitialPlugins implements ApplicationListener<ContextRefreshedEvent> {
    private Logger log = LogManager.getLogger(InitialPlugins.class);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
//        log.info("开始注册事件");
//        long start = System.currentTimeMillis();
//
//        log.info("加载完毕！耗时{}毫秒！", (System.currentTimeMillis() - start));
    }
}
