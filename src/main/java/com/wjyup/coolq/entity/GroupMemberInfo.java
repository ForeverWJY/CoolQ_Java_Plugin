package com.wjyup.coolq.entity;

import java.io.Serializable;

/**
 * QQ群成员信息
 * 
 * @author WJY
 *
 */
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

	public String getEndSayTime() {
		return endSayTime;
	}

	public String getEnterTime() {
		return enterTime;
	}

	public String getLevel() {
		return level;
	}

	public String getNickName() {
		return nickName;
	}

	public Long getQq() {
		return qq;
	}

	public Integer getQqAge() {
		return qqAge;
	}

	public String getQunNickName() {
		return qunNickName;
	}

	public String getSex() {
		return sex;
	}

	public String getTab() {
		return tab;
	}

	public void setEndSayTime(String endSayTime) {
		this.endSayTime = endSayTime;
	}

	public void setEnterTime(String enterTime) {
		this.enterTime = enterTime;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public void setQq(Long qq) {
		this.qq = qq;
	}

	public void setQqAge(Integer qqAge) {
		this.qqAge = qqAge;
	}

	public void setQunNickName(String qunNickName) {
		this.qunNickName = qunNickName;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public void setTab(String tab) {
		this.tab = tab;
	}

}
