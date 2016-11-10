package com.wjyup.coolq.entity;

import java.io.Serializable;
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

}
