package com.wjyup.coolq.util.service;

import java.util.List;

import com.wjyup.coolq.entity.RequestData;

public interface IMenuService extends IBaseService{
	/**
	 * 根据关键字进行过滤
	 * @param data 接收到的json
	 * @return 结果
	 */
	String listMenu(RequestData data);

	/**
	 * 根据城市名称查询天气信息，加入缓存
	 * @param name 城市名称
	 */
	String getWeatherByName(String name);
	
	/**
	 * 获取oschina资讯
	 * @param key 聊天关键字
	 * @return oschina_news 和 oschina_soft 字符串的集合
	 */
	List<String> getOschinaNews();
}
