package com.wjyup.coolq.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.wjyup.coolq.entity.Data;

/**
 * CoolQ SDK
 * 
 * @author WJY
 */
public class CQSDK {

	private static Logger log = Logger.getLogger("CQSDK");

	/////////////////////// 静态API/////////////////////////

	/**
	 * 发送 @某人
	 */
	public static String sendAt(String qq) {
		if (StringUtils.isNotBlank(qq)) {
			if ("-1".equals(qq)) {
				qq = "all";
			}
			return "[CQ:at,qq=" + qq + "]";
		}
		return null;
	}

	/**
	 * 发送Emoji表情
	 */
	public static String sendEmoji(String id) {
		if (StringUtils.isNotBlank(id)) {
			return "[CQ:emoji,id=" + id + "]";
		}
		return null;
	}

	/**
	 * 发送表情
	 */
	public static String sendFace(String id) {
		if (StringUtils.isNotBlank(id)) {
			return "[CQ:face,id=" + id + "]";
		}
		return null;
	}

	/**
	 * 发送窗口抖动
	 */
	public static String sendShake() {
		return "[CQ:shake]";
	}

	/**
	 * 发送名片分享
	 */
	public static String sendCardShare(String id) {
		if (StringUtils.isNotBlank(id)) {
			return "[CQ:contact,type=qq,id=" + id + "]";
		}
		return null;
	}

	/**
	 * 发送图片
	 */
	public static String sendImage(String imgFile) {
		if (StringUtils.isNotBlank(imgFile)) {
			return "[CQ:image,file=" + imgFile + "]";
		}
		return null;
	}

	/**
	 * 发送音乐 音乐网站类型,目前支持 qq/QQ音乐 163/网易云音乐 xiami/虾米音乐，默认为qq
	 */
	public static String sendMusic(String songID, String type) {
		if (StringUtils.isNotBlank(songID)) {
			if (StringUtils.isBlank(type)) {
				type = "qq";
			}
			return "[CQ:music,id=" + songID + ",type=" + type + "]";
		}
		return null;
	}

	/**
	 * 发送大表情(原创表情)
	 */
	public static String sendBigFace(String id, String sid) {
		if (StringUtils.isNotBlank(id) && StringUtils.isNotBlank(sid)) {
			return "[CQ:bface,p=" + id + ",id=" + sid + "]";
		}
		return null;
	}

	/**
	 * 发送小表情
	 */
	public static String sendSmallFace(String id) {
		if (StringUtils.isNotBlank(id)) {
			return "[CQ:sface,id=" + id + "]";
		}
		return null;
	}

	/**
	 * 发送厘米秀
	 * 
	 * @param id
	 *            动作代号
	 * @param qq
	 *            双人动作的对象,非必须
	 * @param content
	 *            动作顺带的消息内容,不建议发送长文本
	 */
	public static String sendShow(String id, String qq, String content) {
		if (StringUtils.isNotBlank(id)) {
			StringBuffer ss = new StringBuffer("[CQ:show,id=" + id);
			if (StringUtils.isNotBlank(qq)) {
				ss.append(",qq=" + qq);
			}
			if (StringUtils.isNotBlank(content)) {
				ss.append(",content=" + content);
			}
			ss.append("]");
			return ss.toString();
		}
		return null;
	}

	/**
	 * 发送链接分享
	 * 
	 * @param url
	 *            点击卡片后跳转的网页地址
	 * @param title
	 *            可空,分享的标题，建议12字以内
	 * @param content
	 *            可空,分享的简介，建议30字以内
	 * @param picUrl
	 *            可空,分享的图片链接，留空则为默认图片
	 */
	public static String sendShare(String url, String title, String content, String picUrl) {
		if (StringUtils.isNotBlank(url)) {
			StringBuffer ss = new StringBuffer("[CQ:share,url=" + url);
			if (StringUtils.isNotBlank(title)) {
				ss.append(",title=" + title);
			}
			if (StringUtils.isNotBlank(content)) {
				ss.append(",content=" + content);
			}
			if (StringUtils.isNotBlank(picUrl)) {
				ss.append(",image=" + picUrl);
			}
			ss.append("]");
			return ss.toString();
		}
		return null;
	}

	/////////////////////// 动态API/////////////////////////
	/*
	 * 一般情况下返回状态码(0为成功) 详细说明请见 http://d.cqp.me/Pro/%E5%BC%80%E5%8F%91/Error
	 */
	
	/**
	 * 发送私聊信息
	 */
	public static String sendPrivateMsg(String qq,String message) {
		if(StringUtils.isNotBlank(qq) && StringUtils.isNotBlank(message)){
			Data data = new Data(Long.parseLong(qq), message,"sendPrivateMsg");
			String result = WebSocketUtil.sendSocketData(data.toJson());
			return result;
		}
		return null;
	}
	
	/**
	 * 发送群信息
	 */
	public static String sendGroupMsg(String group,String message) {
		if(StringUtils.isNotBlank(group) && StringUtils.isNotBlank(message)){
			Data data = new Data();
			data.setGroup(Long.parseLong(group));
			data.setMsg(message);
			data.setFun("sendGroupMsg");
			WebSocketUtil.sendSocketData(data.toJson());
		}
		return null;
	}
	
	/**
	 * 发送讨论组信息
	 */
	public static String sendDiscussMsg(String discuss,String message) {
		if(StringUtils.isNotBlank(discuss) && StringUtils.isNotBlank(message)){
			Data data = new Data();
			data.setGroup(Long.parseLong(discuss));
			data.setMsg(message);
			data.setFun("sendDiscussMsg");
			WebSocketUtil.sendSocketData(data.toJson());
		}
		return null;
	}
	
	/**
	 * 发送赞
	 */
	public static String sendLike(String qq,Integer count) {
		if(StringUtils.isNotBlank(qq)){
			if(count == null || (count < 0 || count > 10)){
				count = 1;
			}
			Data data = new Data(Long.parseLong(qq),"",count);
			String result = WebSocketUtil.sendSocketData(data.toJson());
			return result;
		}
		return null;
	}

	/**
	 * 取登录QQ
	 */
	public static String sendLike() {
		Data data = new Data("getLoginQQ");
		String result = WebSocketUtil.sendSocketData(data.toJson());
		return result;
	}
	
	/**
	 * 取Cookies
	 */
	public static String getCookies() {
		Data data = new Data("getCookies");
		String cookie = WebSocketUtil.sendSocketData(data.toJson());
		return cookie;
	}
	
	/**
	 * 取登录昵称
	 */
	public static String getLoginNick() {
		Data data = new Data("getLoginNick");
		String nickName = WebSocketUtil.sendSocketData(data.toJson());
		return nickName;
	}
}
