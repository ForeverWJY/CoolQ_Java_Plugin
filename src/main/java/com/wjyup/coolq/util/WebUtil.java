package com.wjyup.coolq.util;

import com.wjyup.coolq.entity.WeatherInfo;
import okhttp3.*;
import org.apache.log4j.Logger;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WebUtil {
	private static Logger log = Logger.getLogger(WebUtil.class); // 日志
	
	//天气缓存
	public static Map<String, WeatherInfo> weatherInfo = new HashMap<>();

	//okhttp mediatype
	public static final MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
	private static ThreadLocal<OkHttpClient> okHttpClientThreadLocal = new ThreadLocal<OkHttpClient>(){
		@Override
		public OkHttpClient get() {
			return new OkHttpClient();
		}
	};


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
	
	/**
	 * 根据URL和请求类型获取请求结果，用于跨域请求
	 * @param url
	 * @param method Method.POST Method.GET
	 * @param data 参数
	 * @param cookie cookies
	 * @return
	 */
	public static String fetch(String url, Method method, Map<String, String> data, Map<String, String> cookie){
		Response resp = null;
		try {
			resp = Jsoup.connect(url).userAgent("Chrome").timeout(5000)
					.data(data)
					.cookies(cookie)
					.ignoreContentType(true).method(method).execute();
		} catch (IOException e) {
			log.error(WebUtil.class,e);
		}
		return resp.body();
	}

	/**
	 * okhttpclient 提交POST数据
	 * @param url 链接
	 * @param json  JSON数据
	 * @return
	 * @throws IOException
	 */
	public static String post(String url, String json) throws IOException {
		RequestBody body = RequestBody.create(mediaType, json);
		Request request = new Request.Builder()
				.url(url)
				.post(body)
				.build();
		okhttp3.Response response = okHttpClientThreadLocal.get().newCall(request).execute();
		if (response.isSuccessful()) {
			return response.body().string();
		} else {
			throw new IOException("Unexpected code " + response);
		}
	}

	/**
	 * okhttpclient 发送get请求
	 * @param url 网址
	 * @throws Exception
	 */
	public static String get(String url) throws Exception {
		Request request = new Request.Builder()
				.url(url)
				.build();

		okhttp3.Response response = okHttpClientThreadLocal.get().newCall(request).execute();
		if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

//		Headers responseHeaders = response.headers();
//		for (int i = 0; i < responseHeaders.size(); i++) {
//			System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
//		}

//		System.out.println(response.body().string());
		return response.body().string();
	}
}
