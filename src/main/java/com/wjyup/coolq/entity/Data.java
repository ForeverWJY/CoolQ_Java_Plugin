package com.wjyup.coolq.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
/**
 * 消息实体类
 * @author WJY
 */
public class Data implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long QQ;
	private String Msg;//消息
	private String Fun;// 方法名
	private Integer Type;// 消息类型
	/*
 	代码(Type)	说明	    含有的子消息类型(SubType)
		1	私聊信息	    11/来自好友 1/来自在线状态 2/来自群 3/来自讨论组
		2	群消息	    1/普通消息 2/匿名消息 3/系统消息
		4	讨论组信息	目前固定为1
		11	上传群文件	目前固定为1
		101	群管理员变动	1/被取消管理员 2/被设置管理员
		102	群成员减少	1/群员离开 2/群员被踢 3/自己(即登录号)被踢
		103	群成员增加	1/管理员已同意 2/管理员邀请
		201	好友已添加	目前固定为1
		301	请求添加好友	目前固定为1
		302	请求添加群	1/他人申请入群 2/自己(即登录号)受邀入群
	 */
	
	private Integer Skn1;//置全群禁言开关，0/关闭 1/开启，默认为0
	private Integer Time;//置群成员禁言 禁言时间，单位:秒，默认为0(解禁)
	private Integer Count;//赞的次数
	private String File;//语音文件名,不带路径
	private String Format;//所需的语音文件格式，目前支持 mp3,amr,wma,m4a,spx,ogg,wav,flac
	private Long Group;//群号码

	public Data() {
	}
	
	//用于发送私聊、讨论组、群消息的方法
	public Data(Long qQ, String msg, String fun) {
		super();
		QQ = qQ;
		Msg = msg;
		Fun = fun;
	}
	
	//用于取登录QQ、取Cookies、取登录昵称、取CsrfToken方法
	public Data(String fun) {
		super();
		Fun = fun;
	}

	//用于发送赞的方法
	public Data(Long qQ, String fun, Integer count) {
		super();
		QQ = qQ;
		Fun = fun;
		Count = count;
	}
	
	//用于接收语音的方法
	public Data(String fun, String file, String format) {
		super();
		Fun = fun;
		File = file;
		Format = format;
	}
	
	//用于取成员信息的方法
	public Data(Long qQ, String fun, Long group) {
		super();
		QQ = qQ;
		Fun = fun;
		Group = group;
	}

	/**
	 * 转json字符串
	 * 
	 * @return
	 */
	public String toJson() {
		Gson g = new GsonBuilder().create();
		return g.toJson(this);
	}
	
	/**
	 * 插件直接调用方法后返回值需要用到，比如发消息给bubucom后直接返回消息
	 * @return
	 */
	public String toArrayJson(){
		Map<String, Object> map = new HashMap<String, Object>();
		ArrayList<Data> arr = new ArrayList<Data>();
		arr.add(this);
		map.put("data", arr);
		Gson g = new Gson();
		return g.toJson(map);
	}

	public Long getQQ() {
		return QQ;
	}

	public void setQQ(Long qQ) {
		QQ = qQ;
	}

	public String getMsg() {
		return Msg;
	}

	public void setMsg(String msg) {
		Msg = msg;
	}

	public String getFun() {
		return Fun;
	}

	public void setFun(String fun) {
		Fun = fun;
	}

	public Integer getType() {
		return Type;
	}

	public void setType(Integer type) {
		Type = type;
	}

	public Integer getSkn1() {
		return Skn1;
	}

	public void setSkn1(Integer skn1) {
		Skn1 = skn1;
	}

	public Integer getTime() {
		return Time;
	}

	public void setTime(Integer time) {
		Time = time;
	}

	public Integer getCount() {
		return Count;
	}

	public void setCount(Integer count) {
		Count = count;
	}

	public String getFile() {
		return File;
	}

	public void setFile(String file) {
		File = file;
	}

	public String getFormat() {
		return Format;
	}

	public void setFormat(String format) {
		Format = format;
	}

	public Long getGroup() {
		return Group;
	}

	public void setGroup(Long group) {
		Group = group;
	}

}
