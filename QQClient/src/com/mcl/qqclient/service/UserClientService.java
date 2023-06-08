package com.mcl.qqclient.service;

import com.mcl.qqcommon.Message;
import com.mcl.qqcommon.MessageType;
import com.mcl.qqcommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @projectName: QQClient
 * @package: com.mcl.qqclient.service
 * @className: UserClientService
 * @author: 马仓龙
 * @description: 该类完成用户登陆验证和用户注册等功能
 * @date: 2023/3/19 11:34
 * @version: 1.0
 */
public class UserClientService {

    private String ip = "192.168.196.8";
    //我们可能在其他地方使用 use信息，因此做成成员属性
    private User user = new User();
    //因为Socket在其他地方也可能使用，因此做成属性
    private Socket socket;

    /**
     * @param userId: 用户名
     * @param pwd: 密码
     * @return void
     * @description 用户注册
     */
    public boolean registerUser(String userId, String pwd) {
        boolean issuc = false;
        user.setUserId(userId);
        user.setPasswd(pwd);
        user.setUserType(MessageType.MESSAGE_REGISTER);
        // 发送到服务器
        try {
            socket = new Socket(InetAddress.getByName(ip), 9999);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(user);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //从服务器接收返回数据信息
        try {
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());//有问题******************
            Message message = (Message)ois.readObject();
            if (message.getMesType().equals(MessageType.MESSAGE_REGISTER_SUCCEED)){
                issuc = true;
            }else if (message.getMesType().equals(MessageType.MESSAGE_REGISTER_FAIL)){
                System.out.println(message.getContent());
                issuc = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return issuc;
    }

    /*
     * @param userId:
    	 * @param pwd:
     * @return boolean
     * @description 根据 userId 和 pwa 到服务器验证该用户是否合法
     */
    public boolean checkUser(String userId, String pwd) {
        boolean b = false;
        // 创建一个 User 对象
        user.setUserId(userId);
        user.setPasswd(pwd);
        user.setUserType(MessageType.MESSAGE_LOGON);

        //连接到服务器，发送user对象
        try {
            socket = new Socket(InetAddress.getByName(ip), 9999);
            //得到 ObjectOutputStream 对象
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(user);//发送user对象

            //读取从服务器端回复的Message对象
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Message message = (Message)ois.readObject();

            if (message.getMesType().equals(MessageType.MESSAGE_LOGIN_SUCCEED)) { //登陆成功
                //创建一个和服务器端保持通讯的线程 -->创建一个线程类 ClientConnectServerThread
                ClientConnectServerThread clientConnectServerThread = new ClientConnectServerThread(socket);
                //启动客户端线程
                clientConnectServerThread.start();
                //这里为了后面客户端的扩展，将线程放入到集合中进行管理
                ManageClientConnectServeThreads.addClientConnectServerThread(userId,clientConnectServerThread);
                b = true;
            }else { //登陆失败
                //若登陆失败，则无法启动和服务器通讯的线程，则需要关闭 socket
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return b;
    }

    /*
     * @param :
     * @return void
     * @description 向服务器发送请求在线用户列表
     */
    public void onlineFriendList() {
        // 发送一个 Message ，类型 MESSAGE_GET_ONLINE_FRIEND
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_GET_ONLINE_FRIEND);
        message.setSender(user.getUserId());

        //发送给服务器
        try {
            //从管理线程的集合中得到当前对象的线程
            ClientConnectServerThread clientConnectServerThread =
                    ManageClientConnectServeThreads.getClientConnectServerThread(user.getUserId());
            //得到 userId 对应的线程持有的 Socket
            Socket socket = clientConnectServerThread.getSocket();
            //得到当前 Socket 对应的 ObjectOutputStream 对象
            ObjectOutputStream oos =
                    new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);//发送 Message 对象，向服务端获取在线用户列表
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //编写方法，退出客户端，并给服务端发送一个退出系统的 Message 对象
    public void logout(){
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_CLIENT_EXIT);
        message.setSender(user.getUserId());//指定我是那个客户端，因为要把这个客户端对应的线程移除

        //发送 Message
        try {
//            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectOutputStream oos =
                    new ObjectOutputStream(ManageClientConnectServeThreads.getClientConnectServerThread(user.getUserId()).getSocket().getOutputStream());
            oos.writeObject(message);
            System.out.println(user.getUserId() + "退出系统");
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
