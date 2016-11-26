package com.wjyup.coolq.vo;

import java.io.Serializable;

/**
 * 群成员信息VO
 * 
 * @author WJY
 *
 */
public class GroupMemberInfoVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer Status;// 状态：0-成功
	private String Group;//群号
	private String QQ;//QQ
	private String Name;//昵称
	private String Card;//群名片
	private Integer Gender;// 性别
	private Integer Old;// 年龄
	private String City;// 城市
	private Long JoinTime;// 加群时间
	private Long lastTime;// 最后发言时间
	private Integer Power;// 管理权限
	private String Tip;//专属头衔
	private String Level;// 等级
	private Integer InBlackList;// 不良记录成员
	private Integer AllowChangeCard;// 允许修改群名片吗
	private Integer TipExpireTime;//专属头衔过期时间

	public GroupMemberInfoVO() {
	}

	public String getGroup() {
		return Group;
	}

	public void setGroup(String group) {
		Group = group;
	}

	public String getQQ() {
		return QQ;
	}

	public void setQQ(String qQ) {
		QQ = qQ;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getCard() {
		return Card;
	}

	public void setCard(String card) {
		Card = card;
	}

	public Integer getGender() {
		return Gender;
	}

	public void setGender(Integer gender) {
		Gender = gender;
	}

	public Integer getOld() {
		return Old;
	}

	public void setOld(Integer old) {
		Old = old;
	}

	public String getCity() {
		return City;
	}

	public void setCity(String city) {
		City = city;
	}

	public Long getJoinTime() {
		return JoinTime;
	}

	public void setJoinTime(Long joinTime) {
		JoinTime = joinTime;
	}

	public Long getLastTime() {
		return lastTime;
	}

	public void setLastTime(Long lastTime) {
		this.lastTime = lastTime;
	}

	public Integer getPower() {
		return Power;
	}

	public void setPower(Integer power) {
		Power = power;
	}

	public String getTip() {
		return Tip;
	}

	public void setTip(String tip) {
		Tip = tip;
	}

	public String getLevel() {
		return Level;
	}

	public void setLevel(String level) {
		Level = level;
	}

	public Integer getInBlackList() {
		return InBlackList;
	}

	public void setInBlackList(Integer inBlackList) {
		InBlackList = inBlackList;
	}

	public Integer getAllowChangeCard() {
		return AllowChangeCard;
	}

	public void setAllowChangeCard(Integer allowChangeCard) {
		AllowChangeCard = allowChangeCard;
	}

	public Integer getTipExpireTime() {
		return TipExpireTime;
	}

	public void setTipExpireTime(Integer tipExpireTime) {
		TipExpireTime = tipExpireTime;
	}

	public Integer getStatus() {
		return Status;
	}

	public void setStatus(Integer status) {
		Status = status;
	}

}
