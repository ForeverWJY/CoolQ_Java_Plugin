package com.wjyup.coolq.entity;

import java.util.Date;
/**
 * 天气缓存信息实体类
 * @author WJY
 */
public class WeatherInfo {
	private Date cacheTime;//天气更新时间
	private String json;
	public WeatherInfo(Date cacheTime, String json) {
		super();
		this.cacheTime = cacheTime;
		this.json = json;
	}
	public WeatherInfo() {
		super();
	}
	public Date getCacheTime() {
		return cacheTime;
	}
	public void setCacheTime(Date cacheTime) {
		this.cacheTime = cacheTime;
	}
	public String getJson() {
		return json;
	}
	public void setJson(String json) {
		this.json = json;
	}
}
