package com.wjyup.coolq.service;

import com.google.common.eventbus.Subscribe;
import com.wjyup.coolq.entity.RequestData;
import com.wjyup.coolq.event.PrivateMsgEvent;
import com.wjyup.coolq.eventbus.XEventBus;
import com.wjyup.coolq.util.CQSDK;
import com.wjyup.coolq.util.SpringContext;
import com.wjyup.coolq.vo.FriendListVO;
import com.wjyup.coolq.vo.GroupListVO;
import com.wjyup.coolq.vo.GroupMemberListVO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuService extends ResolveMessageService implements InitializingBean {
    private Logger log = LogManager.getLogger(this.getClass());

    @Autowired
    private XEventBus eventBus;

    @Override
    public void afterPropertiesSet() {
        eventBus.register(this);
    }

    @Subscribe
    public void privateMsg(PrivateMsgEvent event) {
        log.debug("================================{}", event.getRequestData().getMsg());
        String key = event.getRequestData().getMsg();
        RequestData data = event.getRequestData();
        //包含CQ码直接跳过
        if(key.contains("CQ:")) return;
        //我的群列表
        if("查看群列表".equals(key)){
            if(SpringContext.getConfigCache().isManger(event.getRequestData().getQQ())){
                List<GroupListVO> list = CQSDK.getGroupList();
                if (list != null && !list.isEmpty()) {
                    StringBuilder sb = new StringBuilder("共有").append(list.size()).append("个群\n");
                    for(GroupListVO vo : list){
                        sb.append(String.format("\n=>群号:%s\n=>群昵称:%s\n=>群创建者QQ:%s\n", vo.getGroup(), vo.getGroupNickName(), vo.getGroupOwner()));
                    }
                    reply(data, sb.substring(0, sb.length() - 1));
                }
            }
        }
        //我的好友列表
        if("查看好友列表".equals(key)){
            if(SpringContext.getConfigCache().isManger(data.getQQ())){
                List<FriendListVO> list = CQSDK.getFriendList();
                StringBuffer msg = new StringBuffer("共有"+list.size()+"个好友\n");
                for(FriendListVO vo : list){
                    msg.append("\n=>昵称："+vo.getName()+"\n");
                    msg.append("=>QQ："+vo.getQq()+"\n");
                }
                reply(data, msg.substring(0, msg.length() - 1));
            }
        }
        //查看指定群的成员列表
        if(key.indexOf("group:") == 0){
            if(SpringContext.getConfigCache().isManger(data.getQQ())){
                String group = key.trim().replace("group:", "");
                List<GroupMemberListVO> list = CQSDK.getGroupMemberList2(group.trim());
                StringBuffer msg = new StringBuffer("群号："+group+" 共有"+list.size()+"个成员\n");
                for(GroupMemberListVO vo : list){
                    msg.append("\n=>群名片："+vo.getName()+"\n");
                    msg.append("=>QQ："+vo.getQq()+"\n");
                    if(vo.getIsCreater() == 1){
                        msg.append("=>是该群的创建者\n");
                    }
                    if(vo.getIsManager() == 1){
                        msg.append("=>是该群管理员\n");
                    }
                }
                reply(data, msg.substring(0, msg.length() - 1));
            }
        }
    }
}
