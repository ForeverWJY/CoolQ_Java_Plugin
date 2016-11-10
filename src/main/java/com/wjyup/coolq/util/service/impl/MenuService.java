package com.wjyup.coolq.util.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import com.wjyup.coolq.entity.RequestData;
import com.wjyup.coolq.entity.TempCache;
import com.wjyup.coolq.util.CQSDK;
import com.wjyup.coolq.util.ConfigCache;
import com.wjyup.coolq.util.WebUtil;
import com.wjyup.coolq.util.service.IMenuService;

import us.codecraft.xsoup.Xsoup;
@Service("menuService")
public class MenuService extends BaseService implements IMenuService {
	
	@Override
	public String listMenu(RequestData data) {
		try {
			String key = data.getMsg();
			//oschina资讯
			if("oschina".equals(key)){
				if(ConfigCache.mapCache.get("oschina_news") == null ||
						ConfigCache.mapCache.get("oschina_soft") == null){
					getOschinaNews();
				}
				//读取缓存
				TempCache t1 = (TempCache) ConfigCache.mapCache.get("oschina_news");
				TempCache t2 = (TempCache) ConfigCache.mapCache.get("oschina_soft");
				if(t1 != null && t2 != null){
					Date nowDate = new Date();
					//如果缓存过期了，重新加载
					if(nowDate.getTime() > t1.getDeadLineTime().getTime()){
						getOschinaNews();
					}
					reply(data, t1.getObject().toString());
					reply(data, t2.getObject().toString());
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
	public void getOschinaNews() {
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

}
