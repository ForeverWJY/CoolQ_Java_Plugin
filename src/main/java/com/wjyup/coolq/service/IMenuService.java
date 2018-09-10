package com.wjyup.coolq.service;

import com.wjyup.coolq.entity.RequestData;

import java.util.List;

public interface IMenuService extends IBaseService {
	/**
	 * 根据关键字进行过滤
	 * @param data 接收到的json
	 * @return 结果
	 */
	String listMenu(RequestData data);

	/**
	 * 根据城市名称查询天气信息，数据缓存2小时
	 * @param name 城市名称
	 */
	String getWeatherByName(String name);
	
	/**
	 * 获取oschina资讯 数据缓存1小时
	 * @return oschina_news 和 oschina_soft 字符串的集合
	 */
	List<String> getOschinaNews();
	
	/**
	 * 获取知乎日报 数据缓存1小时
	 * @return
	 */
	String getZhiHuDaily();
}
