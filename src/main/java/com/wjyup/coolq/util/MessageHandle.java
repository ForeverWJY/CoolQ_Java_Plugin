package com.wjyup.coolq.util;

import com.wjyup.coolq.entity.GroupApplication;
import com.wjyup.coolq.entity.RequestData;
import com.wjyup.coolq.service.IGroupService;
import com.wjyup.coolq.service.IMenuService;
import com.wjyup.coolq.service.ISettingListService;
import com.wjyup.coolq.service.impl.MenuService;
import com.wjyup.coolq.vo.SettingVO;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 处理消息的线程
 * @author WJY
 */
public class MessageHandle implements Runnable{
	private final Logger log = LogManager.getLogger(MessageHandle.class);
	
	private IMenuService menuService = null;
	private ISettingListService settingListService = null;
	private IGroupService groupService = null;
	
	private RequestData data;
	
	public MessageHandle() {
	}
	
	public MessageHandle(RequestData requestData) {
		super();
		this.data = requestData;
		//使用Spring工具类手动注入
		menuService = SpringContext.getBean(MenuService.class);
//		settingListService = SpringContext.getBean("settingListService");
//		groupService = SpringContext.getBean("groupService");
	}

	@Override
	public void run() {
		SettingVO vo = null;
		switch (data.getType()) {
		//私聊消息
		case 1:
			if(data.getSubType() == 11){//来自好友
			    
			}else if(data.getSubType() == 1){//来自在线状态 私聊
			    
			}else if(data.getSubType() == 2){//来自群 私聊
			    
			}else if(data.getSubType() == 3){//来自讨论组 私聊
			    
			}
			resolveMessage();
			break;
		//群消息
		case 2:
			
			if(data.getSubType() == 1){//普通消息

			}else if(data.getSubType() == 2){//匿名消息

			}else if(data.getSubType() == 3){//系统消息
			    
			}
			resolveMessage();
		    break;
		//讨论组信息
		case 4:
			resolveMessage();
		    break;
		//上传群文件
		case 11:
		    
		    break;
		//群管理员变动
		case 101:
			if(data.getSubType() == 1){//被取消管理员
			    
			}else if(data.getSubType() == 2){//被设置管理员
			    
			}
			resolveMessage();
		    break;
		//群成员减少
		case 102:
			if(data.getSubType() == 1){//群员离开
			    
			}else if(data.getSubType() == 2){//群员被踢
			    
			}else if(data.getSubType() == 3){//自己(即登录号)被踢
			    
			}
			resolveMessage();
			vo = settingListService.getSetting("group_leave");
			if(vo != null && StringUtils.isNotBlank(vo.getValue())){
				//获取群成员的昵称
				String nickName = CQSDK.queryNickNameByQQ(data.getBeingOperateQQ().toString());
				String result = null;
				if(StringUtils.isNotBlank(nickName)){
					result = String.format(vo.getValue(), data.getBeingOperateQQ().toString(), nickName);
				}else{
					result = String.format(vo.getValue(), data.getBeingOperateQQ().toString(), "");
				}
				CQSDK.sendGroupMsg(data.getGroup().toString(), result);
			}
		    break;
		//群成员增加
		case 103:
			if(data.getSubType() == 1){//管理员已同意
			    
			}else if(data.getSubType() == 2){//管理员邀请
			    
			}
			resolveMessage();
			if(data != null && data.getGroup() != null ){
				vo = settingListService.getSetting("group_welcome"+data.getGroup());
			}else{
				vo = settingListService.getSetting("group_welcome");
			}
			if(vo != null && StringUtils.isNotBlank(vo.getValue())){
				String result = String.format(vo.getValue(), CQSDK.sendAt(data.getBeingOperateQQ().toString()));
				CQSDK.sendGroupMsg(data.getGroup().toString(), result);
			}
		    break;
		//好友已添加
		case 201:
			
		    break;
		//请求添加好友
		case 301:
			
		    break;
		//请求添加群
		case 302:
		    if(data.getSubType() == 1){//他人申请入群
		    	
		    }else if(data.getSubType() == 2){//自己(即登录号)受邀入群
		    	
		    }
		    //记录
		    GroupApplication application = new GroupApplication(null, data.getType(), data.getSubType(), data.getQQ().toString(), data.getGroup().toString(), data.getDiscuss().toString(), data.getMsg());
		    groupService.add(application);
		    break;
		default:
			break;
		}
	}

	private void resolveMessage() {
		// 查询是否有缓存
		if(!ConfigCache.MSG_PLUGIN_LIST.isEmpty()){
			ConfigCache.MSG_PLUGIN_LIST.forEach(v -> {
				try{
					v.doit(data);
				}catch (Exception e){
					log.error(e.getMessage(),e);
				}
			});
		}
	}
}
