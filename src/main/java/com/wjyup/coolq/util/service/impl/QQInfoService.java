package com.wjyup.coolq.util.service.impl;

import com.wjyup.coolq.entity.RequestData;
import com.wjyup.coolq.util.CQSDK;
import com.wjyup.coolq.util.ConfigCache;
import com.wjyup.coolq.util.service.ResolveMessageService;
import com.wjyup.coolq.vo.FriendListVO;
import com.wjyup.coolq.vo.GroupListVO;
import com.wjyup.coolq.vo.GroupMemberListVO;

import java.util.List;

/**
 * QQ相关信息查询
 * Created by WJY on 2017/8/1.
 */
public class QQInfoService extends ResolveMessageService{
    @Override
    public void doit(RequestData data) throws Exception {
        String key = data.getMsg();
        //我的群列表
        if(key.length() >= 5 && "查看群列表".equals(key)){
            if(ConfigCache.isManger(data.getQQ())){
                List<GroupListVO> list = CQSDK.getGroupList();
                StringBuffer msg = new StringBuffer("共有"+list.size()+"个群\n");
                for(GroupListVO vo : list){
                    msg.append("\n=>群号:"+vo.getGroup()+"\n");
                    msg.append("=>群昵称:"+vo.getGroupNickName()+"\n");
//						msg.append("=>群创建者QQ:"+vo.getGroupOwner()+"\n");
                }
                reply(data, msg.substring(0, msg.length() - 1));
            }
        }
        //我的好友列表
        if(key.length() >= 6 && "查看好友列表".equals(key)){
            if(ConfigCache.isManger(data.getQQ())){
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
        if(key.length() >= 5 && key.indexOf("group:") == 0){
            if(ConfigCache.isManger(data.getQQ())){
                String group = key.trim().replace("group:", "");
                List<GroupMemberListVO> list = CQSDK.getGroupMemberList2(group);
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
