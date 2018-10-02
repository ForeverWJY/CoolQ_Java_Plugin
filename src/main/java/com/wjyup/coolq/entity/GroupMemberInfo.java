package com.wjyup.coolq.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * QQ群成员信息
 * 
 * @author WJY
 *
 */
@Getter
@Setter
public class GroupMemberInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String nickName;// 昵称
	private String qunNickName;// 群昵称
	private String tab;// 标签
	private Long qq;// QQ
	private String sex;// 性别
	private Integer qqAge;// Q龄
	private String enterTime;// 入群时间
	private String level;// 等级（积分）
	private String endSayTime;// 最后发言时间

	public GroupMemberInfo() {
	}

	public GroupMemberInfo(String nickName, String qunNickName, String tab, Long qq, String sex, Integer qqAge,
			String enterTime, String level, String endSayTime) {
		super();
		this.nickName = nickName;
		this.qunNickName = qunNickName;
		this.tab = tab;
		this.qq = qq;
		this.sex = sex;
		this.qqAge = qqAge;
		this.enterTime = enterTime;
		this.level = level;
		this.endSayTime = endSayTime;
	}
}
