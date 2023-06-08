package com.mcl.qqclient.service;

import com.mcl.qqcommon.Message;
import com.mcl.qqcommon.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;

/**
 * @projectName: QQClient
 * @package: com.mcl.qqclient.service
 * @className: MessageClientService
 * @author: 马仓龙
 * @description: 该类/对象，提供和消息相关的服务方法
 * @date: 2023/3/20 19:45
 * @version: 1.0
 */
public class MessageClientService {

    /**
     * @param content: 内容
     * @param senderId: 发送者
     * @return void
     * @description 群发消息
     */
    public void sendMessageToAll(String content, String senderId) {
        //构建 Message
        Message message = new Message();
        message.setSender(senderId);
        message.setContent(content);
        message.setMesType(MessageType.MESSAGE_TO_ALL);//消息类型为群发
        message.setSendTime(new Date().toString());//发送时间设置到 message 对象
        System.out.println(senderId + " 对大家说 " + content);
        //发送给服务端
        try {
            ObjectOutputStream oos =
                    new ObjectOutputStream(ManageClientConnectServeThreads.getClientConnectServerThread(senderId).getSocket().getOutputStream());
            oos.writeObject(message);
            System.out.println(message.getSendTime());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * @param content: 内容
     * @param senderId: 发送者
     * @param getterId: 接收者
     * @return void
     * @description 私聊消息
     */
    public void sendMessageToOne(String content, String senderId, String getterId){
        //构建 Message
        Message message = new Message();
        message.setSender(senderId);
        message.setGetter(getterId);
        message.setContent(content);
        message.setMesType(MessageType.MESSAGE_COMM_MES);//消息类型为普通
        message.setSendTime(new Date().toString());//发送时间设置到 message 对象
        System.out.println(senderId + " 对 " + getterId + " 说 " + content);
        //发送给服务端
        try {
            ObjectOutputStream oos =
                    new ObjectOutputStream(ManageClientConnectServeThreads.getClientConnectServerThread(senderId).getSocket().getOutputStream());
            oos.writeObject(message);
            System.out.println(message.getSendTime());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
