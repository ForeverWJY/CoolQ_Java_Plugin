package com.wjyup.coolq.vo;

import java.io.Serializable;

/**
 * 好友列表
 * 
 * @author WJY
 *
 */
public class FriendListVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;// 昵称
	private Long qq;// QQ

	public FriendListVO() {
	}

	public FriendListVO(String name, Long qq) {
		super();
		this.name = name;
		this.qq = qq;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getQq() {
		return qq;
	}

	public void setQq(Long qq) {
		this.qq = qq;
	}

}
