package com.wjyup.coolq.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

/**
 * 发送消息工具类
 * @author WJY
 */
public class SendMessageUtil {
	private static Logger log = LogManager.getLogger(SendMessageUtil.class);
	
	/**
	 * 发送json数据并获取返回值
	 * @param message 消息
	 * @return 发送消息的结果
	 */
	public static String sendSocketData(String message){
		try {
			ConfigCache configCache = SpringContext.getConfigCache();
			//判断发送消息方式
			if(StaticConf.MSG_SEND_TYPE_HTTP.equalsIgnoreCase(configCache.getMSG_SEND_TYPE())){// http
				String url = String.format("http://%s:%s", configCache.getHTTP_HOST(), configCache.getHTTP_PORT());
				if(configCache.isUSE_TOKEN()){// 使用token
					long authTime = System.currentTimeMillis() / 1000;
					String key = configCache.getKEY()+":"+authTime;
					String authToken = DigestUtils.md5DigestAsHex(key.getBytes(StandardCharsets.UTF_8));
					JSONObject jsonObject = JSON.parseObject(message);
					jsonObject.put("authTime", authTime);
					jsonObject.put("authToken", authToken);
					message = jsonObject.toJSONString();
				}
				log.debug("发送的json文本："+message);
				try{
					String result = WebUtil.post(url, message);
					log.debug("返回结果:" + result);
					return result;
				}catch (Exception e){
					log.error(e.getMessage(),e);
				}

			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
}
