package com.wjyup.coolq.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wjyup.coolq.entity.CQImageInfo;
import com.wjyup.coolq.entity.Data;
import com.wjyup.coolq.vo.*;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * CoolQ SDK
 *
 * HTTP状态码
 * 200：表示成功调用了API，但不能保证调用结果是正确的
 * 403：表示缺少API所需信息
 * 404：表示调用了不存在的API
 * 405：表示使用了接口不支持的协议，如GET，HEAD
 * 406：表示提交的数据非支持的数据格式，如XML
 *
 * 如果使用了 数据校验 功能，在出错的情况下会返回以下状态码
 * 401：表示缺少校验参数，或传递的参数有误
 * 408：表示该请求超过有效时间
 *
 * @author WJY
 */
public class CQSDK {
	private static Logger log = LogManager.getLogger("CQSDK");

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
	 * 发送图片 图片文件需放在image文件夹下
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
	 * 发送语音
	 */
	public static String sendVoice(String fileName) {
		if (StringUtils.isNotBlank(fileName)) {
			return "[CQ:record,file=" + StringEscapeUtils.escapeJava(fileName) + "]";
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
			StringBuilder ss = new StringBuilder("[CQ:share,url=").append(url);
			if (StringUtils.isNotBlank(title)) {
				ss.append(",title=").append(title);
			}
			if (StringUtils.isNotBlank(content)) {
				ss.append(",content=").append(content);
			}
			if (StringUtils.isNotBlank(picUrl)) {
				ss.append(",image=").append(picUrl);
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
			return SendMessageUtil.sendSocketData(data.toJson());
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
			return SendMessageUtil.sendSocketData(data.toJson());
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
			return SendMessageUtil.sendSocketData(data.toJson());
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
			String result = SendMessageUtil.sendSocketData(data.toJson());
			return result;
		}
		return null;
	}

	/**
	 * 取登录QQ
	 */
	public static String getLoginQQ() {
		Data data = new Data("getLoginQQ");
		String result = SendMessageUtil.sendSocketData(data.toJson());
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
		String cookie = SendMessageUtil.sendSocketData(data.toJson());
		if(StringUtils.isNotBlank(cookie)){
			JSONObject jobj = JSONObject.parseObject(cookie);
			Integer status = jobj.getInteger("Status");
			if(status == 0){
				String result = jobj.getString("Result");
				result = result.replaceAll(" ", "");
				String[] res = result.split(";");
				for(String str : res){
					String[] temp = str.split("=");
					map.put(temp[0], temp[1]);
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
		String nickName = SendMessageUtil.sendSocketData(data.toJson());
		if(StringUtils.isNotBlank(nickName)){
			JSONObject jobj = JSONObject.parseObject(nickName);
			Integer status = jobj.getInteger("Status");
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
		String info = SendMessageUtil.sendSocketData(data.toJson());
		if(StringUtils.isNotBlank(info)){
			JSONObject jobj = JSONObject.parseObject(info);
			Integer status = jobj.getInteger("Status");
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
			jobj.put("Fun", "getGroupMemberInfo");
			jobj.put("Group", group);
			jobj.put("QQ", qq);
			jobj.put("LocalCache", booleanToInteger(cache));
			String groupMemberInfo = SendMessageUtil.sendSocketData(jobj.toJSONString());
			//System.out.println(groupMemberInfo);
			if(StringUtils.isNotBlank(groupMemberInfo)){
				GroupMemberInfoVO infoVO = JSON.parseObject(groupMemberInfo, GroupMemberInfoVO.class);
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
			jobj.put("Fun", "getStrangerInfo");
			jobj.put("QQ", qq);
			jobj.put("LocalCache", booleanToInteger(cache));
			String info = SendMessageUtil.sendSocketData(jobj.toJSONString());
			if(StringUtils.isBlank(info)){
				return null;
			}
			StrangerInfoVO infoVO = JSON.parseObject(info, StrangerInfoVO.class);
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
			obj.put("Fun", "GetFontInfo");
			obj.put("ID", id);
			String result = SendMessageUtil.sendSocketData(obj.toJSONString());
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
			obj.put("Fun", "GetAnonymousInfo");
			obj.put("source", source);
			String result = SendMessageUtil.sendSocketData(obj.toJSONString());
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
			obj.put("Fun", "GetFileInfo");
			obj.put("source", source);
			String result = SendMessageUtil.sendSocketData(obj.toJSONString());
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
			obj.put("Fun", "setGroupKick");
			obj.put("Group", group);
			obj.put("QQ", qq);
			obj.put("RefuseJoin", refuse);
			String result = SendMessageUtil.sendSocketData(obj.toJSONString());
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
			obj.put("Fun", "setGroupBan");
			obj.put("Group", group);
			obj.put("QQ", qq);
			obj.put("Time", time);//0为解除禁言
			String result = SendMessageUtil.sendSocketData(obj.toJSONString());
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
			obj.put("Fun", "setGroupAdmin");
			obj.put("Group", group);
			obj.put("QQ", qq);
			obj.put("Become", booleanToInteger(become));
			String result = SendMessageUtil.sendSocketData(obj.toJSONString());
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
			obj.put("Fun", "setGroupWholeBan");
			obj.put("Group", group);
			obj.put("IsGag", booleanToInteger(isGag));
			String result = SendMessageUtil.sendSocketData(obj.toJSONString());
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
			obj.put("Fun", "setGroupAnonymous");
			obj.put("Group", group);
			obj.put("Open", booleanToInteger(open));
			String result = SendMessageUtil.sendSocketData(obj.toJSONString());
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
			obj.put("Fun", "setGroupCard");
			obj.put("Group", group);
			obj.put("QQ", qq);
			obj.put("Card", card);//为空时清空群名片
			String result = SendMessageUtil.sendSocketData(obj.toJSONString());
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
			obj.put("Fun", "setDiscussLeave");
			obj.put("Group", discuss);
			String result = SendMessageUtil.sendSocketData(obj.toJSONString());
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
			obj.put("Fun", "setGroupAddRequest");
			obj.put("responseFlag", responseFlag);
			obj.put("subtype", subType);//  1/群添加,2/群邀请
			obj.put("type", type);//  1/通过,2/拒绝
			obj.put("Msg", Msg);// 拒绝时的理由
			String result = SendMessageUtil.sendSocketData(obj.toJSONString());
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
			obj.put("Fun", "setGroupAnonymousBan");
			obj.put("Group", group);
			obj.put("Anonymous", anonymous);//  1/群添加,2/群邀请
			obj.put("Time", time);//  1/通过,2/拒绝
			String result = SendMessageUtil.sendSocketData(obj.toJSONString());
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
			obj.put("Fun", "setFriendAddRequest");
			obj.put("responseFlag", responseFlag);
			obj.put("Type", type);// 1/通过,2/拒绝
			obj.put("Name", name);//备注
			String result = SendMessageUtil.sendSocketData(obj.toJSONString());
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
			obj.put("Fun", "setGroupSpecialTitle");
			obj.put("Group", group);
			obj.put("QQ", qq);
			obj.put("Tip", tip);//头衔名称
			obj.put("Time", time);//过期时间
			String result = SendMessageUtil.sendSocketData(obj.toJSONString());
			return result;
		}
		return null;
	}
	
	
	/**
	 * 获取群列表
	 * url ： http://qun.qq.com/cgi-bin/qun_mgr/get_group_list
	 * method : post
	 * parameters : bkn
	 */
	public static List<GroupListVO> getGroupList(){
		try {
//			List<GroupListVO> list = new ArrayList<>();
//			Map<String, String> data = new HashMap<>();
//			data.put("bkn", getCsrfToken());
//			Map<String, String> cookies = getCookies();
//			String result = WebUtil.fetch("http://qun.qq.com/cgi-bin/qun_mgr/get_group_list", Method.POST, data, cookies);
//			if(result != null){
//				//System.out.println(result);
//				log.info(result);
//				JSONArray jarr = JSONObject.parseObject(result).getJSONArray("create");
//				GroupListVO listVO = null;
//				if(jarr != null){
//					for(int a=0; a<jarr.size(); a++){
//						JSONObject obj = jarr.getJSONObject(a);
//						listVO = new GroupListVO(obj.getLong("gc"), obj.getString("gn"), obj.getLong("owner"));
//						list.add(listVO);
//					}
//				}
//				jarr = JSONObject.parseObject(result).getJSONArray("join");
//				if(jarr != null){
//					for(int a=0; a<jarr.size(); a++){
//						JSONObject obj = jarr.getJSONObject(a);
//						listVO = new GroupListVO(obj.getLong("gc"), obj.getString("gn"), obj.getLong("owner"));
//						list.add(listVO);
//					}
//				}
//				jarr = JSONObject.parseObject(result).getJSONArray("manage");
//				if(jarr != null){
//					for(int a=0; a<jarr.size(); a++){
//						JSONObject obj = jarr.getJSONObject(a);
//						listVO = new GroupListVO(obj.getLong("gc"), obj.getString("gn"), obj.getLong("owner"));
//						list.add(listVO);
//					}
//				}
//				return list;
//			}
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
	 * 需要的cookie参数：uin skey
	 */
	public static List<FriendListVO> getFriendList(){
		try {
//			List<FriendListVO> voList = new ArrayList<>();
//			Map<String, String> data = new HashMap<>();
//			data.put("bkn", getCsrfToken());
//			Map<String, String> cookies = getCookies();
//			String result = WebUtil.fetch("http://qun.qq.com/cgi-bin/qun_mgr/get_friend_list", Method.POST, data, cookies);
//			if(result != null){
////				System.out.println(result);
//				log.info(result);
//				JSONObject jobj = JSONObject.parseObject(result).getJSONObject("result");
//				Iterator<String> iter = jobj.keySet().iterator();
//				while(iter.hasNext()){
//					String key = iter.next();
//					JSONObject jsonObject = jobj.getJSONObject(key);
//					jsonObject.getString("gname");//分组名称
//					JSONArray jarr = jsonObject.getJSONArray("mems");//分组下的成员列表
//					if(jarr != null && jarr.size() > 0){
//						for(int i=0; i<jarr.size(); i++){
//							JSONObject obj  = jarr.getJSONObject(i);
//							voList.add(new FriendListVO(obj.getString("name"), obj.getLong("uin")));
//						}
//						return voList;
//					}
//				}
//			}
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
	public static List<GroupMemberInfoVO> getGroupMemberList1(String group){
		try {
//			List<GroupMemberInfoVO> voList = new ArrayList<>(0);
//			Map<String, String> data = new HashMap<>();
//			data.put("bkn", getCsrfToken());
//			data.put("gc", group);
//			data.put("st", "0");
//			data.put("end", "2000");
//			data.put("sort", "0");
//			Map<String, String> cookies = getCookies();
//			String result = WebUtil.fetch("http://qun.qq.com/cgi-bin/qun_mgr/search_group_members", Method.POST, data, cookies);
//			if(result != null){
////				System.out.println(result);
//				log.info(result);
//				JSONObject jobj = JSONObject.parseObject(result);
//				//等级列表
//				JSONObject level = jobj.getJSONObject("levelname");
//				HashMap<String, String> levelMap = new HashMap<>(level.size());
//				Iterator<String> it = level.keySet().iterator();
//				while(it.hasNext()){
//					String key = it.next();
//					levelMap.put(key, level.getString(key));
//				}
//				JSONArray jarr = jobj.getJSONArray("mems");
//				GroupMemberInfoVO vo = null;
//				voList = new ArrayList<>(jarr.size());
//				for(int a=0; a<jarr.size(); a++){
//					JSONObject obj = jarr.getJSONObject(a);
//					vo = new GroupMemberInfoVO();
//					vo.setGroup(group);
//					vo.setQQ(obj.getString("uin"));
//					vo.setCard(obj.getString("card"));
//					vo.setName(obj.getString("nick"));
//					vo.setGender(obj.getInteger("g"));
//					vo.setLastTime(obj.getLong("last_speak_time"));
//					vo.setPower(obj.getInteger("role"));
//					vo.setOld(obj.getInteger("qage"));
//					vo.setLevel(levelMap.get(obj.getJSONObject("lv").getString("level")));
//					voList.add(vo);
//				}
//				return voList;
//			}
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
	public static List<GroupMemberListVO> getGroupMemberList2(String group){
		try {
//			List<GroupMemberListVO> voList = new ArrayList<>();
//			Map<String, String> cookies = getCookies();
//			String url = "http://qun.qzone.qq.com/cgi-bin/get_group_member?uin="+getLoginQQ()
//			+"&groupid="+group+"&random="+Math.random()+"&g_tk="+getCsrfToken();
//			System.out.println(url);
//			Map<String, String> data = new HashMap<>(0);
//			String result = WebUtil.fetch(url, Method.GET, data, cookies);
//			if(result != null){
//				log.info(result);
//				result = result.substring(10, result.length() - 2);
//				JSONArray jarr = JSONObject.parseObject(result).getJSONObject("data").getJSONArray("item");
//				for(int i=0; i<jarr.size(); i++){
//					JSONObject obj = jarr.getJSONObject(i);
//					voList.add(new GroupMemberListVO(obj.getString("nick"), obj.getLong("uin"), obj.getInteger("iscreator"), obj.getInteger("ismanager")));
//				}
//				return voList;
//			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
	
	
	/**
	 * 获取图片信息
	 */
	public static CQImageInfo getImageInfo(String fileName){
		CQImageInfo cqImageInfo = null;
		// 读取一般的属性文件
		try {
			FileInputStream fin=new FileInputStream(SpringContext.getConfigCache().getCOOLQ_IMAGE_PATH() + "/"
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
	 * 根据QQ查询出昵称
	 */
	public static String queryNickNameByQQ(String qq){
		String nickName = null;
		if(StringUtils.isBlank(qq)){
			return null;
		}
		log.info("开始获取“"+qq+"”的昵称");
		try {
			String url = String.format("http://users.qzone.qq.com/fcg-bin/cgi_get_portrait.fcg?uins=%s&ptlang=2052",qq);
			//接收的是GBK字符集
			String result = new String(WebUtil.get(url).getBytes("UTF-8"),"GBK");
			if(StringUtils.isNotBlank(result) && result.length() > 80){
				log.info("获取昵称数据："+result);
				String str = result.substring(17);
				str = str.substring(0, str.length() - 1);
				String[] s = str.split(",");
				if(StringUtils.isNotBlank(s[6])){
					str = s[6].substring(1, s[6].length() - 1);
					nickName = str;
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		log.info(qq+"的昵称为["+nickName+"]");
		return nickName;
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
		return flag ? 1:0;
	}
}
