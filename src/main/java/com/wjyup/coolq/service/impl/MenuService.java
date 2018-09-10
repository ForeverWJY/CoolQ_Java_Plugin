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
			//查询天气
			if(key.length() > 2 && "天气".equals(key.substring(0, 2))){
				String cityName = key.replace("天气", "").trim();
				return getWeatherByName(cityName.trim());
			}
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
			//知乎日报
			if(key.equals("zhihu")){
				String result = getZhiHuDaily();
				if(result != null){
					reply(data, result);
				}
			}
			//基金
			if(key.length() <= 10 && key.length() > 2){
				String k = key.substring(0,2);
				String k1 = key.substring(2);
				if(k.equals("基金") && StringUtils.isNumeric(k1)){
					String fund = getFund(k1);
					if(fund != null) reply(data, fund);
				}else if(k.equals("jg") && StringUtils.isNumeric(k1)){
					String fund = getFundGP(k1);
					if(fund != null) reply(data, fund);
				}
			}
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
			String url = "http://wthrcdn.etouch.cn/weather_mini?city="+URLEncoder.encode(name, "UTF-8");
			String result = WebUtil.get(url);
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

	@Override
	public List<String> getOschinaNews() {
		if(LocalCache.getCache("oschina_news") == null){
			writeOschinaCacheByAPI();
		}
		return (List<String>) LocalCache.getCache("oschina_news");
	}

	@Override
	public String getZhiHuDaily() {
		Object o = LocalCache.getCache("zhihu_daily");
		if( o == null){
			writeZhiHuDailyCache();
		}
		//读取缓存
		return LocalCache.getCache("zhihu_daily").toString();
	}
	
	/**
	 * 获取知乎日报，并写入缓存
	 */
	private void writeZhiHuDailyCache(){
		String url = "http://daily.zhihu.com";
		try {
			String result = WebUtil.get(url);
			if(StringUtils.isNotBlank(result)){
				List<Element> list = Xsoup.select(result, "//div[@class='main-content-wrap']/div[@class='row']/div[@class='col-lg-4']").getElements();
				StringBuilder strs = new StringBuilder("知乎日报：\n");
				//只截取开头9条记录
				for(Element el : list){
					List<Element> elList = Xsoup.select(el, "//div[@class='wrap']/div[@class='box']/a").getElements();
					int  b = 0;
					for(Element els : elList){
						String str = String.format("\n%s\n->链接：%s\n", els.getElementsByClass("title").text().trim(),"http://daily.zhihu.com"+els.attr("href"));
						strs.append(str);
						if(b == 3) break;
						b++;
					}
				}
				//写入缓存
				LocalCache.addCache("zhihu_daily", strs.toString());
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	/**
	 * 基金实时走势
	 */
	private String getFund(String fundNum) throws Exception {
		if(StringUtils.isBlank(fundNum)) return null;
		String url = "http://fundgz.1234567.com.cn/js/"+fundNum+".js";
		StringBuilder sb = new StringBuilder();
		String json = WebUtil.get(url);
		if(StringUtils.isNotBlank(json)){
			json = json.substring(8, json.length() - 2);
			JSONObject obj = JSON.parseObject(json);
			if(obj.containsKey("name") && StringUtils.isNotBlank(obj.getString("name"))){
				sb.append("基金名称：").append(obj.getString("name"));
			}
			if(obj.containsKey("fundcode") && StringUtils.isNotBlank(obj.getString("fundcode"))){
				sb.append("[").append(obj.getString("fundcode")).append("]");
			}
			if(obj.containsKey("gsz") && StringUtils.isNotBlank(obj.getString("gsz"))){
				sb.append("\n实时净值：").append(obj.getString("gsz"));
			}
			if(obj.containsKey("gszzl") && StringUtils.isNotBlank(obj.getString("gszzl"))){
				sb.append("\n实时涨跌幅：").append(obj.getString("gszzl")+"%");
			}
			if(obj.containsKey("gztime") && StringUtils.isNotBlank(obj.getString("gztime"))){
				sb.append("\n更新时间：").append(obj.getString("gztime"));
			}
			if(obj.containsKey("jzrq") && StringUtils.isNotBlank(obj.getString("jzrq"))
					&& obj.containsKey("dwjz") && StringUtils.isNotBlank(obj.getString("dwjz"))){
				sb.append("\n").append(obj.getString("jzrq")).append("净值：").append(obj.getString("dwjz"));
			}
			if(log.isDebugEnabled()){
				log.debug(sb.toString());
			}
			return sb.toString();
		}
		return null;
	}
	
	/**
	 * 查询基金和对应的股票信息
	 */
	public String getFundGP(String fundNum){
		try {
			if(fundNum == null) return null;
			StringBuffer sb = new StringBuffer();
			//查询基金的信息
			String fund = getFund(fundNum);
			if(fund != null){
				sb.append(fund+"\n");
				//查询基金买的股票列表
				String fundWithGP = getGP(fundNum);
				if(fundWithGP != null){
					//传入股票代码列表，获取股票详情
					String result = getGPResult(fundWithGP);
					if(result != null){
						sb.append(result);
						return sb.toString();
					}
				}else{
					log.info("基金的股票代码为空！");
				}
			}else{
				return "没查询到["+fundNum+"]的基金信息";
			}
		} catch (Exception e) {
			log.error("查询基金和对应的股票信息失败\n", e);
		}
		return null;
	}
	
	/**
	 * 查询基金重仓的股票
	 */
	private String getGP(String fundNum) throws Exception {
		//优先读取缓存
		Object o = LocalCache.getCache("jijin_"+fundNum);
		//读取缓存
		if(o != null && StringUtils.isNotBlank(o.toString())){
			return o.toString();
		}
		//获取结果并设置缓存
		String url = "http://fund.eastmoney.com/pingzhongdata/"+fundNum+".js";
		String text = WebUtil.get(url);
		if(StringUtils.isNotBlank(text)){
			//处理js
			int lastIndex = text.indexOf("/*股票仓位测算图*/");
			String js = text.substring(0, lastIndex);
			js = js.replace("//最小申购金额", "");
			ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
			//执行js
			engine.eval(js);
			//js数组转成Java数组
			engine.eval("var JavaArray = Java.to(stockCodes,\"java.lang.String[]\");");
			String[] str = (String[]) engine.get("JavaArray");
			String gpstr = Arrays.deepToString(str).replaceAll(" ", "").replaceAll("\\[", "").replaceAll("\\]", "");

			LocalCache.addCache("jijin_"+fundNum, gpstr);
			return gpstr;
		}
		return null;
	}
	
	/**
	 * 获取股票数据
	 * @param gpstr
	 * @return
	 */
	private String getGPResult(String gpstr) throws Exception {
		//拼接URL
		String url = "http://nufm3.dfcfw.com/EM_Finance2014NumericApplication/JS.aspx?type=CT&cmd="+gpstr+"&sty=E1OQCPZT&st=z&sr=&p=&ps=&cb=&js=var%20js_favstock={favif:[%28x%29]}&token=8a36403b92724d5d1dd36dc40534aec5&rt=function%20random()%20{%20[native%20code]%20}";
		StringBuilder sb = new StringBuilder("股票名称[代码]|最新价|涨跌幅|市盈率\n");
		String json = WebUtil.get(url);
		if(StringUtils.isNotBlank(json)){
			json = "{\"data\":"+json.substring(23, json.length() - 1) + "}";
			JSONObject obj = JSON.parseObject(json);
			if(obj.containsKey("data") && obj.getJSONArray("data").size() > 0){
				JSONArray jarr = obj.getJSONArray("data");
				for(int i=0; i<jarr.size(); i++){
					String o = jarr.getString(i);
					//解析股票
					String[] os = o.split(",");
					sb.append(os[2]+"["+os[1]+"]"+"|"+os[3]+"|"+os[4]+"|"+os[5]);
					if(i != jarr.size() - 1){
						sb.append("\n");
					}
				}
			}
			return sb.toString();
		}
		return null;
	}
}
