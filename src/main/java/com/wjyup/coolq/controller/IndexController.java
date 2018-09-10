package com.wjyup.coolq.controller;

import com.alibaba.fastjson.JSON;
import com.wjyup.coolq.entity.RequestData;
import com.wjyup.coolq.util.ConfigCache;
import com.wjyup.coolq.util.MessageHandle;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 * 消息接收Controller
 * 
 * @author WJY
 *
 */
@RestController
@Scope("prototype")
public class IndexController {
	@Resource
	private ConfigCache configCache;
	// 聊天记录单独存放
	private Logger log = LogManager.getLogger(IndexController.class);

	@Resource
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;

	@RequestMapping(value = "index")
	public String index() {
		log.info("index controller.index");
		return "This is index test";
	}

	@RequestMapping(value = "/coolq", produces = "application/json;charset=UTF-8")
	public String coolq(@RequestBody String param) {
		try {
			// 消息长度
			if(StringUtils.isBlank(param)){
				return "[]";
			}
			// GBK解码
			String str1 = URLDecoder.decode(param, "GBK");
			// UTF-8解码
			str1 = URLDecoder.decode(str1, "UTF-8");
			str1 = str1.substring(0, str1.length() - 1);
			log.debug("接收消息：" + str1);
			// json转对象（消息对象）
			RequestData requestData = JSON.parseObject(str1, RequestData.class);
			log.debug("消息内容："+requestData.getMsg());

			boolean isOk = true;
			//校验token
			if (configCache.isUSE_TOKEN()) {
				log.debug("authTime:{}", requestData.getAuthTime());
				log.debug("authToken:{}", requestData.getAuthToken());
				String key = configCache.getKEY() + ":" + requestData.getAuthTime();
				String authToken = DigestUtils.md5DigestAsHex(key.getBytes(StandardCharsets.UTF_8));
				if (authToken.equals(requestData.getAuthToken())) {
					isOk = true;
				} else {
					isOk = false;
					log.error("authToken校验出错：请核对key的设置是否和插件的设置相同！");
				}
			}
			if (isOk) {
				// 交给线程处理消息
				MessageHandle handle = new MessageHandle(requestData);
				// 交给Spring管理线程
				threadPoolTaskExecutor.execute(handle);
			}
			// 记录聊天记录
//			chatLogService.add(new ChatLog(requestData.getType(), requestData.getSubType(), requestData.getQQ(),
//					requestData.getGroup(), requestData.getMsg(), requestData.getFont()));

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return "[]";
	}

}
