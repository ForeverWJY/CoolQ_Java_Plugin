package com.wjyup.coolq.util;

import com.wjyup.coolq.entity.RequestData;
import org.apache.log4j.Logger;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * 处理消息的线程
 * @author WJY
 */
public class MessageHandle implements Runnable{
    private Logger log = Logger.getLogger("MessageHandle");
	
//	private IMenuService menuService = null;
	
	private RequestData data;
	
	public MessageHandle() {
	}
	
	public MessageHandle(RequestData requestData) {
		super();
		this.data = requestData;
		//使用Spring工具类手动注入
//		menuService = (IMenuService) SpringApplicationContextHolder.getBean("menuService");
	}

	@Override
	public void run() {
		String message = null;
		switch (data.getType()) {
		//私聊消息
		case 1:
			if(data.getSubType() == 11){//来自好友
			    
			}else if(data.getSubType() == 1){//来自在线状态 私聊
			    
			}else if(data.getSubType() == 2){//来自群 私聊
			    
			}else if(data.getSubType() == 3){//来自讨论组 私聊
			    
			}
//			message = menuService.listMenu(data);
//			if(StringUtils.isNotBlank(message)){
//				CQSDK.sendPrivateMsg(data.getQQ().toString(), message);
//			}
            resolveMsg();
			break;
		//群消息
		case 2:
			
			if(data.getSubType() == 1){//普通消息

			}else if(data.getSubType() == 2){//匿名消息

			}else if(data.getSubType() == 3){//系统消息

			}
            resolveMsg();
		    break;
		//讨论组信息
		case 4:
			
		    break;
		//上传群文件
		case 11:
		    
		    break;
		//群管理员变动
		case 101:
			if(data.getSubType() == 1){//被取消管理员
			    
			}else if(data.getSubType() == 2){//被设置管理员
			    
			}
		    break;
		//群成员减少
		case 102:
			if(data.getSubType() == 1){//群员离开
			    
			}else if(data.getSubType() == 2){//群员被踢
			    
			}else if(data.getSubType() == 3){//自己(即登录号)被踢
			    
			}
		    break;
		//群成员增加
		case 103:
			if(data.getSubType() == 1){//管理员已同意
			    
			}else if(data.getSubType() == 2){//管理员邀请
			    
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
		    break;
		default:
			break;
		}
	}

    /**
     * 新增加通过反射处理消息的方法
     */
	private void resolveMsg(){
        //遍历指定包下的所有类
        Set<Class<?>> list = null;
        try {
            list = ScanPackage.getClasses(ConfigCache.PLUGIN_PACKAGE_PATH);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        if(list != null){
            list.forEach(cls -> {
                try {
                    System.out.println(cls.getName());
                    Class c = Class.forName(cls.getName());
                    Object o = c.newInstance();
                    Method method = c.getDeclaredMethod("doit", RequestData.class);
                    method.invoke(o,data);
                }catch (NoSuchMethodException e){
                    log.error("没找到doit方法");
                } catch (Exception e) {
                    log.error(e.getMessage(),e);
                }

            });
        }
    }

	public RequestData getRequestData() {
		return data;
	}

	public void setRequestData(RequestData requestData) {
		this.data = requestData;
	}

}
