package com.wjyup.coolq.service.impl.plugins;

import com.wjyup.coolq.entity.RequestData;
import com.wjyup.coolq.service.ResolveMessageService;
import com.wjyup.coolq.util.LocalCache;
import com.wjyup.coolq.util.WebUtil;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;
import us.codecraft.xsoup.Xsoup;

import java.util.List;

/**
 * 知乎日报
 */
@Component
public class ZhiHuDailyService extends ResolveMessageService {
    private final String KEY = "zhihu_daily";
    @Override
    public void doit(RequestData data) throws Exception {
        String msg = data.getMsg();
        if (msg.equalsIgnoreCase("zhihu")) {
            String zhihu = (String) LocalCache.getCache(KEY);
            if (StringUtils.isBlank(zhihu)) {
                writeZhiHuDailyCache();
                reply(data, (String) LocalCache.getCache(KEY));
            } else {
                reply(data, zhihu);
            }
        }
    }

    /**
     * 获取知乎日报，并写入缓存
     */
    private void writeZhiHuDailyCache(){
        String url = "http://daily.zhihu.com";
        try {
            String result = WebUtil.get(url);
            if(StringUtils.isNotBlank(result)){
                List<Element> list = Xsoup.select(result, "//div[@class='main-content-wrap']/div[@class='row']/div[@class='col-lg-4']").getElements();
                StringBuilder strs = new StringBuilder("知乎日报：\n");
                //只截取开头9条记录
                for(Element el : list){
                    List<Element> elList = Xsoup.select(el, "//div[@class='wrap']/div[@class='box']/a").getElements();
                    int  b = 0;
                    for(Element els : elList){
                        String str = String.format("\n%s\n->链接：%s\n", els.getElementsByClass("title").text().trim(),"http://daily.zhihu.com"+els.attr("href"));
                        strs.append(str);
                        if(b == 3) break;
                        b++;
                    }
                }
                //写入缓存
                LocalCache.addCache(KEY, strs.toString());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
