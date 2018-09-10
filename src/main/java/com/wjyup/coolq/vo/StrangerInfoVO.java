package com.wjyup.coolq.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 取陌生人信息
 * 
 * @author WJY
 *
 */
@Getter
@Setter
public class StrangerInfoVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer Status;// 状态：0-成功
	private Long QQ;
	private Integer Gender;// 性别：0-男，1-女
	private Integer Old;// 年龄
	private String Name;// 昵称

	public StrangerInfoVO() {
	}

}
