package com.wjyup.coolq.listener;

import java.io.InputStream;
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
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
}
