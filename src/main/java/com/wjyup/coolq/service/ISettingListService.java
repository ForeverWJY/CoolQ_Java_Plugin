package com.wjyup.coolq.service;

import com.wjyup.coolq.vo.SettingVO;

/**
 * 机器人设置Service
 * @author WJY
 *
 */
public interface ISettingListService {

	/**
	 * 保存
	 * @param vo
	 * @return
	 */
	boolean add(SettingVO vo);
	
	/**
	 * 根据key获取对应的值
	 * @return
	 */
	SettingVO getSetting(String key);
	
	/**
	 * 根据key 修改value 的值
	 */
	boolean update(SettingVO vo);
}
