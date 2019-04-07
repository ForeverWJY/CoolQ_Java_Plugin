package com.wjyup.coolq.event;

import com.wjyup.coolq.entity.RequestData;
import com.wjyup.coolq.eventbus.XEvent;
import lombok.Data;

/**
 * 私聊事件
 */
@Data
public class PrivateMsgEvent implements XEvent {

    private RequestData requestData;

    public PrivateMsgEvent() {
    }

    public PrivateMsgEvent(RequestData requestData) {
        this.requestData = requestData;
    }
}
