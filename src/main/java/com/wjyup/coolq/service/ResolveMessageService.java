package com.wjyup.coolq.service;

import com.wjyup.coolq.entity.RequestData;
import com.wjyup.coolq.util.CQSDK;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 公共处理信息的类，只需要继承，并实现相应的方法即可，通过Spring管理，进行动态获取并调用doit方法
 * Created by WJY on 2017/8/1.
 */
public abstract class ResolveMessageService {
    public final static Logger log = LogManager.getLogger(ResolveMessageService.class);

    /**
     *
     * @param data
     */
    public abstract void doit(RequestData data) throws Exception;

    /**
     * 用于主动回复消息
     * @param data 收到的消息
     * @param message 回复的消息
     */
    protected void reply(RequestData data, String message){
        switch (data.getType()) {
            //私聊消息
            case 1:
                CQSDK.sendPrivateMsg(data.getQQ().toString(), message);
                break;
            //群消息
            case 2:
                CQSDK.sendGroupMsg(data.getGroup().toString(), message);
                break;
            //讨论组信息
            case 4:
                CQSDK.sendDiscussMsg(data.getGroup().toString(), message);
                break;
        }
    }
}
