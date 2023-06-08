package com.mcl.qqclient.service;

import com.mcl.qqcommon.Message;
import com.mcl.qqcommon.MessageType;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * @projectName: QQClient
 * @package: com.mcl.qqclient.service
 * @className: ClientConnectServerThread
 * @author: 马仓龙
 * @description: TODO
 * @date: 2023/3/19 11:55
 * @version: 1.0
 */
public class ClientConnectServerThread extends Thread {
    //该线程需要持有 Socket
    private Socket socket;

    //给构造器可以接受一个 Socket 对象
    public ClientConnectServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        // 因为线程需要在后台和服务器通讯，因此做成一个无限循环(while循环)
        while (true) {
            //一直读取从服务端发来的消息
            System.out.println("客户端线程，等待读取从服务端发送的消息");
//            System.out.println(socket);
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                //如果服务器没有发送 Message 对象，线程会阻塞在这里
                Message message = (Message)ois.readObject();
                //判断 Message 类型，然后做相应的业务处理
                //如果时读取到的是服务端返回的在线用户列表
                if (message.getMesType().equals(MessageType.MESSAGE_RET_ONLINE_FRIEND)) {
                    //取出在线列表信息
                    String[] onlineUsers = message.getContent().split(" ");
                    System.out.println("========= 在线用户列表如下 =========");
                    for (String onlineUser : onlineUsers) {
                        System.out.println("用户：" + onlineUser);
                    }
                }else if (message.getMesType().equals(MessageType.MESSAGE_COMM_MES)) {
                    //普通聊天消息(直接显示在控制台)
                    System.out.println("\n" + message.getSender() +  " 对 " + message.getGetter()
                            + " 说 " + message.getContent());
                    System.out.println(message.getSendTime());
                }else if (message.getMesType().equals(MessageType.MESSAGE_TO_ALL)) {
                    //群发聊天消息(直接显示在控制台)
                    System.out.println("\n" + message.getSender() +  " 对大家说 " + message.getContent());
                    System.out.println(message.getSendTime());
                }else if (message.getMesType().equals(MessageType.MESSAGE_FILE)) {
                    //文件消息，需要接收文件，存在电脑上
                    String dest = message.getDest();
                    byte[] fileBytes = message.getFileBytes();
                    FileOutputStream fos = new FileOutputStream(dest);
                    fos.write(fileBytes);
                    System.out.println("\n" + message.getSender() + " 给 " + message.getGetter()
                            + " 发送文件：" + message.getSrc() + " 到你的电脑目录：" + dest);
                }else {
                    //其他类型的Message则先不进行处理
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Socket getSocket() {
        return socket;
    }
}
