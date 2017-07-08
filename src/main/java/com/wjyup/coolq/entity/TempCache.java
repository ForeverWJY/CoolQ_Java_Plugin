package com.wjyup.coolq.entity;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * 缓存实体类
 *
 * @author WJY
 */
public class TempCache implements Serializable {

	private static final long serialVersionUID = 1L;

	private Date cacheTime;// 加入缓存的时间
	private Date deadLineTime;// 缓存过期时间
	private Object object;// 缓存对象

	public TempCache() {
		super();
	}

	public TempCache(Date cacheTime, Date deadLineTime, Object object) {
		super();
		this.cacheTime = cacheTime;
		this.deadLineTime = deadLineTime;
		this.object = object;
	}

	public Date getCacheTime() {
		return cacheTime;
	}

	public Date getDeadLineTime() {
		return deadLineTime;
	}

	public Object getObject() {
		return object;
	}

	public void setCacheTime(Date cacheTime) {
		this.cacheTime = cacheTime;
	}

	public void setDeadLineTime(Date deadLineTime) {
		this.deadLineTime = deadLineTime;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	/**
	 * 是否超过过期时间
	 * @return过期：false  没过期：true
	 */
	public boolean isOverDeadLineTime(){
		long deadTime = deadLineTime.getTime();
		long nowTime = System.currentTimeMillis();
		return nowTime > deadTime;
	}

	/**
	 * 数据缓存一天
	 * 过程：取缓存日期的是今年的第多少天，先比较年份，再比较开始日期和现在日期的大小
	 * @return 过期：false  没过期：true
	 */
	public boolean isExpired(){
		if(getCacheTime() == null) return true;
		Calendar start = Calendar.getInstance();
		start.setTime(getCacheTime());
		Calendar now = Calendar.getInstance();
		if(now.get(Calendar.YEAR) > start.get(Calendar.YEAR)){
			return true;
		}else if(now.get(Calendar.DAY_OF_YEAR) > start.get(Calendar.DAY_OF_YEAR)){
			return true;
		}
		return false;
	}

}
