package com.wjyup.coolq.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
/**
 * WebSocket工具类
 * @author WJY
 *
 */
public class WebSocketUtil {
	private static Logger log = Logger.getLogger(WebSocketUtil.class);
	//websocket接收握手返回数据大小
	private static int RCVBUF = 16384;
	
	private static Socket socket = null;
	private static OutputStream out = null;
	private static InputStream in = null;

	/**
	 * 初始化ws
	 */
	private static void initWS(){
		try {
			socket = new Socket(ConfigCache.WSHost, Integer.parseInt(ConfigCache.WSPort));
			out = socket.getOutputStream();
			in = socket.getInputStream();

			// 握手
			StringBuffer header = new StringBuffer();
			header.append("GET ws://" + ConfigCache.WSHost + ":" + ConfigCache.WSPort + "/ HTTP/1.1\r\n");
			header.append("Host: " + ConfigCache.WSHost + ":" + ConfigCache.WSPort + "\r\n");
			header.append("Connection: Upgrade\r\n");
			header.append("Upgrade: websocket\r\n");
			header.append("Sec-WebSocket-Version: 13\r\n");
			header.append("Sec-WebSocket-Key: d359Fdo6omyqfxyYF7Yacw==\r\n");
			header.append("\r\n");
			out.write(header.toString().getBytes());
			out.flush();

			// 读取握手数据
			byte[] rawbuffer = new byte[RCVBUF];
			in.read(rawbuffer);
			String rb = new String(rawbuffer);
			log.info("握手数据:" + rb);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 发送json数据并获取返回值
	 * @param message
	 * @return
	 */
	public static String sendSocketData(String message){
		try {
            initWS();
			byte[] sendByte = encode(message.getBytes("UTF-8"));
			log.info("sendMsg:"+new String(sendByte));
			// 发送编码后的数据
			out.write(sendByte);
			out.flush();

			// 获取返回值
			int len = 0;
			// 迭代次数
			int count = 0;
			do {
				//防止超时
				if (count > 50) {
					break;
				}
				len = in.available();
				if(len > 5){
					byte[] rawbuffer = new byte[len];
					in.read(rawbuffer);
					String result= new String(rawbuffer,"UTF-8");
					log.info("返回结果:" + result);
					return result;
				}
				count++;
				Thread.sleep(200);
			} while (len == 0);
            closeWS();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
	
	public static void closeWS(){
		try {
			out.close();
			in.close();
			socket.close();
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * 直接调用插件的websocket接口
	 * @param message json数据
	 */
	public static String sendSocketData1(String message){
		Socket client = null;
		String result = null;
		try {
			if(StringUtils.isBlank(message)){
				return null;
			}
			client = new Socket(ConfigCache.WSHost, Integer.parseInt(ConfigCache.WSPort));
			OutputStream out = client.getOutputStream();
			InputStream in = client.getInputStream();

			// 握手
			StringBuffer header = new StringBuffer();
			header.append("GET ws://" + ConfigCache.WSHost + ":" + ConfigCache.WSPort + "/ HTTP/1.1\r\n");
			header.append("Host: " + ConfigCache.WSHost + ":" + ConfigCache.WSPort + "\r\n");
			header.append("Connection: Upgrade\r\n");
			header.append("Upgrade: websocket\r\n");
			header.append("Sec-WebSocket-Version: 13\r\n");
			header.append("Sec-WebSocket-Key: d359Fdo6omyqfxyYF7Yacw==\r\n");
			header.append("\r\n");
			out.write(header.toString().getBytes());
			out.flush();

			// 读取握手数据
			byte[] rawbuffer = new byte[RCVBUF];
			in.read(rawbuffer);
			String rb = new String(rawbuffer);
			log.info("握手数据:" + rb);
			
			log.info("发送消息："+message);
			byte[] sendByte = encode(message.getBytes("UTF-8"));
			System.out.println("sendByte:"+new String(sendByte));
			// 发送编码后的数据
			out.write(sendByte);
			out.flush();

			// 获取返回值
			int len = 0;
			// 迭代次数
			int count = 0;
			do {
				//防止超时
				if (count > 50) {
					break;
				}
				len = in.available();
				if(len > 5){
					rawbuffer = new byte[len];
					in.read(rawbuffer);
					result= new String(rawbuffer,"UTF-8");
					log.info("返回结果:" + result);
					return result;
				}
				count++;
				Thread.sleep(200);
			} while (len == 0);
			log.info("ws即将关闭！");
			client.close();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			try {
				if (client != null) {
					client.close();
				}
			} catch (Exception e) {
				log.error("Close the IO session error: ", e);
			}
		}
		return null;
	}
	
	/**
	 * 转码方法
	 * 
	 * @return
	 */
	public static byte[] encode(byte[] data) {
		ByteBuffer mes = ByteBuffer.wrap(data);
		boolean mask = true; // framedata.getTransfereMasked();
		int sizebytes = mes.remaining() <= 125 ? 1 : mes.remaining() <= 65535 ? 2 : 8;
		ByteBuffer buf = ByteBuffer
				.allocate(1 + (sizebytes > 1 ? sizebytes + 1 : sizebytes) + (mask ? 4 : 0) + mes.remaining());
		byte optcode = 1;
		byte one = (byte) -128;
		one |= optcode;
		buf.put(one);
		byte[] payloadlengthbytes = toByteArray(mes.remaining(), sizebytes);
		assert (payloadlengthbytes.length == sizebytes);

		if (sizebytes == 1) {
			buf.put((byte) ((byte) payloadlengthbytes[0] | (mask ? (byte) -128 : 0)));
		} else if (sizebytes == 2) {
			buf.put((byte) ((byte) 126 | (mask ? (byte) -128 : 0)));
			buf.put(payloadlengthbytes);
		} else if (sizebytes == 8) {
			buf.put((byte) ((byte) 127 | (mask ? (byte) -128 : 0)));
			buf.put(payloadlengthbytes);
		} else
			throw new RuntimeException("Size representation not supported/specified");

		if (mask) {
			ByteBuffer maskkey = ByteBuffer.allocate(4);
			Random reuseableRandom = new Random();
			maskkey.putInt(reuseableRandom.nextInt());
			buf.put(maskkey.array());
			for (int i = 0; mes.hasRemaining(); i++) {
				buf.put((byte) (mes.get() ^ maskkey.get(i % 4)));
			}
		} else
			buf.put(mes);
		// translateFrame ( buf.array () , buf.array ().length );
		assert (buf.remaining() == 0) : buf.remaining();
		buf.flip();
		return buf.array();
	}

	/**
	 * @param val
	 * @param bytecount
	 * @return
	 */
	private static byte[] toByteArray(long val, int bytecount) {
		byte[] buffer = new byte[bytecount];
		int highest = 8 * bytecount - 8;
		for (int i = 0; i < bytecount; i++) {
			buffer[i] = (byte) (val >>> (highest - 8 * i));
		}
		return buffer;
	}
	
	/**
	 * 调用php的websocket接口
	 * @param message
	 */
	public static String getPHPData(String message){
		try {
			if(StringUtils.isBlank(message)){
				return null;
			}
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("data", message);
			Document doc = Jsoup.connect(ConfigCache.PHP_WS_URL).data(map).timeout(5000).post();
			if(doc != null && StringUtils.isNotBlank(doc.text())){
				return doc.text();
			}
		} catch (Exception ex) {
			log.error("调用php的websocket接口 exception: " + ex.getMessage());
		}
		return null;
	}

}
