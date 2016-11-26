package com.wjyup.coolq.vo;

import java.io.Serializable;

/**
 * 群成员列表，包含是否是创建者、是否是管理员、昵称、QQ
 * 
 * @author WJY
 *
 */
public class GroupMemberListVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;// 昵称
	private Long qq;// QQ
	private Integer isCreater;// 是否是创建者
	private Integer isManager;// 是否是管理员

	public GroupMemberListVO() {
	}

	public GroupMemberListVO(String name, Long qq) {
		super();
		this.name = name;
		this.qq = qq;
	}

	public GroupMemberListVO(String name, Long qq, Integer isCreater, Integer isManager) {
		super();
		this.name = name;
		this.qq = qq;
		this.isCreater = isCreater;
		this.isManager = isManager;
	}

	public Integer getIsCreater() {
		return isCreater;
	}

	public void setIsCreater(Integer isCreater) {
		this.isCreater = isCreater;
	}

	public Integer getIsManager() {
		return isManager;
	}

	public void setIsManager(Integer isManager) {
		this.isManager = isManager;
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
