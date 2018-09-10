package com.wjyup.coolq.vo;

import java.io.Serializable;

/**
 * 机器人设置VO
 * 
 * @author WJY
 *
 */
public class SettingVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String key;
	private String value;

	public SettingVO() {
	}

	public SettingVO(String key, String value) {
		super();
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
