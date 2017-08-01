package com.wjyup.coolq.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

import com.wjyup.coolq.entity.WeatherInfo;

public class WebUtil {
	private static Logger log = Logger.getLogger(WebUtil.class); // 日志
	
	//天气缓存
	public static Map<String, WeatherInfo> weatherInfo = new HashMap<>();

	public static final String[] DUER_RESULT_TYPE = {"txt","txt_card","txt_img","txt_comm","txt_card_filmpet","txt_sugg","txt_list","img_comm","gif_face","multi_news","txt_single_link","multi_normal","multi_movie"};

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
	 * duer接口HTTPclient调用方式
	 */
	public static String getDuerMessage(String content) {
		//返回结果
		String result = null;
		if(StringUtils.isEmpty(content)){
			return result;
		}
		byte[] byte_content = null;
		try {
			byte_content = content.getBytes("UTF-8");
		} catch (Exception e2) {
			log.error("请求参数字符集编码异常：Charset=UTF-8");
		}

		//建立连接
		String urlstr = "https://xiaodu.baidu.com/ws";
		CloseableHttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(urlstr);
		post.setConfig(RequestConfig.DEFAULT);
		EntityBuilder enbu = EntityBuilder.create().setContentType(ContentType.APPLICATION_JSON).setContentEncoding("UTF-8").setBinary(byte_content);
		HttpEntity rqEntity = enbu.build();
		post.setEntity(rqEntity);

		//执行请求
		HttpResponse httprs = null;
		try {
			httprs = client.execute(post);
		} catch (IOException e) {
			log.error("请求失败。meg="+e.getMessage());
		}
		HttpEntity rsEntity = httprs.getEntity();

		if(httprs.getStatusLine().getStatusCode() != 200)
			log.error("请求异常，StatusLine="+httprs.getStatusLine());
		if(rsEntity==null)
			log.error("请求返回空");
		try {
			result = EntityUtils.toString(rsEntity, "UTF-8");
		} catch (Exception e) {
			log.error("请求返回字符集编码异常：Charset=UTF-8");
		}

		//关闭连接
		try {
			client.close();
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		return result;
	}
}
