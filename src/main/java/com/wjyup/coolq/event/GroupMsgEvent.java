package com.wjyup.coolq.event;

import com.wjyup.coolq.entity.RequestData;
import com.wjyup.coolq.eventbus.XEvent;
import lombok.Data;

/**
 * 群聊事件
 */
@Data
public class GroupMsgEvent implements XEvent {
    private RequestData requestData;

    public GroupMsgEvent() {
    }

    public GroupMsgEvent(RequestData requestData) {
        this.requestData = requestData;
    }
}
