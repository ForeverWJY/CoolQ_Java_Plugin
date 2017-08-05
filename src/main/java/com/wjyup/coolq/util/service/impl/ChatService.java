package com.wjyup.coolq.util.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wjyup.coolq.entity.RequestData;
import com.wjyup.coolq.util.CQSDK;
import com.wjyup.coolq.util.WebUtil;
import com.wjyup.coolq.util.service.ResolveMessageService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

/**
 * 聊天
 * Created by WJY on 2017/8/5.
 */
@Repository
public class ChatService extends ResolveMessageService{
    @Override
    public void doit(RequestData data) throws Exception {
        String msg = data.getMsg();
        if(StringUtils.isNotBlank(msg) && msg.startsWith("d ") && msg.length() > 3){
            String str = msg.substring(2);
            String result = chat(str);
            if(result != null){
                reply(data,result);
                String voice = voice(result);
                if(voice != null){
                    reply(data,voice);
                }

            }
        }
    }

    /**
     * 文字聊天
     * @param msg
     * @return
     * @throws Exception
     */
    private String chat(String msg) throws Exception{
        String result = WebUtil.get("https://chat.1sls.cc/v2/?type=9&id=123456&msg="+msg);
        if(StringUtils.isNotBlank(result)){
            JSONObject obj1 = JSON.parseObject(result);
            int status = obj1.getInteger("Status");
            if(status == 200){
                String Msg = obj1.getString("Msg");
                if(obj1.containsKey("Data")){
                    String Data = obj1.getString("Data");
                    Msg += CQSDK.sendImage(Data);
                }
                return Msg;
            }
        }
        return null;
    }

    /**
     * 文字转语音
     * @param msg 文字
     * @return
     * @throws Exception
     */
    private String voice(String msg)throws Exception{
        String result = WebUtil.get("https://chat.1sls.cc/v2/voice.php?type=0&id=123456&rid=123456&speed=9&pitch=9&volume=15&msg="+msg);
        if(StringUtils.isNotBlank(result)){
            JSONObject obj1 = JSON.parseObject(result);
            int status = obj1.getInteger("Status");
            String url = obj1.getString("Url");
            if(status == 200 && StringUtils.isNotBlank(url)){
                return CQSDK.sendVoice("https://chat.1sls.cc/v2/"+url);
            }
        }
        return null;
    }
}
