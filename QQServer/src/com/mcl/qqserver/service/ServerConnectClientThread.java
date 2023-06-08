package com.mcl.qqserver.service;

import com.mcl.qqcommon.Message;
import com.mcl.qqcommon.MessageType;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Set;

/**
 * @projectName: QQServer
 * @package: com.mcl.qqserver.service
 * @className: ServerConnectClientThread
 * @author: 马仓龙
 * @description: 该类的一个对象和某个客户端保持通讯
 * @date: 2023/3/19 20:37
 * @version: 1.0
 */
@SuppressWarnings("all")
public class ServerConnectClientThread extends Thread {
    private Socket socket;
    private String userId;//连接到服务端的用户ID

    public ServerConnectClientThread() {
    }


    public ServerConnectClientThread(Socket socket, String userId) {
        this.socket = socket;
        this.userId = userId;
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    public void run() {//这里线程处于 run 状态，可以接收发送消息
        while(true) {
            System.out.println("服务端和客户端 " + userId +" 保持通讯，读取数据......");
            try {
//                System.out.println(socket);
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message)ois.readObject();
                //根据 Message 类型，做相应的业务处理
                if (message.getMesType().equals(MessageType.MESSAGE_GET_ONLINE_FRIEND)) {
                    //客户端要在线用户列表
                    String onlineUser = ManageClientThreads.getOnlineUser();
                    System.out.println(message.getSender() + "正在请求在线客户列表");
                    //返回 Message
                    // 构建一个 Message 对象
                    Message message2 = new Message();
                    message2.setMesType(MessageType.MESSAGE_RET_ONLINE_FRIEND);
                    message2.setContent(onlineUser);
                    message2.setGetter(message.getSender());
                    //写入数据通道，返回给客户端
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(message2);
                }else if (message.getMesType().equals(MessageType.MESSAGE_CLIENT_EXIT)) {
                    System.out.println(message.getSender() + "要退出系统");
                    //将这个客户端对应的线程从集合中删除
                    ManageClientThreads.removeClientThread(message.getSender());
                    socket.close();
                    //退出 while 循环
                    break;
                }else if (message.getMesType().equals(MessageType.MESSAGE_COMM_MES)) {
                    //根据 message 获取 getterId 然后再得到对应线程
                    String getterId = message.getGetter();
                    ServerConnectClientThread getterClientThread =
                            ManageClientThreads.getServerConnectClientThread(getterId);
                    //再得到 socket 对象对应的输出流
                    ObjectOutputStream oos =
                            new ObjectOutputStream(getterClientThread.getSocket().getOutputStream());
                    //将消息进行转发（若客户不在线，可以将消息保存到数据库，实现离线留言（***用户上线的判断））
                    oos.writeObject(message);
                }else if (message.getMesType().equals(MessageType.MESSAGE_TO_ALL)) {
                    HashMap<String, ServerConnectClientThread> hm = ManageClientThreads.getHm();
                    Set<String> keys = hm.keySet();
                    for (String key : keys) {
                        // key 就是当前在线用户ID
                        if (!key.equals(message.getSender())) {
                            //排除群发消息用户
                            ObjectOutputStream oos =
                                    new ObjectOutputStream(hm.get(key).getSocket().getOutputStream());
                            oos.writeObject(message);
                        }
                    }
                }else if (message.getMesType().equals(MessageType.MESSAGE_FILE)) {
                    ServerConnectClientThread serverConnectClientThread = ManageClientThreads.getServerConnectClientThread(message.getGetter());
                    Socket socket = serverConnectClientThread.socket;
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(message);
                }else {
                    //别的类型先不做处理
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
