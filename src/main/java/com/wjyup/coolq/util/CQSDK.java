package com.wjyup.coolq.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Connection.Method;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.wjyup.coolq.entity.CQImageInfo;
import com.wjyup.coolq.entity.Data;
import com.wjyup.coolq.vo.FriendListVO;
import com.wjyup.coolq.vo.GroupListVO;
import com.wjyup.coolq.vo.GroupMemberInfoVO;
import com.wjyup.coolq.vo.GroupMemberListVO;
import com.wjyup.coolq.vo.StrangerInfoVO;

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
	 *  @param imgFile 图片路径，可使用网络图片和本地图片．使用本地图片时需在路径前加入 file://
	 */
	public static String sendImage(String imgFile) {
		if (StringUtils.isNotBlank(imgFile)) {
			return "[CQ:image,file=" + StringEscapeUtils.escapeJava(imgFile) + "]";
		}
		return null;
	}

	/**
	 * 发送位置分享(location)
	 * @param lat 纬度
	 * @param lon 经度
	 * @param zoom 放大倍数，可空，默认为 15
	 * @param title 地点名称，建议12字以内
	 * @param content 地址，建议20字以内
	 * @return string CQ码_位置分享
	 */
	public static String sendLocation(double lat, double lon, int zoom, String title, String content){
		String format = "[CQ:location,lat=%1$f,lon=%2$f,zoom=%3$d,title=%4$s,content=%5$s]";
		return String.format(format,lat,lon,zoom,title,content);
	}

	/**
	 * 发送音乐 音乐网站类型,目前支持 qq/QQ音乐 163/网易云音乐 xiami/虾米音乐，默认为qq
	 */
	public static String sendMusic(String songID, String type, boolean newStyle) {
		if (StringUtils.isNotBlank(songID)) {
			if (StringUtils.isBlank(type)) {
				type = "qq";
			}
			int style = newStyle ? 1 : 0;
			return "[CQ:music,id=" + songID + ",type=" + type + ",style="+ style +"]";
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
	 * 发送语音
	 */
	public static String sendVoice(String fileName) {
		if (StringUtils.isNotBlank(fileName)) {
			return "[CQ:record,file=" + fileName + "]";
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
				ss.append(",title=" + StringEscapeUtils.escapeJava(title));
			}
			if (StringUtils.isNotBlank(content)) {
				ss.append(",content=" + StringEscapeUtils.escapeJava(content));
			}
			if (StringUtils.isNotBlank(picUrl)) {
				ss.append(",image=" + StringEscapeUtils.escapeJava(picUrl));
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
			return WebSocketUtil.sendSocketData(data.toJson());
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
			return WebSocketUtil.sendSocketData(data.toJson());
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
	public static String getLoginQQ() {
		Data data = new Data("getLoginQQ");
		String result = WebSocketUtil.sendSocketData(data.toJson());
		if(StringUtils.isNotBlank(result)){
			JSONObject jobj = JSONObject.parseObject(result);
			Integer status = jobj.getInteger("Status");
			if(status == 0){
				return jobj.getString("Result");
			}
			return null;
		}
		return null;
	}
	
	/**
	 * 取Cookies
	 */
	public static Map<String, String> getCookies() {
		Map<String, String> map = new HashMap<>();
		Data data = new Data("getCookies");
		String cookie = WebSocketUtil.sendSocketData(data.toJson());
		if(StringUtils.isNotBlank(cookie)){
			JSONObject jobj = JSONObject.parseObject(cookie);
			Integer status = jobj.getInteger("status");
			if(status == 0){
				String result = jobj.getString("Result");
				result = result.replaceAll(" ", "");
				String[] res = result.split(";");
				for(String str : res){
					String[] temp = str.split("=");
					if(temp.length == 2){
						map.put(temp[0], temp[1]);
					}
				}
				return map;
			}
		}
		return null;
	}
	
	/**
	 * 取登录昵称
	 */
	public static String getLoginNick() {
		Data data = new Data("getLoginNick");
		String nickName = WebSocketUtil.sendSocketData(data.toJson());
		if(StringUtils.isNotBlank(nickName)){
			JSONObject jobj = JSONObject.parseObject(nickName);
			Integer status = jobj.getInteger("status");
			if(status == 0){
				return jobj.getString("Result");
			}
			return null;
		}
		return null;
	}
	
	/**
	 * 取CsrfToken
	 */
	public static String getCsrfToken(){
		Data data = new Data("getCsrfToken");
		String info = WebSocketUtil.sendSocketData(data.toJson());
		if(StringUtils.isNotBlank(info)){
			JSONObject jobj = JSONObject.parseObject(info);
			Integer status = jobj.getInteger("status");
			if(status == 0){
				return  jobj.getString("Result");
			}
			return null;
		}
		return null;
	}
	
	/**
	 * 取群成员信息
	 */
	public static GroupMemberInfoVO getGroupMemberInfo(String group,String qq, boolean cache){
		if(StringUtils.isNotBlank(group) && StringUtils.isNotBlank(qq)){
			JSONObject jobj = new JSONObject();
			jobj.put("fun", "getGroupMemberInfo");
			jobj.put("Group", group);
			jobj.put("QQ", qq);
			jobj.put("Cache", booleanToInteger(cache));
			String groupMemberInfo = WebSocketUtil.sendSocketData(jobj.toJSONString());
			//System.out.println(groupMemberInfo);
			if(StringUtils.isNotBlank(groupMemberInfo)){
				Gson g = new Gson();
				GroupMemberInfoVO infoVO = g.fromJson(groupMemberInfo, GroupMemberInfoVO.class);
				if(infoVO.getStatus() == 0){
					return infoVO;
				}
				return null;
			}
			return null;
		}
		return null;
	}
	
	/**
	 * 取陌生人信息
	 */
	public static StrangerInfoVO getStrangerInfo(String qq, boolean cache){
		if(StringUtils.isNotBlank(qq)){
			JSONObject jobj = new JSONObject();
			jobj.put("fun", "getStrangerInfo");
			jobj.put("QQ", qq);
			jobj.put("Cache", booleanToInteger(cache));
			String info = WebSocketUtil.sendSocketData(jobj.toJSONString());
			if(StringUtils.isBlank(info)){
				return null;
			}
			Gson g = new Gson();
			StrangerInfoVO infoVO = g.fromJson(info, StrangerInfoVO.class);
			if(infoVO.getStatus() == 0){
				return infoVO;
			}
			return null;
		}
		return null;
	}
	
	/**
	 * 其他_字体转换
	 * @return 返回带有数据的Json文本
	 */
	public static String GetFontInfo(String id){
		if(StringUtils.isNotBlank(id)){
			JSONObject obj = new JSONObject();
			obj.put("fun", "GetFontInfo");
			obj.put("ID", id);
			String result = WebSocketUtil.sendSocketData(obj.toJSONString());
			return result;
		}
		return null;
	}
	
	/**
	 * 其他_转换_文本到匿名（获取匿名人的消息，用于禁言）
	 * @return 返回带有数据的Json文本
	 */
	public static String GetAnonymousInfo(String source){
		if(StringUtils.isNotBlank(source)){
			JSONObject obj = new JSONObject();
			obj.put("fun", "GetAnonymousInfo");
			obj.put("source", source);
			String result = WebSocketUtil.sendSocketData(obj.toJSONString());
			return result;
		}
		return null;
	}
	
	/**
	 * 其他_转换_文本到群文件
	 * @return 返回带有数据的Json文本
	 */
	public static String GetFileInfo(String source){
		if(StringUtils.isNotBlank(source)){
			JSONObject obj = new JSONObject();
			obj.put("fun", "GetFileInfo");
			obj.put("source", source);
			String result = WebSocketUtil.sendSocketData(obj.toJSONString());
			return result;
		}
		return null;
	}
	
	/**
	 * 置成员移除
	 */
	public static String setGroupKick(String group, String qq, boolean refuse){
		if(StringUtils.isNotBlank(group) && StringUtils.isNotBlank(qq)){
			JSONObject obj = new JSONObject();
			obj.put("fun", "setGroupKick");
			obj.put("Group", group);
			obj.put("QQ", qq);
			obj.put("RefuseJoin", refuse);
			String result = WebSocketUtil.sendSocketData(obj.toJSONString());
			return result;
		}
		return null;
	}
	
	/**
	 * 置成员禁言
	 */
	public static String setGroupBan(String group, String qq, Integer time){
		if(StringUtils.isNotBlank(group) && StringUtils.isNotBlank(qq) && time != null){
			JSONObject obj = new JSONObject();
			obj.put("fun", "setGroupBan");
			obj.put("Group", group);
			obj.put("QQ", qq);
			obj.put("Time", time);//0为解除禁言
			String result = WebSocketUtil.sendSocketData(obj.toJSONString());
			return result;
		}
		return null;
	}
	
	/**
	 * 置群管理员
	 * 真为设置管理员,假为取消管理员
	 */
	public static String setGroupAdmin(String group, String qq, boolean become){
		if(StringUtils.isNotBlank(group) && StringUtils.isNotBlank(qq)){
			JSONObject obj = new JSONObject();
			obj.put("fun", "setGroupAdmin");
			obj.put("Group", group);
			obj.put("QQ", qq);
			obj.put("Become", booleanToInteger(become));
			String result = WebSocketUtil.sendSocketData(obj.toJSONString());
			return result;
		}
		return null;
	}
	
	/**
	 * 置全群禁言
	 * 真为开启,假为关闭
	 */
	public static String setGroupWholeBan(String group, boolean isGag){
		if(StringUtils.isNotBlank(group)){
			JSONObject obj = new JSONObject();
			obj.put("fun", "setGroupWholeBan");
			obj.put("Group", group);
			obj.put("IsGag", booleanToInteger(isGag));
			String result = WebSocketUtil.sendSocketData(obj.toJSONString());
			return result;
		}
		return null;
	}
	
	/**
	 * 置群匿名设置
	 * 真为开启,假为关闭
	 */
	public static String setGroupAnonymous(String group, boolean open){
		if(StringUtils.isNotBlank(group)){
			JSONObject obj = new JSONObject();
			obj.put("fun", "setGroupAnonymous");
			obj.put("Group", group);
			obj.put("Open", booleanToInteger(open));
			String result = WebSocketUtil.sendSocketData(obj.toJSONString());
			return result;
		}
		return null;
	}
	
	/**
	 * 置群成员名片
	 */
	public static String setGroupCard(String group, String qq, String card){
		if(StringUtils.isNotBlank(group) && StringUtils.isNotBlank(qq) && StringUtils.isNotBlank(card)){
			JSONObject obj = new JSONObject();
			obj.put("fun", "setGroupCard");
			obj.put("Group", group);
			obj.put("QQ", qq);
			obj.put("Card", card);//为空时清空群名片
			String result = WebSocketUtil.sendSocketData(obj.toJSONString());
			return result;
		}
		return null;
	}
	
	/**
	 * 置讨论组退出
	 */
	public static String setDiscussLeave(String discuss){
		if(StringUtils.isNotBlank(discuss)){
			JSONObject obj = new JSONObject();
			obj.put("fun", "setDiscussLeave");
			obj.put("Group", discuss);
			String result = WebSocketUtil.sendSocketData(obj.toJSONString());
			return result;
		}
		return null;
	}
	
	/**
	 * 置群添加请求
	 */
	public static String setGroupAddRequest(String responseFlag, Integer subType, Integer type, String Msg){
		if(StringUtils.isNotBlank(responseFlag) && subType != null && type != null){
			JSONObject obj = new JSONObject();
			obj.put("fun", "setGroupAddRequest");
			obj.put("responseFlag", responseFlag);
			obj.put("subtype", subType);//  1/群添加,2/群邀请
			obj.put("type", type);//  1/通过,2/拒绝
			obj.put("Msg", Msg);// 拒绝时的理由
			String result = WebSocketUtil.sendSocketData(obj.toJSONString());
			return result;
		}
		return null;
	}
	
	/**
	 * 置匿名群员禁言
	 */
	public static String setGroupAnonymousBan(String group, String anonymous, Integer type, Integer time){
		if(StringUtils.isNotBlank(group) && StringUtils.isNotBlank(anonymous) && time != null){
			JSONObject obj = new JSONObject();
			obj.put("fun", "setGroupAnonymousBan");
			obj.put("Group", group);
			obj.put("Anonymous", anonymous);//  1/群添加,2/群邀请
			obj.put("Time", time);//  1/通过,2/拒绝
			String result = WebSocketUtil.sendSocketData(obj.toJSONString());
			return result;
		}
		return null;
	}
	
	/**
	 * 置好友添加请求
	 */
	public static String setFriendAddRequest(String responseFlag, Integer type, String name){
		if(StringUtils.isNotBlank(responseFlag) && StringUtils.isNotBlank(name) && type != null){
			JSONObject obj = new JSONObject();
			obj.put("fun", "setFriendAddRequest");
			obj.put("responseFlag", responseFlag);
			obj.put("Type", type);// 1/通过,2/拒绝
			obj.put("Name", name);//备注
			String result = WebSocketUtil.sendSocketData(obj.toJSONString());
			return result;
		}
		return null;
	}
	
	/**
	 * 置群成员专属头衔
	 */
	public static String setGroupSpecialTitle(String group, String qq, String tip, Integer time){
		if(StringUtils.isNotBlank(group) && StringUtils.isNotBlank(qq) && StringUtils.isNotBlank(tip) && time != null){
			JSONObject obj = new JSONObject();
			obj.put("fun", "setGroupSpecialTitle");
			obj.put("Group", group);
			obj.put("QQ", qq);
			obj.put("Tip", tip);//头衔名称
			obj.put("Time", time);//过期时间
			String result = WebSocketUtil.sendSocketData(obj.toJSONString());
			return result;
		}
		return null;
	}
	
	
	/**
	 * 获取群列表
	 * url ： http://qun.qq.com/cgi-bin/qun_mgr/get_group_list
	 * method : post
	 * parameters : bkn
	 * getGroupList 方法
	 */
	@Deprecated
	public static List<GroupListVO> getGroupList_old(){
		try {
			List<GroupListVO> list = new ArrayList<>();
			Map<String, String> data = new HashMap<>();
			data.put("bkn", getCsrfToken());
			Map<String, String> cookies = getCookies();
			String result = WebUtil.fetch("http://qun.qq.com/cgi-bin/qun_mgr/get_group_list", Method.POST, data, cookies);
			if(result != null){
				//System.out.println(result);
				JSONArray jarr = JSONObject.parseObject(result).getJSONArray("manage");
				GroupListVO listVO = null;
				for(int a=0; a<jarr.size(); a++){
					JSONObject obj = jarr.getJSONObject(a);
					listVO = new GroupListVO(obj.getLong("gc"), obj.getString("gn"), obj.getLong("owner"));
					list.add(listVO);
				}
				return list;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
	
	/**
	 * 获取好友列表的方法
	 * url : http://qun.qq.com/cgi-bin/qun_mgr/get_friend_list
	 * method : post
	 * parameters : bkn
	 */
	@Deprecated
	public static List<FriendListVO> getFriendList_old(){
		try {
			List<FriendListVO> voList = new ArrayList<>();
			Map<String, String> data = new HashMap<>();
			data.put("bkn", getCsrfToken());
			Map<String, String> cookies = getCookies();
			String result = WebUtil.fetch("http://qun.qq.com/cgi-bin/qun_mgr/get_friend_list", Method.POST, data, cookies);
			if(result != null){
//				System.out.println(result);
				JSONObject jobj = JSONObject.parseObject(result).getJSONObject("result");
				Iterator<String> iter = jobj.keySet().iterator();
				while(iter.hasNext()){
					String key = iter.next();
					JSONObject jsonObject = jobj.getJSONObject(key);
					jsonObject.getString("gname");//分组名称
					JSONArray jarr = jsonObject.getJSONArray("mems");//分组下的成员列表
					if(jarr != null && jarr.size() > 0){
						for(int i=0; i<jarr.size(); i++){
							JSONObject obj  = jarr.getJSONObject(i);
							voList.add(new FriendListVO(obj.getString("name"), obj.getLong("uin")));
						}
						return voList;
					}
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
	
	/**
	 * 获取群成员列表方法1 （获取的群成员昵称可能不完整，如需完整昵称请使用 获取群成员列表方法2）
	 * url : http://qun.qq.com/cgi-bin/qun_mgr/search_group_members
	 * method : post
	 * parameters : 
	 *   gc => 群号
	 *   st => 0
	 *   end => 2000 查询2000人，默认值20
	 *   sort=> 0
	 *   bkn => csrtoken
	 */
	@Deprecated
	public static List<GroupMemberInfoVO> getGroupMemberList1(String group){
		try {
			List<GroupMemberInfoVO> voList = new ArrayList<>(0);
			Map<String, String> data = new HashMap<>();
			data.put("bkn", getCsrfToken());
			data.put("gc", group);
			data.put("st", "0");
			data.put("end", "2000");
			data.put("sort", "0");
			Map<String, String> cookies = getCookies();
			String result = WebUtil.fetch("http://qun.qq.com/cgi-bin/qun_mgr/search_group_members", Method.POST, data, cookies);
			if(result != null){
//				System.out.println(result);
				JSONObject jobj = JSONObject.parseObject(result);
				//等级列表
				JSONObject level = jobj.getJSONObject("levelname");
				HashMap<String, String> levelMap = new HashMap<>(level.size());
				Iterator<String> it = level.keySet().iterator();
				while(it.hasNext()){
					String key = it.next();
					levelMap.put(key, level.getString(key));
				}
				JSONArray jarr = jobj.getJSONArray("mems");
				GroupMemberInfoVO vo = null;
				voList = new ArrayList<>(jarr.size());
				for(int a=0; a<jarr.size(); a++){
					JSONObject obj = jarr.getJSONObject(a);
					vo = new GroupMemberInfoVO();
					vo.setGroup(group);
					vo.setQQ(obj.getString("uin"));
					vo.setCard(obj.getString("card"));
					vo.setName(obj.getString("nick"));
					vo.setGender(obj.getInteger("g"));
					vo.setLastTime(obj.getLong("last_speak_time"));
					vo.setPower(obj.getInteger("role"));
					vo.setOld(obj.getInteger("qage"));
					vo.setLevel(levelMap.get(obj.getJSONObject("lv").getString("level")));
					voList.add(vo);
				}
				return voList;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
	
	/**
	 * 获取群成员列表方法2
	 * url :　http://qun.qzone.qq.com/cgi-bin/get_group_member?uin=1066231345&groupid=215400054&random=0.15297316415049034&g_tk=277066193
	 * mehod : get
	 * parameters :
	 * 	uin = QQ号
	 *  groupid = 群号
	 *  random = 随机数
	 *  g_tk = csrtoken
	 */
	@Deprecated
	public static List<GroupMemberListVO> getGroupMemberList2(String group){
		try {
			List<GroupMemberListVO> voList = new ArrayList<>();
			Map<String, String> cookies = getCookies();
			String url = "http://qun.qzone.qq.com/cgi-bin/get_group_member?uin="+getLoginQQ()
			+"&groupid="+group+"&random="+Math.random()+"&g_tk="+getCsrfToken();
			System.out.println(url);
			Map<String, String> data = new HashMap<>(0);
			String result = WebUtil.fetch(url, Method.GET, data, cookies);
			if(result != null){
				result = result.substring(10, result.length() - 2);
				JSONArray jarr = JSONObject.parseObject(result).getJSONObject("data").getJSONArray("item");
				for(int i=0; i<jarr.size(); i++){
					JSONObject obj = jarr.getJSONObject(i);
					voList.add(new GroupMemberListVO(obj.getString("nick"), obj.getLong("uin"), obj.getInteger("iscreator"), obj.getInteger("ismanager")));
				}
				return voList;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 获取群成员列表
	 * @param groupQQ 群QQ
	 */
	public static String getGroupMemberList(String groupQQ){
		if(StringUtils.isNotBlank(groupQQ)){
			Data data = new Data();
			data.setFun("getGroupMemberList");
			data.setGroup(Long.valueOf(groupQQ));
			String result = WebSocketUtil.sendSocketData(data.toJson());
			return result;
		}
		return null;
	}

	/**
	 * 获取群列表
	 */
	public static List<GroupListVO> getGroupList(){
		List<GroupListVO> list = new ArrayList<>();
		Data data = new Data();
		data.setFun("getGroupList");
		String result = WebSocketUtil.sendSocketData(data.toJson());
		if(StringUtils.isNotBlank(result)){
			JSONObject obj = JSON.parseObject(result);
			if(obj.getInteger("status") == 0 && obj.containsKey("data")){
				JSONArray jarr = obj.getJSONArray("data");
				GroupListVO listVO = null;
				for(int a=0; a<jarr.size(); a++){
					JSONObject o1 = jarr.getJSONObject(a);
					listVO = new GroupListVO(o1.getLong("Group"), o1.getString("Name"), null);
					list.add(listVO);
				}
			}
		}
		return list;
	}

	/**
	 * 获好友列表
	 */
	public static List<FriendListVO> getFriendList(){
		List<FriendListVO> list = new ArrayList<>();
		Data data = new Data();
		data.setFun("getFriendList");
		String result = WebSocketUtil.sendSocketData(data.toJson());
		System.out.println(result);
		if(StringUtils.isNotBlank(result)){
			JSONObject obj = JSONObject.parseObject(result);
			if(obj.getInteger("status") == 0 && obj.containsKey("Result")){
				JSONObject jobj = obj.getJSONObject("Result");
				Iterator<String> iter = jobj.keySet().iterator();
				while(iter.hasNext()){
					String key = iter.next();
					JSONObject jsonObject = jobj.getJSONObject(key);
					jsonObject.getString("gname");//分组名称
					JSONArray jarr = jsonObject.getJSONArray("mems");//分组下的成员列表
					if(jarr != null && jarr.size() > 0){
						for(int i=0; i<jarr.size(); i++){
							JSONObject obj1  = jarr.getJSONObject(i);
							list.add(new FriendListVO(obj1.getString("name"), obj1.getLong("uin")));
						}
					}
				}
			}
		}
		return list;
	}


	/**
	 * 获取图片信息
	 */
	public static CQImageInfo getImageInfo(String fileName){
		CQImageInfo cqImageInfo = null;
		// 读取一般的属性文件
		try {
			FileInputStream fin=new FileInputStream(ConfigCache.COOLQ_IMAGE_PATH + "/" 
						+ fileName + ".cqimg"); // 打开文件
			Properties props=new Properties();                 // 建立属性类
			props.load(fin);                                   // 读入文件
			String md5 = props.getProperty("md5");
			String width = props.getProperty("width");
			String height = props.getProperty("height");
			String size = props.getProperty("size");
			String url = props.getProperty("url");
			String addtime = props.getProperty("addtime");
			Date date = new Date(Long.parseLong(addtime));
			cqImageInfo = new CQImageInfo(md5, Integer.parseInt(width), Integer.parseInt(height), Long.parseLong(size), url, date);
			fin.close();
		} catch (FileNotFoundException e) {
			log.error(e.getMessage(), e);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}                                       // 关闭文件
		return cqImageInfo;
	}

	/**
	 * 取群详细信息
	 * @Auth 20
	 * @param groupID 目标群
	 * @return 群详细信息，执行失败时返回null
	 */
	public static String getGroupInfo(String groupID){
		if(StringUtils.isNotBlank(groupID)){
			Data data = new Data();
			data.setFun("getGroupInfo");
			data.setGroup(Long.parseLong(groupID));
			String result = WebSocketUtil.sendSocketData(data.toJson());
			return result;
		}
		return null;
	}

	/**
	 * 取解禁剩余时间
	 * @Auth 20
	 * @param groupID 目标群
	 * @return int 禁言剩余时间，单位：秒，0为未禁言，执行失败时返回null
	 */
	public static String getBanStatus(String groupID){
		if(StringUtils.isNotBlank(groupID)){
			Data data = new Data();
			data.setFun("getBanStatus");
			data.setGroup(Long.parseLong(groupID));
			String result = WebSocketUtil.sendSocketData(data.toJson());
			return result;
		}
		return null;
	}

	/**
	 * 取指定群中被禁言用户列表
	 * @Auth 20
	 * @param groupID 目标群
	 * @return 被禁言的用户信息，执行失败时返回null
	 */
	public static String getBanList(String groupID){
		if(StringUtils.isNotBlank(groupID)){
			Data data = new Data();
			data.setFun("getBanList");
			data.setGroup(Long.parseLong(groupID));
			String result = WebSocketUtil.sendSocketData(data.toJson());
			return result;
		}
		return null;
	}

	/**
	 * 取头像链接
	 * @Auth 20
	 * @param qq 目标QQ
	 * @param size 头像尺寸，默认 100
	 * @return string 头像链接
	 */
	public static String getHeadimgLink(String qq, int size){
		if(StringUtils.isNotBlank(qq) && size > 0){
			Data data = new Data();
			data.setFun("getHeadimgLink");
			data.setSize(size);
			data.setQq(Long.parseLong(qq));
			String result = WebSocketUtil.sendSocketData(data.toJson());
			return result;
		}
		return null;
	}

	/**
	 * 批量取QQ头像
	 * @Auth 20
	 * @param  qqList QQ列表，每个QQ用 _ 分开
	 * @return QQ头像链接列表，执行失败时返回null
	 */
	public static String getMoreQQHeadimg(String qqList){
		if(StringUtils.isNotBlank(qqList)){
			Data data = new Data();
			data.setFun("getMoreQQHeadimg");
			data.setQqList(qqList);
			String result = WebSocketUtil.sendSocketData(data.toJson());
			return result;
		}
		return null;
	}

	/**
	 * 批量取QQ昵称
	 * @Auth 20
	 * @param  qqList QQ列表，每个QQ用 _ 分开
	 * @return QQ昵称列表，执行失败时返回null
	 */
	public static String getMoreQQName(String qqList){
		if(StringUtils.isNotBlank(qqList)){
			Data data = new Data();
			data.setFun("getMoreQQName");
			data.setQqList(qqList);
			String result = WebSocketUtil.sendSocketData(data.toJson());
			return result;
		}
		return null;
	}

	/**
	 * 批量取群头像
	 * @Auth 20
	 * @param groupList 群列表，每个群用 - 分开
	 * @return 群头像链接列表，执行失败时返回null
	 */
	public static String getMoreGroupHeadimg(String groupList){
		if(StringUtils.isNotBlank(groupList)){
			Data data = new Data();
			data.setFun("getMoreGroupHeadimg");
			data.setGroupList(groupList);
			String result = WebSocketUtil.sendSocketData(data.toJson());
			return result;
		}
		return null;
	}
	
	/**
	 * 下载文件
	 */
	public String downFile(String url){
		
		return null;
	}
	
	/**
	 * 用于布尔型转Integer型
	 * @param flag
	 * @return
	 */
	private static int booleanToInteger(boolean flag){
		if(flag){
			return 1;
		}
		return 0;
	}
	
}
