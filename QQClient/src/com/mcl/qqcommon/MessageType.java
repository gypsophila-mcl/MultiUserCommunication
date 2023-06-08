package com.mcl.qqcommon;

public interface MessageType {
    //1.在接口中定义一些常量
    //2.不同常量得知代表不同的消息类型
    String MESSAGE_LOGIN_SUCCEED = "1"; //表示登陆成功
    String MESSAGE_LOGIN_FAIL = "2"; //表示登陆失败
    String MESSAGE_COMM_MES = "3"; //表示普通信息包
    String MESSAGE_GET_ONLINE_FRIEND = "4"; //要求返回在线用户列表
    String MESSAGE_RET_ONLINE_FRIEND = "5"; //返回在线用户列表
    String MESSAGE_CLIENT_EXIT = "6"; //客户端请求退出
    String MESSAGE_TO_ALL = "7"; //群发消息包
    String MESSAGE_FILE = "8"; //发送文件
    String MESSAGE_LOGON = "9";//用户登录
    String MESSAGE_REGISTER = "10";//用户注册
    String MESSAGE_REGISTER_SUCCEED = "11"; //表示注册成功
    String MESSAGE_REGISTER_FAIL = "12"; //表示注册失败
}
