package com.wjyup.coolq.util;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import org.apache.log4j.Logger;
/**
 * WebSocket工具类
 * @author WJY
 *
 */
public class WebSocketUtil {
	private static Logger log = Logger.getLogger(WebSocketUtil.class);
	
	/**
	 * 发送json数据并获取返回值
	 * @param message
	 * @return
	 */
	public static String sendSocketData(String message){
		try {
			//判断发送消息方式
			if(StaticConf.WS_SEND_TYPE_HTTP.equals(ConfigCache.WS_SEND_TYPE)){//socket
				StringBuffer sb = new StringBuffer();
				String url = "http://"+ConfigCache.HTTP_HOST+":"+ConfigCache.HTTP_PORT;
				if(ConfigCache.USE_TOKEN){
					long time = System.currentTimeMillis() / 1000;
					String str = ConfigCache.WS_KEY + time + ConfigCache.WS_SECRET;
					//MD5加密
					String hash = Hashing.md5().newHasher().putString(str, Charsets.UTF_8).hash().toString();
					url = url +"/" + time + "/" + hash;
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
