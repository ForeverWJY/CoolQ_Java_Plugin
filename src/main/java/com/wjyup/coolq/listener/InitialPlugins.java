package com.wjyup.coolq.listener;

import com.wjyup.coolq.service.ResolveMessageService;
import com.wjyup.coolq.util.ConfigCache;
import com.wjyup.coolq.util.ScanPackage;
import com.wjyup.coolq.util.SpringContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.nutz.json.Json;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class InitialPlugins implements ApplicationListener<ContextRefreshedEvent> {
    private Logger log = LogManager.getLogger(InitialPlugins.class);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("开始加载插件：");
        long start = System.currentTimeMillis();

        //遍历指定包下的所有类
        Set<Class<?>> list;
        try {
            list = ScanPackage.getClasses(SpringContext.getConfigCache().getPLUGIN_PACKAGE_PATH());
        } catch (Exception e) {
            log.error("插件加载出错！");
            throw new RuntimeException("插件加载出错\n", e);
        }

        if(list != null){
            List<ResolveMessageService> msgList = new ArrayList<>();
            list.forEach(x -> {
                ResolveMessageService rms = (ResolveMessageService) event.getApplicationContext().getBean(x);
                msgList.add(rms);
            });
            ConfigCache.MSG_PLUGIN_LIST.addAll(msgList);

            log.info("插件加载完毕！耗时{}毫秒！，加载的插件列表如下：\n{}", (System.currentTimeMillis() - start), Json.toJson(list));
        }
    }
}
