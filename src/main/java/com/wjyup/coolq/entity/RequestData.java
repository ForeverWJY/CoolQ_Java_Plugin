package com.wjyup.coolq.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
/**
 * 接收的消息
 * @author WJY
 *
 */
@Getter
@Setter
public class RequestData implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer Type;// 消息类型，即事件ID
	/*
	 	代码(Type)	说明		含有的子消息类型(SubType)
			1	私聊信息		11/来自好友 1/来自在线状态 2/来自群 3/来自讨论组
			2	群消息		1/普通消息 2/匿名消息 3/系统消息
			4	讨论组信息	目前固定为1
			11	上传群文件	目前固定为1
			101	群管理员变动	1/被取消管理员 2/被设置管理员
			102	群成员减少	1/群员离开 2/群员被踢 3/自己(即登录号)被踢
			103	群成员增加	1/管理员已同意 2/管理员邀请
			201	好友已添加	目前固定为1
			301	请求添加好友	目前固定为1
			302	请求添加群	1/他人申请入群 2/自己(即登录号)受邀入群
	 */
	private Integer SubType;// 消息子类型，一般为1
	private Long QQ;// 消息来源的QQ/操作者QQ
	private Long Group;// 消息来源的群号
	private Long Discuss;//消息来源的讨论组号
	private String Msg;// 消息内容，或加群/加好友事件的请求理由
	private Integer Font;// 未转义的字体代码
	private Long beingOperateQQ;//操作的QQ，比如加群时是哪个QQ申请的加群
	//key token相关校验
	private Long authTime;
	private String authToken;

	public RequestData() {
	}
}
