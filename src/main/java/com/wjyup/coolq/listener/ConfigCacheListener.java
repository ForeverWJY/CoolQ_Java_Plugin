package com.wjyup.coolq.listener;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import javax.servlet.ServletContextEvent;

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
			//CoolQ 图片文件夹
			ConfigCache.COOLQ_IMAGE_PATH = p.getProperty("coolq.image.path");
			//管理员QQ，使用逗号分隔
			String manage = p.getProperty("manager.qq");
			String[] manages = manage.split(",");
			ConfigCache.MANAGER_QQ = new ArrayList<>(manages.length);
			for(String key : manages){
				ConfigCache.MANAGER_QQ.add(Long.parseLong(key));
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
}
