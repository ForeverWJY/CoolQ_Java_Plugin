package com.wjyup.coolq.util.service.impl;

import com.wjyup.coolq.entity.RequestData;
import com.wjyup.coolq.util.service.ResolveMessageService;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Repository;

import java.io.IOException;

/**
 * Created by WJY on 2017/8/1.
 */
@Repository
public class VIPVideoService extends ResolveMessageService{
    @Override
    public void doit(RequestData data) throws Exception {
        String key = data.getMsg();

        //VIP视频播放
        if(key.startsWith("播放")){
            //拆分
            String[] ss = key.split(" ");
            if(ss.length == 2 && StringUtils.isNotBlank(ss[1])
                    && ss[1].indexOf("http") == 0
                    && ss[1].contains("[CQ") == false){
                String result = vipShortUrl(ss[1]);
                if(result != null){
                    reply(data,result);
                }
            }
        }
    }

    /**
     * 用于返回VIP视频短网址
     * @param url
     * @return
     */
    private String vipShortUrl(String url){
        String result = null;
        if(StringUtils.isNotBlank(url)){
            try {
                Document doc = Jsoup.connect("https://zanlide.com/su/add?url="+url).timeout(5000).get();
                if(doc.hasText()){
                    result = doc.text();
                }
            } catch (IOException e) {
                log.error("添加短网址失败！网址："+url);
                log.error(e.getMessage(),e);
            }
        }
        return result;
    }
}
