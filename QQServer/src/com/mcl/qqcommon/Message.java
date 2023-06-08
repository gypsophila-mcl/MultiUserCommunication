package com.mcl.qqcommon;

import java.io.Serializable;

/**
 * @projectName: QQServer
 * @package: com.mcl.qqcommon
 * @className: Message
 * @author: 马仓龙
 * @description: 表示客户端和服务器端通讯时的消息对象
 * @date: 2023/3/19 10:53
 * @version: 1.0
 */
@SuppressWarnings("all")
public class Message implements Serializable {
    private static final long serialVersionUID = 2L;
    private String sender; //发送方
    private String getter; //接收者
    private String content; //消息内容
    private String sendTime; //发送时间
    private String mesType; //消息类型[在接口中定义消息类型]

    //进行扩展，与文件相关的
    private byte[] fileBytes;
    private int fileLen = 0;
    private String dest;//将文件传输到哪里
    private String src;//原文件路径

    public Message() {
    }

    public Message(String sender, String getter, String content, String sendTime) {
        this.sender = sender;
        this.getter = getter;
        this.content = content;
        this.sendTime = sendTime;
    }

    public String getMesType() {
        return mesType;
    }

    public void setMesType(String mesType) {
        this.mesType = mesType;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getGetter() {
        return getter;
    }

    public void setGetter(String getter) {
        this.getter = getter;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public byte[] getFileBytes() {
        return fileBytes;
    }

    public void setFileBytes(byte[] fileBytes) {
        this.fileBytes = fileBytes;
    }

    public int getFileLen() {
        return fileLen;
    }

    public void setFileLen(int fileLen) {
        this.fileLen = fileLen;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

}
