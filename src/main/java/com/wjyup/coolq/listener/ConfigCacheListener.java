package com.wjyup.coolq.listener;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import javax.servlet.ServletContextEvent;

import com.wjyup.coolq.util.TCPServer;
import org.apache.log4j.Logger;
import org.springframework.web.context.ContextLoaderListener;

import com.wjyup.coolq.util.ConfigCache;

public class ConfigCacheListener extends ContextLoaderListener {
	private Logger log = Logger.getLogger(getClass());
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
		log.info("加载配置。");
		try {
			//读取配置
			InputStream inputStream = getClass().getResourceAsStream("/data.properties");
			Properties p = new Properties();
			p.load(inputStream);
			
			//设置配置
			ConfigCache.WSHost = p.getProperty("ws.host");
			ConfigCache.WSPort = p.getProperty("ws.port");
			ConfigCache.PHP_WS_URL = p.getProperty("php.ws.url");
			String useToken = p.getProperty("ws.use_token");
			if("1".equals(useToken)){
				ConfigCache.USE_TOKEN = true;
			}else{
				ConfigCache.USE_TOKEN = false;
			}
			ConfigCache.HTTP_HOST = p.getProperty("http.host");
			ConfigCache.HTTP_PORT= p.getProperty("http.port");

			ConfigCache.WS_KEY = p.getProperty("ws.key");
			ConfigCache.WS_SECRET = p.getProperty("ws.secret");
			ConfigCache.SOCKET_PORT = Integer.valueOf(p.getProperty("ws.socket.port"));
			//CoolQ 图片文件夹
			ConfigCache.COOLQ_IMAGE_PATH = p.getProperty("coolq.image.path");
			//管理员QQ，使用逗号分隔
			String manage = p.getProperty("manager.qq");
			String[] manages = manage.split(",");
			ConfigCache.MANAGER_QQ = new ArrayList<>(manages.length);
			for(String key : manages){
				ConfigCache.MANAGER_QQ.add(Long.parseLong(key));
			}
			//消息推送模式
			ConfigCache.WS_SEND_TYPE = p.getProperty("ws.send.type");
			if("2".equals(ConfigCache.WS_SEND_TYPE)){
				//启动tcp server
				startSocket();
			}
			//配置一个文件夹，存放所有处理消息的插件类，通过反射遍历并调用
			ConfigCache.PLUGIN_PACKAGE_PATH = p.getProperty("plugin.package.path");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		log.info("配置加载完毕！");
	}

	/**
	 * 启动Socket服务
	 */
	public void startSocket(){
		Thread t = new Thread(new TCPServer());
		t.start();
	}
}
