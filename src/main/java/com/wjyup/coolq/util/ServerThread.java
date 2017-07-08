package com.wjyup.coolq.util;
import com.google.gson.Gson;
import com.wjyup.coolq.entity.RequestData;
import com.wjyup.coolq.util.service.IMenuService;
import org.apache.log4j.Logger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;

/**
 * tcp server 处理收到的消息
 * Created by WJY on 2017/7/8.
 */
public class ServerThread implements Runnable{
    private Logger log = Logger.getLogger(getClass());

    private Socket socket = null;//和本线程相关的Socket
    private IMenuService menuService = null;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
        public void run() {
        InputStream is = null;
//        InputStreamReader isr = null;
//        BufferedReader br = null;
        OutputStream os = null;
        PrintWriter pw = null;
        String msg = null;
        try {
            //与客户端建立通信，获取输入流，读取取客户端提供的信息
            is = socket.getInputStream();
            int byteLen = is.available();
            if(byteLen != 0){
//                isr = new InputStreamReader(is,"UTF-8");
//                br = new BufferedReader(isr);
                DataInputStream dis = new DataInputStream(is);
                byte[] b1 = new byte[byteLen];
                dis.read(b1);
                String data = new String(b1);
                // 转码
                msg = URLDecoder.decode(data, "UTF-8");
                log.info("收到消息"+msg);
            }
            socket.shutdownInput();//关闭输入流

            //获取输出流，响应客户端的请求
            os = socket.getOutputStream();
            pw = new PrintWriter(os);
            pw.write("[]");
            pw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //关闭资源即相关socket
            try {
                if(pw!=null)
                    pw.close();
                if(os!=null)
                    os.close();
//                if(br!=null)
//                    br.close();
//                if(isr!=null)
//                    isr.close();
                if(is!=null)
                    is.close();
                if(socket!=null)
                    socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //调用消息处理
            Gson g = new Gson();
            // json转对象（消息对象）
            RequestData requestData = g.fromJson(msg, RequestData.class);
            log.info("消息内容："+requestData.getMsg());
            // 交给线程处理消息
            MessageHandle handle = new MessageHandle(requestData);
            // 手动启用
            // Thread th = new Thread(handle);
            // th.start();
            // 交给Spring管理线程
            ThreadPoolTaskExecutor threadPoolTaskExecutor = SpringApplicationContextHolder.getBean("threadPoolTaskExecutor");
            threadPoolTaskExecutor.execute(handle);
        }

    }

}
