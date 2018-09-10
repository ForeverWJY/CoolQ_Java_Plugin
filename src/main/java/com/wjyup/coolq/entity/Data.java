package com.wjyup.coolq.entity;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 消息实体类
 * @author WJY
 */
public class Data implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long qq;
	private String msg;//消息
	private String fun;// 方法名
	private Integer type;// 消息类型
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
	
	private Integer skn1;//置全群禁言开关，0/关闭 1/开启，默认为0
	private Integer time;//置群成员禁言 禁言时间，单位:秒，默认为0(解禁)
	private Integer count;//赞的次数
	private String file;//语音文件名,不带路径
	private String format;//所需的语音文件格式，目前支持 mp3,amr,wma,m4a,spx,ogg,wav,flac
	private Long group;//群号码
	private Integer cache;//缓存

	public Data() {
	}
	
	//用于发送私聊、讨论组、群消息的方法
	public Data(Long qQ, String msg, String fun) {
		super();
		this.qq = qQ;
		this.msg = msg;
		this.fun = fun;
	}
	
	//用于取登录QQ、取Cookies、取登录昵称、取CsrfToken方法
	public Data(String fun) {
		super();
		this.fun = fun;
	}

	//用于发送赞的方法
	public Data(Long qQ, String fun, Integer count) {
		super();
		this.qq = qQ;
		this.fun = fun;
		this.count = count;
	}
	
	//用于接收语音的方法
	public Data(String fun, String file, String format) {
		super();
		this.fun = fun;
		this.file = file;
		this.format = format;
	}
	
	//用于取成员信息的方法
	public Data(Long qQ, String fun, Long group) {
		super();
		qq = qQ;
		this.fun = fun;
		this.group = group;
	}
	
	//用于接收图片信息的方法
	public Data(String fun, String file) {
		super();
		this.fun = fun;
		this.file = file;
	}
	
	//获取群成员信息
	public Data(Long qQ, String fun, Long group, Integer cache) {
		super();
		qq = qQ;
		this.fun = fun;
		this.group = group;
		this.cache = cache;
	}

	/**
	 * 转json字符串
	 * 
	 * @return
	 */
	public String toJson() {
		return JSON.toJSONString(this);
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
		return JSON.toJSONString(map);
	}

	public Long getQq() {
		return qq;
	}

	public void setQq(Long qq) {
		this.qq = qq;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getFun() {
		return fun;
	}

	public void setFun(String fun) {
		this.fun = fun;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getSkn1() {
		return skn1;
	}

	public void setSkn1(Integer skn1) {
		this.skn1 = skn1;
	}

	public Integer getTime() {
		return time;
	}

	public void setTime(Integer time) {
		this.time = time;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public Long getGroup() {
		return group;
	}

	public void setGroup(Long group) {
		this.group = group;
	}

	public Integer getCache() {
		return cache;
	}

	public void setCache(Integer cache) {
		this.cache = cache;
	}
}
