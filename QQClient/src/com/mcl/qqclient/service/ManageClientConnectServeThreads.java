package com.mcl.qqclient.service;

import java.util.HashMap;

/**
 * @projectName: QQClient
 * @package: com.mcl.qqclient.service
 * @className: ManageClientConnectServeThead
 * @author: 马仓龙
 * @description: 该类管理客户端连接到服务器端的线程的类
 * @date: 2023/3/19 20:09
 * @version: 1.0
 */
public class ManageClientConnectServeThreads {
    //我们把多个线程放入到 HashMap 集合中，key 就是用户id， value 就是线程
    private static HashMap<String, ClientConnectServerThread> hm = new HashMap<>();

    /*
     * @param userId:
    	 * @param clientConnectServerThread:
     * @return void
     * @description 将某个线程加入集合
     */
    public static void addClientConnectServerThread(String userId, ClientConnectServerThread clientConnectServerThread) {
        hm.put(userId, clientConnectServerThread);

    }

    /*
     * @param userId:
     * @return ClientConnectServerThread
     * @description 通过userId可以得到对应线程
     */
    public static ClientConnectServerThread getClientConnectServerThread(String userId) {
        return hm.get(userId);
    }
}
