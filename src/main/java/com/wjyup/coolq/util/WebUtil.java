package com.wjyup.coolq.util;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

public class WebUtil {
	private static Logger log = Logger.getLogger(WebUtil.class); // 日志
	
	/**
	 * 根据URL和请求类型获取请求结果，用于跨域请求
	 * @param url
	 * @param urlType POST OR GET
	 * @return
	 */
	public static String getUrlResult(String url,String urlType){
		Method method = null;
		if(urlType!= null && !"".equals(urlType) && urlType.equalsIgnoreCase("post")){
			method = Method.POST;
		}else{
			method = Method.GET;
		}
		Response resp = null;
		try {
			resp = Jsoup.connect(url).userAgent("Chrome").timeout(5000).ignoreContentType(true).method(method).execute();
		} catch (IOException e) {
			log.error(WebUtil.class,e);
		}
		if(resp == null){
			return "error";
		}else{
			return resp.body();
		}
	}
}
