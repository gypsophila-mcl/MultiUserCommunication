package com.mcl.qqserver.service;

import java.util.HashMap;
import java.util.Iterator;

/**
 * @projectName: QQServer
 * @package: com.mcl.qqserver.service
 * @className: ManageClientThreads
 * @author: 马仓龙
 * @description: 用于管理和客户端通讯的线程
 * @date: 2023/3/19 20:46
 * @version: 1.0
 */
@SuppressWarnings("all")
public class ManageClientThreads {
    //我们把多个线程放入到 HashMap 集合中，key 就是用户id， value 就是线程
    private static HashMap<String, ServerConnectClientThread> hm = new HashMap<>();

    //添加线程对象到 hm 集合
    public static void addClientThread(String userId, ServerConnectClientThread serverConnectClientThread) {
        hm.put(userId, serverConnectClientThread);
    }

    //返回 HashMap 集合
    public static HashMap<String, ServerConnectClientThread> getHm() {
        return hm;
    }

    //根据 userId 返回 ServerConnectClientThread 线程
    public static ServerConnectClientThread getServerConnectClientThread(String userId) {
        return hm.get(userId);
    }

    //返回在线用户列表(用空格隔开)
    public static String getOnlineUser(){
        //集合遍历，遍历 HashMap 的 key
        Iterator<String> iterator = hm.keySet().iterator();
        String onlineUserList = "";
        while (iterator.hasNext()) {
            onlineUserList += iterator.next().toString() + " ";
        }
        return onlineUserList;
    }

    //从集合中移除某个用户以及线程对象
    public static void removeClientThread(String userId) {
        hm.remove(userId);
    }

}
