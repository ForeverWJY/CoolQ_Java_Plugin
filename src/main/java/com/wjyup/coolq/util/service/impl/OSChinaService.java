package com.wjyup.coolq.util.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wjyup.coolq.entity.RequestData;
import com.wjyup.coolq.entity.TempCache;
import com.wjyup.coolq.util.ConfigCache;
import com.wjyup.coolq.util.WebUtil;
import com.wjyup.coolq.util.service.ResolveMessageService;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import us.codecraft.xsoup.Xsoup;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * OSChina资讯插件
 * Created by WJY on 2017/8/1.
 */
public class OSChinaService extends ResolveMessageService{
    @Override
    public void doit(RequestData data) throws Exception {
        String key = data.getMsg();
        //oschina资讯
        if(key.length() == 7 && "oschina".equals(key)){
            List<String> list = this.getOschinaNews();
            if(list != null && list.size() > 0){
                reply(data, list.get(0));
                reply(data, list.get(1));
            }
        }
    }

    private List<String> getOschinaNews() {
        if(ConfigCache.mapCache.get("oschina_news") == null ||
                ConfigCache.mapCache.get("oschina_soft") == null){
            writeOschinaCacheByAPI();
        }
        List<String> list = null;
        //读取缓存
        TempCache t1 = (TempCache) ConfigCache.mapCache.get("oschina_news");
        TempCache t2 = (TempCache) ConfigCache.mapCache.get("oschina_soft");
        if(t1 != null && t2 != null){
            //如果缓存过期了，重新加载
            if(t1.isOverDeadLineTime()){
                writeOschinaCacheByAPI();
            }
            t1 = (TempCache) ConfigCache.mapCache.get("oschina_news");
            t2 = (TempCache) ConfigCache.mapCache.get("oschina_soft");
            list = new ArrayList<>(2);
            list.add(t1.getObject().toString());
            list.add(t2.getObject().toString());
        }
        return list;
    }

    /**
     * 读取oschina的数据，写入到缓存中
     */
    private void writeOschinaCacheByAPI(){
        try {
            //获取资讯消息
            String url = "https://www.oschina.net/action/apiv2/sub_list?token=d6112fa662bc4bf21084670a857fbd20";
            String userAgent = "OSChina.NET/1.0 (oscapp; 283; Android 6.0.1; X800+ Build/BEXCNFN5902012221S; 2ec82093-4c87-4be3-9cb1-bcd735aef591)";
            Connection.Response response = Jsoup.connect(url).userAgent(userAgent)
                    .header("AppToken", "799293cae64a1ef4c36a83d362f3f80cb9007505")
                    .method(Connection.Method.GET)
                    .ignoreContentType(true)
                    .execute();
            if(response.statusCode() == 200){
                if(log.isDebugEnabled()){
                    log.info("oschina api 数据："+response.body());
                }
                JSONObject root = JSON.parseObject(response.body());
                StringBuffer news = new StringBuffer("OSChina资讯：");
                StringBuffer softs = new StringBuffer("OSChina资讯：");
                if(root.containsKey("code") && root.getIntValue("code") == 1){//success
                    JSONObject result = root.getJSONObject("result");
                    if(result.containsKey("items") && result.getJSONArray("items").size() > 0){
                        JSONArray items = result.getJSONArray("items");
                        Iterator<Object> it = items.iterator();
                        int i = 1;
                        while(it.hasNext()){
                            String txt = it.next().toString();
                            JSONObject obj = JSONObject.parseObject(txt);
                            if(i <= 10){
                                if(obj.containsKey("title") && StringUtils.isNotBlank(obj.getString("title"))){
                                    news.append("\n"+i+"."+obj.getString("title")+"\n");
                                }
                                if(obj.containsKey("href") && StringUtils.isNotBlank(obj.getString("href"))){
                                    news.append("->链接："+obj.getString("href")+"\n");
                                }
                            }else{
                                if(obj.containsKey("title") && StringUtils.isNotBlank(obj.getString("title"))){
                                    softs.append("\n"+i+"."+obj.getString("title")+"\n");
                                }
                                if(obj.containsKey("href") && StringUtils.isNotBlank(obj.getString("href"))){
                                    softs.append("->链接："+obj.getString("href")+"\n");
                                }
                            }
                            i++;
                        }
                    }
                    //写入缓存
                    Date cacheTime = new Date();
                    long deadLineTime = cacheTime.getTime() + (1*60*60*1000);
                    TempCache temp1 = new TempCache(new Date(), new Date(deadLineTime), news.toString().substring(0, news.length() - 1));
                    TempCache temp2 = new TempCache(new Date(), new Date(deadLineTime), softs.toString().substring(0, softs.length() - 1));
                    ConfigCache.mapCache.put("oschina_news", temp1);
                    ConfigCache.mapCache.put("oschina_soft", temp2);
                }
            }else{
                log.error("oschina api 信息获取状态码："+response.statusCode());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 读取oschina的数据，写入到缓存中
     * 已标记为废弃
     */
    @Deprecated
    private void writeOschinaCache(){
        try {
            String text = WebUtil.getUrlResult("http://www.oschina.net/", "get");
            if(StringUtils.isNotBlank(text)){
                Document doc = Jsoup.parse(text);
                StringBuffer news = new StringBuffer("OSChina综合资讯：");
                //综合资讯
                List<Element> list = Xsoup.select(doc, "//div[@id='IndustryNews']/ul[@class='p1']/li/a").getElements();
                for(Element e : list){
                    String href = e.attr("href");
                    if(!"http".equals(href.substring(0,4))){
                        href = "http://www.oschina.net"+href;
                    }
                    news.append("\n"+e.text()+"\n->链接："+href+"\n");
                }
                StringBuffer softs = new StringBuffer("OSChina软件更新资讯：");
                //软件资讯
                list = Xsoup.select(doc, "//div[@id='ProjectNews']/ul[@class='p1']/li/a").getElements();
                for(Element e : list){
                    String href = e.attr("href");
                    if(!"http".equals(href.substring(0,4))){
                        href = "http://www.oschina.net"+href;
                    }
                    softs.append("\n"+e.text()+"\n->链接："+href+"\n");
                }
                log.info(news.toString());
                log.info(softs.toString());
                //写入缓存
                Date cacheTime = new Date();
                long deadLineTime = cacheTime.getTime() + (8*60*60*1000);
                TempCache temp1 = new TempCache(new Date(), new Date(deadLineTime), news.toString());
                TempCache temp2 = new TempCache(new Date(), new Date(deadLineTime), softs.toString());
                ConfigCache.mapCache.put("oschina_news", temp1);
                ConfigCache.mapCache.put("oschina_soft", temp2);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
