package com.wjyup.coolq.util.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wjyup.coolq.entity.RequestData;
import com.wjyup.coolq.entity.WeatherInfo;
import com.wjyup.coolq.util.WebUtil;
import com.wjyup.coolq.util.service.ResolveMessageService;
import org.apache.commons.lang3.StringUtils;

import java.net.URLEncoder;
import java.util.Date;

/**
 * 天气插件
 * Created by WJY on 2017/8/1.
 */
public class WeatherService extends ResolveMessageService{

    @Override
    public void doit(RequestData data) throws Exception {
        String key = data.getMsg();
        //查询天气
        if(key.length() >= 2 && "天气".equals(key.substring(0, 2))){
            String cityName = key.substring(3, key.length());
            String result = getWeatherByName(cityName);
            if(StringUtils.isNotBlank(result)){
                reply(data, result);
            }
        }
    }

    private String getWeatherByName(String name) {
        StringBuffer str = new StringBuffer();
        try {
            //优先读取缓存
            WeatherInfo info = WebUtil.weatherInfo.get(name);
            if(info != null){
                long time = new Date().getTime() - info.getCacheTime().getTime();
                //缓存两小时
                if(time < 60*60*1000*2){
                    return info.getJson()+"\nfor cache 2 hours";
                }
            }
            //重新查询并写入到缓存
            String url = "https://zanlide.com/api/weather?city="+ URLEncoder.encode(name, "UTF-8");
            String result = WebUtil.getUrlResult(url, "get");
            log.info(result);
            if(StringUtils.isNotBlank(result)){
                JSONObject jobj = JSON.parseObject(result);
                Integer desc = jobj.getInteger("status");
                if(desc == 1000){
                    JSONObject jdata = jobj.getJSONObject("data");
                    str.append(String.format("城市:%s，温度：%s℃\n",jdata.getString("city"),jdata.getString("wendu")));
                    JSONArray dataArr = jdata.getJSONArray("forecast");
                    for(int i=0; i<dataArr.size(); i++){
                        JSONObject obj = dataArr.getJSONObject(i);
                        str.append(String.format("%s：%s %s %s %s\n",
                                obj.get("date"),obj.get("type"),obj.get("low"),
                                obj.get("high"),obj.get("fengxiang"),obj.get("fengli")));
                    }
                    str.append("感冒情况："+jdata.getString("ganmao"));
                    //加入cache
                    WeatherInfo weatherInfo = new WeatherInfo(new Date(), str.toString());
                    WebUtil.weatherInfo.put(name, weatherInfo);
                    return str.toString();
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return "没有找到["+name+"]的天气信息！";
        }
        return "没有找到["+name+"]的天气信息！";
    }
}
