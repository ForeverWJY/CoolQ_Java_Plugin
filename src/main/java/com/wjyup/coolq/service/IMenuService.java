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
}
