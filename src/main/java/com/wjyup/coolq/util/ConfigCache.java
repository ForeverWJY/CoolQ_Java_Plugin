package com.wjyup.coolq.util;

import java.util.HashMap;
import java.util.List;

/**
 * 静态配置
 * 
 * @author WJY
 */
public class ConfigCache {

	public static String WSHost;// websocket的host
	public static String WSPort;// websocket的port
	public static String PHP_WS_URL;//php接口的url
	public static String COOLQ_IMAGE_PATH;//CoolQ 图片文件夹
	public static List<Long> MANAGER_QQ;//管理员QQ，使用逗号分隔

	//根据关键字存放缓存，比如：oschina_news
	public static HashMap<String, Object> mapCache = new HashMap<String, Object>(2);
	/*
	 * oschina_news oschina综合资讯
	 * oschina_soft oschina软件更新资讯
	 */
	
	//查看QQ是否是机器人管理员
	public static boolean isManger(Long qq){
		try {
			if(qq == null || qq == 0 || MANAGER_QQ == null || MANAGER_QQ.isEmpty()){
				return false;
			}
			for(Long q : MANAGER_QQ){
				if(q.longValue() == qq.longValue()){
					return true;
				}
			}
		} catch (Exception e) {
		}
		return false;
	}
}
