package com.wjyup.coolq.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 群申请列表
 * 
 * @author WJY
 */
@Getter
@Setter
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
}
