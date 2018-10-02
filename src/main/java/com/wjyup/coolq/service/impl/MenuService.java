package com.wjyup.coolq.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wjyup.coolq.entity.*;
import com.wjyup.coolq.service.IMenuService;
import com.wjyup.coolq.util.*;
import com.wjyup.coolq.vo.FriendListVO;
import com.wjyup.coolq.vo.GroupListVO;
import com.wjyup.coolq.vo.GroupMemberListVO;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import us.codecraft.xsoup.Xsoup;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
@Service("menuService")
public class MenuService extends BaseService implements IMenuService {

	@Override
	public String listMenu(RequestData data) {
		try {
			String key = data.getMsg();
			//包含CQ码直接跳过
			if(key.contains("CQ:")) return null;
			//我的群列表
			if("查看群列表".equals(key)){
				if(SpringContext.getConfigCache().isManger(data.getQQ())){
					List<GroupListVO> list = CQSDK.getGroupList();
					StringBuffer msg = new StringBuffer("共有"+list.size()+"个群\n");
					for(GroupListVO vo : list){
						msg.append(String.format("\n=>群号:%s\n=>群昵称:%s\n=>群创建者QQ:%s\n", vo.getGroup(), vo.getGroupNickName(), vo.getGroupOwner()));
					}
					reply(data, msg.substring(0, msg.length() - 1));
				}
			}
			//我的好友列表
			if("查看好友列表".equals(key)){
				if(SpringContext.getConfigCache().isManger(data.getQQ())){
					List<FriendListVO> list = CQSDK.getFriendList();
					StringBuffer msg = new StringBuffer("共有"+list.size()+"个好友\n");
					for(FriendListVO vo : list){
						msg.append("\n=>昵称："+vo.getName()+"\n");
						msg.append("=>QQ："+vo.getQq()+"\n");
					}
					reply(data, msg.substring(0, msg.length() - 1));
				}
			}
			//查看指定群的成员列表
			if(key.indexOf("group:") == 0){
				if(SpringContext.getConfigCache().isManger(data.getQQ())){
					String group = key.trim().replace("group:", "");
					List<GroupMemberListVO> list = CQSDK.getGroupMemberList2(group.trim());
					StringBuffer msg = new StringBuffer("群号："+group+" 共有"+list.size()+"个成员\n");
					for(GroupMemberListVO vo : list){
						msg.append("\n=>群名片："+vo.getName()+"\n");
						msg.append("=>QQ："+vo.getQq()+"\n");
						if(vo.getIsCreater() == 1){
							msg.append("=>是该群的创建者\n");
						}
						if(vo.getIsManager() == 1){
							msg.append("=>是该群管理员\n");
						}
					}
					reply(data, msg.substring(0, msg.length() - 1));
				}
			}
			//显示功能列表
//			if(key.equals("help")){
//				SettingVO vo = settingListService.getSetting("menu_list");
//				reply(data,vo.getValue());
//			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
	
	/**
	 * 用于主动回复消息
	 * @param data 数据
	 * @param message 消息
	 */
	private void reply(RequestData data,String message){
		switch (data.getType()) {
		//私聊消息
		case 1:
			CQSDK.sendPrivateMsg(data.getQQ().toString(), message);
			break;
		//群消息
		case 2:
			CQSDK.sendGroupMsg(data.getGroup().toString(), message);
		    break;
		//讨论组信息
		case 4:
			CQSDK.sendDiscussMsg(data.getDiscuss().toString(), message);
			break;
		}
	}
	
	/**
	 * 读取oschina的数据，写入到缓存中
	 */
	private void writeOschinaCacheByAPI(){
		String[] OSCHINA_TYPE = {"industry", "project"};
		String url = "https://www.oschina.net/action/ajax/get_more_news_list?newsType=";
		try {
			//获取资讯消息
			String result = WebUtil.get(url + OSCHINA_TYPE[0]);
			if (result != null) {
				Elements elements = Xsoup.select(result, "//div[@class='main-info box-aw']/a").getElements();
				List<String> strList = new ArrayList<>();
				for (int i = 1; i < elements.size(); i++) {
					if (i > 10) break;
					elements.get(i).children().forEach(x -> {
						String href = x.parent().attr("href");
						if (!href.startsWith("http")) {
							href = "https://www.oschina.net" + href;
						}
						strList.add(String.format("%s\n->%s",x.text(), href));
					});
				}
				//写入缓存
				LocalCache.addCache("oschina_news", strList);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
}
