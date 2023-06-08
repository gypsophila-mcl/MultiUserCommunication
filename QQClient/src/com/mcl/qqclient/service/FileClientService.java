package com.mcl.qqclient.service;

import com.mcl.qqcommon.Message;
import com.mcl.qqcommon.MessageType;

import java.io.*;

/**
 * @author: 马仓龙
 * @version: 1.0
 * @description: 该类/对象，提供和文件传输相关的服务方法
 */
public class FileClientService {

    /**
     * @param src: 本地文件路径
     * @param dest: 目的地文件路径
     * @param senderId: 发送者
     * @param getterId: 接收者
     * @return void
     * @description 给个人发送文件
     */
    public void sendFileToOne(String src,String dest,String senderId, String getterId){
        //读取 src 文件，封装到 Message 对象中
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_FILE);
        message.setGetter(getterId);
        message.setSender(senderId);
        message.setSrc(src);
        message.setDest(dest);

        //需要将文件进行读取
        FileInputStream fis = null;
        byte[] fileBytes = new byte[(int)new File(src).length()];
        //************************************到读取文件，转换成byte数组了**********************************************
        try {
            fis = new FileInputStream(src);
            fis.read(fileBytes);//将 src 文件读入字节数组
            //将文件对应的字节数组，设置到 message对象
            message.setFileBytes(fileBytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //提示信息
        System.out.println("\n" + senderId + " 给 " + getterId
                + " 发送文件：" + src + " 到对方的电脑目录：" + dest);

        //发送
        try {
            ObjectOutputStream oos =
                    new ObjectOutputStream(ManageClientConnectServeThreads.getClientConnectServerThread(senderId).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
