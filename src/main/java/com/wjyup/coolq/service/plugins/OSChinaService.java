package com.wjyup.coolq.service.plugins;

import com.google.common.eventbus.Subscribe;
import com.wjyup.coolq.entity.RequestData;
import com.wjyup.coolq.event.GroupMsgEvent;
import com.wjyup.coolq.event.PrivateMsgEvent;
import com.wjyup.coolq.eventbus.XEventBus;
import com.wjyup.coolq.service.ResolveMessageService;
import com.wjyup.coolq.util.LocalCache;
import com.wjyup.coolq.util.WebUtil;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.xsoup.Xsoup;

import java.util.ArrayList;
import java.util.List;

@Component
public class OSChinaService extends ResolveMessageService implements InitializingBean {
    @Autowired
    private XEventBus eventBus;

    private final String oschinaNews = "oschina_news";
    private final String oschinaSoft = "oschina_soft";

    @Subscribe
    public void doit(PrivateMsgEvent event) {
        doit(event.getRequestData());
    }

    @Subscribe
    private void doit(GroupMsgEvent event) {
        doit(event.getRequestData());
    }

    private void doit(RequestData data) {
        String msg = data.getMsg();
        if("oschina".equals(msg)){
            List<String> cache = (List<String>) LocalCache.getCache(oschinaNews);
            if (cache == null || cache.isEmpty()) {
                writeOschinaCacheByAPI();
                cache = (List<String>) LocalCache.getCache(oschinaNews);
            }
            cache.forEach(x -> reply(data, x));
        }
    }

    /**
     * 读取oschina的数据，写入到缓存中
     */
    private void writeOschinaCacheByAPI(){
        String[] OSCHINA_TYPE = {"industry", "project"};
        String url = "https://www.oschina.net/action/ajax/get_more_news_list?newsType=";
        try {
            //获取资讯消息
            String result = WebUtil.get(url + OSCHINA_TYPE[0]);
            if (result != null) {
                Elements elements = Xsoup.select(result, "//div[@class='main-info box-aw']/a").getElements();
                List<String> strList = new ArrayList<>();
                for (int i = 1; i < elements.size(); i++) {
                    if (i > 10) break;
                    elements.get(i).children().forEach(x -> {
                        String href = x.parent().attr("href");
                        if (!href.startsWith("http")) {
                            href = "https://www.oschina.net" + href;
                        }
                        strList.add(String.format("%s\n->%s",x.text(), href));
                    });
                }
                //写入缓存
                LocalCache.addCache(oschinaNews, strList);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        eventBus.register(this);
    }
}
