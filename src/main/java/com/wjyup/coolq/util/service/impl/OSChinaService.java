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
import org.springframework.stereotype.Repository;
import us.codecraft.xsoup.Xsoup;

import java.util.*;

/**
 * OSChina资讯插件
 * Created by WJY on 2017/8/1.
 */
@Repository
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

    private List<String> getOschinaNews() throws Exception{
        //优先获取缓存的数据
        String oschinaNews = (String) TempCache.CACHE.get("oschina_news");
        String oschinaSoft = (String) TempCache.CACHE.get("oschina_soft");
        if(StringUtils.isBlank(oschinaNews) || StringUtils.isBlank(oschinaSoft)){
            writeOschinaCacheByAPI();
        }
        List<String> list = new ArrayList<>(2);
        list.add(TempCache.CACHE.get("oschina_news").toString());
        list.add(TempCache.CACHE.get("oschina_soft").toString());
        return list;
    }

    /**
     * 读取oschina的数据，写入到缓存中
     */
    private void writeOschinaCacheByAPI(){
        try {
            //获取资讯消息
            String url = "http://blog.wjyup.com/test/api/oschina/";
            String userAgent = "OSChina.NET/1.0 (oscapp; 283; Android 6.0.1; X800+ Build/BEXCNFN5902012221S; 2ec82093-4c87-4be3-9cb1-bcd735aef591)";
            Connection.Response response = Jsoup.connect(url).userAgent(userAgent)
                    .method(Connection.Method.GET)
                    .ignoreContentType(true)
                    .execute();
            if(response.statusCode() == 200){
                if(log.isDebugEnabled()){
                    log.info("oschina api 数据："+response.body());
                }
                JSONObject root = JSON.parseObject(response.body());
                StringBuilder news = new StringBuilder("OSChina资讯：");
                StringBuilder softs = new StringBuilder("OSChina资讯：");
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
                                    news.append("\n").append(i).append(".").append(obj.getString("title")).append("\n");
                                }
                                if(obj.containsKey("href") && StringUtils.isNotBlank(obj.getString("href"))){
                                    news.append("->链接：").append(obj.getString("href"));
                                }
                            }else{
                                if(obj.containsKey("title") && StringUtils.isNotBlank(obj.getString("title"))){
                                    softs.append("\n").append(i).append(".").append(obj.getString("title")).append("\n");
                                }
                                if(obj.containsKey("href") && StringUtils.isNotBlank(obj.getString("href"))){
                                    softs.append("->链接：").append(obj.getString("href"));
                                }
                            }
                            i++;
                        }
                    }
                    //写入缓存
                    TempCache.CACHE.put("oschina_news",news.deleteCharAt(news.length() - 1).toString());
                    TempCache.CACHE.put("oschina_soft", softs.deleteCharAt(softs.length() - 1).toString());
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
     * 已标记为废弃,使用：<code>writeOschinaCacheByAPI</code>
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
                TempCache.CACHE.put("oschina_news",news.deleteCharAt(news.length() - 1).toString());
                TempCache.CACHE.put("oschina_soft", softs.deleteCharAt(softs.length() - 1).toString());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
