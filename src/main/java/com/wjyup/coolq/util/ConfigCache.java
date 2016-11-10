package com.wjyup.coolq.util;

import java.util.HashMap;

/**
 * 静态配置
 * 
 * @author WJY
 */
public class ConfigCache {

	public static String WSHost;// websocket的host
	public static String WSPort;// websocket的port
	public static String PHP_WS_URL;//php接口的url

	//根据关键字存放缓存，比如：oschina_news
	public static HashMap<String, Object> mapCache = new HashMap<String, Object>(2);
	/*
	 * oschina_news oschina综合资讯
	 * oschina_soft oschina软件更新资讯
	 */
	
}
