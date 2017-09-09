package com.wjyup.coolq.controller;

import com.google.gson.Gson;
import com.wjyup.coolq.entity.RequestData;
import com.wjyup.coolq.util.MessageHandle;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.DataInputStream;
import java.net.URLDecoder;

/**
 * 消息接收Controller
 * 
 * @author WJY
 *
 */
@RestController
@Scope("prototype")
public class IndexController {
	// 聊天记录单独存放
	private Logger log = Logger.getLogger("chat");

	@Resource
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;

	@RequestMapping(value = "index")
	public void index() {
		System.out.println("index");
	}

	@RequestMapping(value = "/coolq", produces = "application/json;charset=UTF-8")
	public String coolq(HttpServletRequest request) {
		RequestData requestData = null;
		try {
			// 消息长度
			int len = request.getContentLength();
			// 创建消息长度的字节数组
			byte[] originData = new byte[len];
			// 写入数据
			DataInputStream in = new DataInputStream(request.getInputStream());
			in.readFully(originData);
			in.close();
			// 得到Json消息
			String str = new String(originData);
			// 转码
			String str1 = URLDecoder.decode(str, "UTF-8");
//			String str1 = new String(str.getBytes("GBK"),"utf8");
			log.info("接收消息：" + str1);
			Gson g = new Gson();
			// json转对象（消息对象）
			requestData = g.fromJson(str1, RequestData.class);
			log.info("消息内容："+requestData.getMsg());
			// 交给线程处理消息
			MessageHandle handle = new MessageHandle(requestData);
			// 手动启用
			// Thread th = new Thread(handle);
			// th.start();
			// 交给Spring管理线程
			threadPoolTaskExecutor.execute(handle);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return "[]";
		/*
		 * CQSDK.sendPrivateMsg("1066231345", requestData.getConvMsg()); //发消息测试
		 * Data data = new Data(1066231345l,
		 * requestData.getConvMsg(),"sendPrivateMsg");
		 * log.info("返回的消息："+data.toArrayJson()); //返回信息 return
		 * data.toArrayJson();
		 */
	}

}
