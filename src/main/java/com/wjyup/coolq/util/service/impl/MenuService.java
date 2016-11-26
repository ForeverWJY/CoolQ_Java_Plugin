package com.wjyup.coolq.util.service.impl;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wjyup.coolq.entity.RequestData;
import com.wjyup.coolq.entity.TempCache;
import com.wjyup.coolq.entity.WeatherInfo;
import com.wjyup.coolq.util.CQSDK;
import com.wjyup.coolq.util.ConfigCache;
import com.wjyup.coolq.util.WebUtil;
import com.wjyup.coolq.util.service.IMenuService;
import com.wjyup.coolq.vo.FriendListVO;
import com.wjyup.coolq.vo.GroupListVO;
import com.wjyup.coolq.vo.GroupMemberListVO;

import us.codecraft.xsoup.Xsoup;
@Service("menuService")
public class MenuService extends BaseService implements IMenuService {
	
	@Override
	public String listMenu(RequestData data) {
		try {
			String key = data.getMsg();
			//查询天气
			if("天气".equals(key.substring(0, 2))){
				String cityName = key.substring(3, key.length());
				return getWeatherByName(cityName);
			}
			//oschina资讯
			if("oschina".equals(key)){
				List<String> list = getOschinaNews();
				if(list != null && list.size() > 0){
					reply(data, list.get(0));
					reply(data, list.get(1));
				}
			}
			//我的群列表
			if("查看群列表".equals(key)){
				if(ConfigCache.isManger(data.getQQ())){
					List<GroupListVO> list = CQSDK.getGroupList();
					StringBuffer msg = new StringBuffer("共有"+list.size()+"个群\n");
					for(GroupListVO vo : list){
						msg.append("\n=>群号:"+vo.getGroup()+"\n");
						msg.append("=>群昵称:"+vo.getGroupNickName()+"\n");
						msg.append("=>群创建者QQ:"+vo.getGroupOwner()+"\n");
					}
					reply(data, msg.substring(0, msg.length() - 1));
				}
			}
			//我的好友列表
			if("查看好友列表".equals(key)){
				if(ConfigCache.isManger(data.getQQ())){
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
				if(ConfigCache.isManger(data.getQQ())){
					String group = key.trim().replace("group:", "");
					List<GroupMemberListVO> list = CQSDK.getGroupMemberList2(group);
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
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
	
	/**
	 * 用于主动回复消息
	 * @param data
	 * @param message
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
			CQSDK.sendDiscussMsg(data.getGroup().toString(), message);
			break;
		}
	}

	@Override
	public String getWeatherByName(String name) {
		StringBuffer str = new StringBuffer();
		try {
			//优先读取缓存
			WeatherInfo info = WebUtil.weatherInfo.get(name);
			if(info != null){
				long time = new Date().getTime() - info.getCacheTime().getTime();
				//缓存两小时
				if(time < 60*60*1000*2){
					return info.getJson()+"\nfor cache 2 hours";
				}
			}
			//重新查询并写入到缓存
			String url = "https://zanlide.com/api/weather?city="+URLEncoder.encode(name, "UTF-8");
			String result = WebUtil.getUrlResult(url, "get");
			log.info(result);
			if(StringUtils.isNotBlank(result)){
				JSONObject jobj = JSON.parseObject(result);
				Integer desc = jobj.getInteger("status");
				if(desc == 1000){
					JSONObject jdata = jobj.getJSONObject("data");
					str.append(String.format("城市:%s，温度：%s℃\n",jdata.getString("city"),jdata.getString("wendu")));
					JSONArray dataArr = jdata.getJSONArray("forecast");
					for(int i=0; i<dataArr.size(); i++){
						JSONObject obj = dataArr.getJSONObject(i);
						str.append(String.format("%s：%s %s %s %s\n",
								obj.get("date"),obj.get("type"),obj.get("low"),
								obj.get("high"),obj.get("fengxiang"),obj.get("fengli")));
					}
					str.append("感冒情况："+jdata.getString("ganmao"));
					//加入cache
					WeatherInfo weatherInfo = new WeatherInfo(new Date(), str.toString());
					WebUtil.weatherInfo.put(name, weatherInfo);
					return str.toString();
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return "没有找到["+name+"]的天气信息！";
		}
		return "没有找到["+name+"]的天气信息！";
	}
	
	/**
	 * 读取oschina的数据，写入到缓存中
	 */
	private void writeOschinaCache(){
		try {
			String text = WebUtil.getUrlResult("http://www.oschina.net/", "get");
			if(StringUtils.isNotBlank(text)){
				Document doc = Jsoup.parse(text);
				StringBuffer news = new StringBuffer("OSChina综合资讯：");
				//综合资讯
				List<Element> list = Xsoup.select(doc, "//div[@id='IndustryNews']/ul[@class='p1']/li/a").getElements();
				for(Element e : list){
					String href = e.attr("href");
					if(!"http".equals(href.substring(0,4))){
						href = "http://www.oschina.net"+href;
					}
					news.append("\n"+e.text()+"\n->链接："+href+"\n");
				}
				StringBuffer softs = new StringBuffer("OSChina软件更新资讯：");
				//软件资讯
				list = Xsoup.select(doc, "//div[@id='ProjectNews']/ul[@class='p1']/li/a").getElements();
				for(Element e : list){
					String href = e.attr("href");
					if(!"http".equals(href.substring(0,4))){
						href = "http://www.oschina.net"+href;
					}
					softs.append("\n"+e.text()+"\n->链接："+href+"\n");
				}
				log.info(news.toString());
				log.info(softs.toString());
				//写入缓存
				Date cacheTime = new Date();
				long deadLineTime = cacheTime.getTime() + (8*60*60*1000);
				TempCache temp1 = new TempCache(new Date(), new Date(deadLineTime), news.toString());
				TempCache temp2 = new TempCache(new Date(), new Date(deadLineTime), softs.toString());
				ConfigCache.mapCache.put("oschina_news", temp1);
				ConfigCache.mapCache.put("oschina_soft", temp2);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	@Override
	public List<String> getOschinaNews() {
		if(ConfigCache.mapCache.get("oschina_news") == null ||
				ConfigCache.mapCache.get("oschina_soft") == null){
			writeOschinaCache();
		}
		List<String> list = null;
		//读取缓存
		TempCache t1 = (TempCache) ConfigCache.mapCache.get("oschina_news");
		TempCache t2 = (TempCache) ConfigCache.mapCache.get("oschina_soft");
		if(t1 != null && t2 != null){
			Date nowDate = new Date();
			//如果缓存过期了，重新加载
			if(nowDate.getTime() > t1.getDeadLineTime().getTime()){
				writeOschinaCache();
			}
			list = new ArrayList<>(2);
			list.add(t1.getObject().toString());
			list.add(t2.getObject().toString());
		}
		return list;
	}

}
