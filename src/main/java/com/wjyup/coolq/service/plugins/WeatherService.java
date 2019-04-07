package com.wjyup.coolq.service.plugins;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.eventbus.Subscribe;
import com.wjyup.coolq.entity.RequestData;
import com.wjyup.coolq.event.GroupMsgEvent;
import com.wjyup.coolq.event.PrivateMsgEvent;
import com.wjyup.coolq.eventbus.XEventBus;
import com.wjyup.coolq.service.ResolveMessageService;
import com.wjyup.coolq.util.LocalCache;
import com.wjyup.coolq.util.WebUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;

/**
 * 查询天气
 */
@Component
public class WeatherService extends ResolveMessageService implements InitializingBean {
    @Autowired
    private XEventBus eventBus;

    @Subscribe
    public void doit(PrivateMsgEvent event) {
        doit(event.getRequestData());
    }

    @Subscribe
    public void doit(GroupMsgEvent event) {
        doit(event.getRequestData());
    }

    private void doit(RequestData data) {
        String key = data.getMsg();
        if (key.startsWith("天气") && key.length() < 10) {
            String cityName = key.replace("天气", "").replace(" ", "").trim();
            String weatherByName = getWeatherByName(cityName);
            if (StringUtils.isNotBlank(weatherByName)) {
                reply(data, weatherByName);
            }
        }
    }

    /**
     * 查询天气接口
     * @param name 城市名称
     * @return 查询结果| 失败信息
     */
    private String getWeatherByName(String name) {
        try {
            String cache = (String) LocalCache.getCache(name);
            //优先读取缓存
            if (cache == null) {
                String url = "http://wthrcdn.etouch.cn/weather_mini?city="+ URLEncoder.encode(name, "UTF-8");
                String result = WebUtil.get(url);
                log.debug(result);
                if(StringUtils.isNotBlank(result)){
                    JSONObject jobj = JSON.parseObject(result);
                    Integer desc = jobj.getInteger("status");
                    if(desc == 1000){
                        StringBuilder str = new StringBuilder();
                        JSONObject jdata = jobj.getJSONObject("data");
                        str.append(String.format("城市:%s，温度：%s℃\n",jdata.getString("city"),jdata.getString("wendu")));
                        JSONArray dataArr = jdata.getJSONArray("forecast");
                        for(int i=0; i<dataArr.size(); i++){
                            JSONObject obj = dataArr.getJSONObject(i);
                            String fengli = (String) obj.get("fengli");
                            str.append(String.format("%s：%s %s %s %s %s\n",
                                    obj.get("date"),obj.get("type"),obj.get("low"), obj.get("high"),
                                    obj.get("fengxiang"), fengli.substring(9, fengli.length() - 3)));
                        }
                        str.append("感冒情况：").append(jdata.getString("ganmao"));
                        //加入cache
                        LocalCache.addCache(name, str.toString());
                        return str.toString();
                    } else {
                        throw new Exception("没有找到["+name+"]的天气信息！");
                    }
                } else {
                    throw new Exception("没有找到["+name+"]的天气信息！");
                }
            }
            return cache + "\nfor cache 12 hour";
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return "没有找到["+name+"]的天气信息！";
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        eventBus.register(this);
    }
}
