package coolq;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Random;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.wjyup.coolq.entity.Data;

public class SocketClientTest {
	private static final Logger log = Logger.getLogger(SocketClientTest.class);

	//websocket接收握手返回数据大小
	private int RCVBUF = 16384;
	
	private Socket socket = null;
	private OutputStream out = null;
	private InputStream in = null;
	
	public String client(String message) {
		Socket client = null;
		String result = null;
		String host = "127.0.0.1";
		int port = 1970;
		try {
			client = new Socket(host, port);
			OutputStream out = client.getOutputStream();
			InputStream in = client.getInputStream();

			// 握手
			StringBuffer header = new StringBuffer();
			header.append("GET ws://" + host + ":" + port + "/ HTTP/1.1\r\n");
			header.append("Host: " + host + ":" + port + "\r\n");
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
					rawbuffer = new byte[len];
					in.read(rawbuffer);
					result= new String(rawbuffer,"UTF-8");
					log.info("返回结果:" + result);
					return result;
				}
				count++;
				Thread.sleep(200);
			} while (len == 0);
			log.info("即将关闭！");
			client.close();
		} catch (Exception e) {
			e.printStackTrace();
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

	public static void main(String[] args) {
		SocketClientTest sc = new SocketClientTest();
//		Data data = new Data(1066231345l,"hi！你好","sendPrivateMsg");
//		Data data = new Data("getCookies");
		Data data = new Data("getLoginNick");
		String json = data.toJson();
//		json = utf8ToUnicode(json);
		System.out.println(json);
		sc.client(data.toJson());
	}
	
	/**
	 * 初始化ws
	 */
	private void initWS(){
		String host = "127.0.0.1";
		int port = 1970;
		try {
			this.socket = new Socket(host, port);
			out = this.socket.getOutputStream();
			in = this.socket.getInputStream();

			// 握手
			StringBuffer header = new StringBuffer();
			header.append("GET ws://" + host + ":" + port + "/ HTTP/1.1\r\n");
			header.append("Host: " + host + ":" + port + "\r\n");
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
			e.printStackTrace();
		}
	}
	
	/**
	 * 发送json数据并获取返回值
	 * @param message
	 * @return
	 */
	private String sendData(String message){
		try {
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Test
	public void waitSocketConnect(){
		try {
			initWS();
			Data data = new Data("getCookies");
			sendData(data.toJson());
			Thread.sleep(3000);
			data = new Data("getLoginNick");
			sendData(data.toJson());
			Thread.sleep(3000);
			data = new Data(1066231345l,"hi！你好","sendPrivateMsg");
			sendData(data.toJson());
			out.close();
			in.close();
			socket.close();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 转码方法
	 * 
	 * @return
	 */
	public byte[] encode(byte[] data) {
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
	private byte[] toByteArray(long val, int bytecount) {
		byte[] buffer = new byte[bytecount];
		int highest = 8 * bytecount - 8;
		for (int i = 0; i < bytecount; i++) {
			buffer[i] = (byte) (val >>> (highest - 8 * i));
		}
		return buffer;
	}

}
