package com.wjyup.coolq.util.service;

import com.wjyup.coolq.entity.RequestData;

public interface IMenuService extends IBaseService{
	/**
	 * 根据关键字进行过滤
	 * @param data 接收到的json
	 * @return 结果
	 */
	String listMenu(RequestData data);
	
	/**
	 * 获取oschina资讯
	 * @param key 聊天关键字
	 */
	void getOschinaNews();
}
