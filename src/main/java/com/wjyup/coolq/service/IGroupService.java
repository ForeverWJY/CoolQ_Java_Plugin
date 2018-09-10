package com.wjyup.coolq.service;

import com.wjyup.coolq.entity.GroupApplication;

import java.util.List;

public interface IGroupService {

	/**
	 * 获取申请列表
	 */
	List<GroupApplication> getApplicationList();
	
	/**
	 * 插入申请记录
	 */
	boolean add(GroupApplication application);
}
