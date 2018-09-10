package com.wjyup.coolq.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 群申请列表
 * 
 * @author WJY
 */
public class GroupApplication implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private Integer type;// 类型
	private Integer subType;// 子类型
	private String qq;// qq
	private String group;// 群
	private String discuss;// 讨论组
	private String msg;// 消息
	private Integer status;//状态：-1：待审核，1：通过，2：驳回
	private Date createTime;//创建时间

	public GroupApplication() {
	}

	public GroupApplication(Integer id, Integer type, Integer subType, String qq, String group, String discuss,
			String msg) {
		super();
		this.id = id;
		this.type = type;
		this.subType = subType;
		this.qq = qq;
		this.group = group;
		this.discuss = discuss;
		this.msg = msg;
	}

	public String getDiscuss() {
		return discuss;
	}

	public String getGroup() {
		return group;
	}

	public Integer getId() {
		return id;
	}

	public String getMsg() {
		return msg;
	}

	public String getQq() {
		return qq;
	}

	public Integer getSubType() {
		return subType;
	}

	public Integer getType() {
		return type;
	}

	public void setDiscuss(String discuss) {
		this.discuss = discuss;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public void setSubType(Integer subType) {
		this.subType = subType;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
}
