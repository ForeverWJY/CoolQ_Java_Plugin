package com.wjyup.coolq.util;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * TCP Server 端，用于接收插件的消息
 * Created by WJY on 2017/7/8.
 */
public class TCPServer implements Runnable{
    private Logger log = Logger.getLogger(getClass());
    public void start(){
        try {
            log.info("开始启动Socket服务！");
            //创建一个服务器端的Socket，即ServerSocket,绑定需要监听的端口
            ServerSocket serverSocket = new ServerSocket(ConfigCache.SOCKET_PORT);
            Socket socket = null;
            //记录连接过服务器的客户端数量
//            int count = 0;
//            System.out.println("***服务器即将启动，等待客户端的连接***");
            while(true){//循环侦听新的客户端的连接
                //调用accept（）方法侦听，等待客户端的连接以获取Socket实例
                socket = serverSocket.accept();
                //创建新线程
                Thread thread = new Thread(new ServerThread(socket));
                thread.start();

//                count++;
//                System.out.println("服务器端被连接过的次数："+count);
//                InetAddress address = socket.getInetAddress();
//                System.out.println("当前客户端的IP为："+address.getHostAddress());
            }
            //serverSocket.close();一直循环监听，不用关闭连接
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        }
    }

    @Override
    public void run() {
        start();
    }

    public static void main(String[] args) {
        new TCPServer().start();
    }
}
