package com.wjyup.coolq.util;

import com.alibaba.fastjson.JSON;
import com.wjyup.coolq.service.ResolveMessageService;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.nutz.json.Json;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * 静态配置
 * 
 * @author WJY
 */
@Component
@Data
@Configuration
@PropertySource(value = "classpath:data.properties")
public class ConfigCache implements InitializingBean {
	private Logger log = LogManager.getLogger(ConfigCache.class);

	// 使用Websocket方式推送消息，访问插件的Websocket的Host和Port
	@Value("${ws.host}")
	private String WSHost;// websocket的host
	@Value("${ws.port}")
	private String WSPort;// websocket的port

	// 使用HTTP方式推送消息时，访问插件的HTTP地址
	@Value("${http.host}")
	private String HTTP_HOST;// http的host
	@Value("${http.port}")
	private String HTTP_PORT;// http的port

	// 选择推送消息的方式：websocket http
	@Value("${msg.send.type}")
	private String MSG_SEND_TYPE;
	/**
	 * 根据是否设置key的值自动判断
	 */
	private boolean USE_TOKEN;//使用TOKEN

	// 插件端的设置：扩展功能->数据设置->key
	@Value("${key}")
	private String KEY;//key

	/**
	 * 处理包含CQ码的消息吗？
	 */
	@Value("${msg.cq}")
	private String CQ_MSG;
	private boolean DO_CQ_MSG;

	@Value("${coolq.image.path}")
	private String COOLQ_IMAGE_PATH;//CoolQ 图片文件夹
	@Value("${manager.qq}")
	private String MANAGER_QQ;//管理员QQ，使用逗号分隔
	// 管理员QQ列表
	private List<Long> ManagerQQList;

	public boolean ISDEBUG = false;//是否开启debug模式
	
	/**
	 * 存储插件列表
	 */
	public static final List<ResolveMessageService> MSG_PLUGIN_LIST = new ArrayList<>();

	/**
	 * 配置一个文件夹，存放所有处理消息的插件类，通过反射遍历并调用
	 */
	@Value("${plugin.package.path}")
	private String PLUGIN_PACKAGE_PATH;

	//查看QQ是否是机器人管理员
	public boolean isManger(Long qq){
			if(qq == null || qq == 0 || getManagerQQList() == null || getManagerQQList().isEmpty()){
				return false;
			}
			for(Long q : getManagerQQList()){
				if(q.longValue() == qq.longValue()){
					return true;
				}
			}
		return false;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		//初始化管理员列表
		if (StringUtils.isNotBlank(getMANAGER_QQ())) {
			String[] str = getMANAGER_QQ().split(",");
			if (str.length > 0) {
				List<Long> list = new ArrayList<>(str.length);
				for (String s : str) {
					list.add(Long.valueOf(s));
				}
				setManagerQQList(list);
			}
		}
		//设置use_token
		if (StringUtils.isNotBlank(getKEY())) {
			setUSE_TOKEN(true);
		}
		if (CQ_MSG.equalsIgnoreCase("true")) {
			DO_CQ_MSG = true;
		}
	}
}
