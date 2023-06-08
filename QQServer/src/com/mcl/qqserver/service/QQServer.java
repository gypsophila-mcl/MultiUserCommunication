package com.mcl.qqserver.service;

import com.mcl.qqcommon.Message;
import com.mcl.qqcommon.MessageType;
import com.mcl.qqcommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @projectName: QQServer
 * @package: com.mcl.qqserver.service
 * @className: QQServer
 * @author: 马仓龙
 * @description: 这是服务端，监听9999端口，等待客户端连接并保持通讯
 * @date: 2023/3/19 20:25
 * @version: 1.0
 */
@SuppressWarnings("all")
public class QQServer {
    private ServerSocket serverSocket = null;
    //创建一个集合存放多个用户，如果是这些用户登录就认为是合法的
    //可以使用 ConcurrentHashMap 处理并发的集合，没有线程安全问题
    // HashMap 没有处理线程安全，因此在多线程情况下是不安全的
    // ConcurrentHashMap 处理了线程安全，及线程同步处理，在多线程情况下是安全的
    private static ConcurrentHashMap<String, User> validUsers = new ConcurrentHashMap<>();

    static {//使用静态代码块初始化 validUsers
        validUsers.put("100",new User("100","123456"));
        validUsers.put("200",new User("200","123456"));
        validUsers.put("300",new User("300","123456"));
        validUsers.put("至尊宝",new User("至尊宝","123456"));
        validUsers.put("紫霞仙子",new User("紫霞仙子","123456"));
        validUsers.put("菩提老祖",new User("菩提老祖","123456"));
    }

    /*
     * @param userId:
    	 * @param passwd:
     * @return boolean
     * @description 验证用户是否有效
     */
    private boolean checkUser(String userId, String passwd) {
        User user = validUsers.get(userId);
        if (user == null) { //说明没有对应的 userId 存在 validUsers 的 key 中
            return false;
        }
        if (!user.getPasswd().equals(passwd)){
            return false;
        }
        return true;
    }

    public QQServer() {
        //注意：端口可以写在配置文件中
        try {
            System.out.println("服务端在9999端口监听......");
            serverSocket = new ServerSocket(9999);
            while (true) { //监听是一直循环的，当和某个客户端及案例连接后，会继续监听
                Socket socket = serverSocket.accept();//如果没有客户端连接，则会阻塞在这里
                //得到socket关联的对象输入流
                ObjectInputStream ois =
                        new ObjectInputStream(socket.getInputStream());
                //读取客户端发送的 user 对象
                User user = (User)ois.readObject();
                //得到socket关联的输出流
                ObjectOutputStream oos =
                        new ObjectOutputStream(socket.getOutputStream());
                //创建一个 Message 对象，准备回复客户端(登陆成功或者失败都要返回)
                Message message = new Message();
                if (user.getUserType().equals(MessageType.MESSAGE_REGISTER)) {//表示这个User对象是注册需求
                    String userId = user.getUserId();
                    if (validUsers.contains(userId)){
                        message.setMesType(MessageType.MESSAGE_REGISTER_FAIL);//注册失败类型
                        message.setContent("已有帐户....");
                    }else {
                        validUsers.put(user.getUserId(),user);//写入
                        message.setMesType(MessageType.MESSAGE_REGISTER_SUCCEED);//注册成功类型
                        System.out.println("账户列表：");
                        showValidUsers();
                    }
                    //将message 回复给客户端
                    oos.writeObject(message);
                    socket.close();
                }
                else if (user.getUserType().equals(MessageType.MESSAGE_LOGON)) {//登陆需求
                    //验证用户，要写个方法
                    if (checkUser(user.getUserId(),user.getPasswd())) {//合法，登陆成功
                        message.setMesType(MessageType.MESSAGE_LOGIN_SUCCEED);//登陆成功类型
                        //创建一个线程，和客户端保持通讯，该线程需要持有 socket 对象
                        ServerConnectClientThread serverConnectClientThread =
                                new ServerConnectClientThread(socket, user.getUserId());
                        //启动该线程
                        serverConnectClientThread.start();
                        //把该线程对象放入到集合中进行管理
                        ManageClientThreads.addClientThread(user.getUserId(), serverConnectClientThread);
                        //将message 回复给客户端
                        oos.writeObject(message);
                    }else { //登陆失败
                        System.out.println("用户 id=" + user.getUserId() + " pwd=" + user.getPasswd() + " 登陆失败！");
                        message.setMesType(MessageType.MESSAGE_LOGIN_FAIL);//登陆失败类型
                        //将message 回复给客户端
                        oos.writeObject(message);
                        //关闭 socket
                        socket.close();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                //如果服务器推出了 while 说明服务器不再监听，因此要关闭 serverSocket
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void showValidUsers(){
        Iterator<String> iterator = validUsers.keySet().iterator();
        while (iterator.hasNext()) {
            String next = iterator.next();
            System.out.println(next);
        }
    }
}
