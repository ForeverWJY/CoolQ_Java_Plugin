package com.wjyup.coolq.vo;

import java.io.Serializable;

/**
 * 取陌生人信息
 * 
 * @author WJY
 *
 */
public class StrangerInfoVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer Status;// 状态：0-成功
	private Long QQ;
	private Integer Gender;// 性别：0-男，1-女
	private Integer Old;// 年龄
	private String Name;// 昵称

	public StrangerInfoVO() {
	}

	public Long getQQ() {
		return QQ;
	}

	public void setQQ(Long qQ) {
		QQ = qQ;
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

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public Integer getStatus() {
		return Status;
	}

	public void setStatus(Integer status) {
		Status = status;
	}

}
