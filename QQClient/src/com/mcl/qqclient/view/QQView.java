package com.mcl.qqclient.view;

import com.mcl.qqclient.service.FileClientService;
import com.mcl.qqclient.service.MessageClientService;
import com.mcl.qqclient.service.UserClientService;
import com.mcl.qqclient.utils.Utility;

/**
 * @projectName: QQClient
 * @package: com.mcl.qqclient.view
 * @className: QQView
 * @author: 马仓龙
 * @description: 客户端菜单界面
 * @date: 2023/3/19 11:07
 * @version: 1.0
 */
@SuppressWarnings("all")
public class QQView {
    private boolean loop = true; //控制是否显示菜单
    private String key = ""; //接收用户的键盘输入
    private UserClientService userClientService = new UserClientService(); //该对象用于登录服务/注册用户
    private MessageClientService messageClientService = new MessageClientService();//对象用户私聊
    private FileClientService fileClientService = new FileClientService();

    public static void main(String[] args) {
        new QQView().mainMenu();
        System.out.println("========== 客户端退出系统 ==========");
        System.exit(0);
    }

    /*
     * @param :
     * @return void
     * @description 显示主菜单
     */
    private void mainMenu(){
        String userId = "";
        String pwd = "";
        while (loop) {
            System.out.println("================== 欢迎登陆网络通信系统 ==================");
            System.out.println("\t\t 1 注册用户");
            System.out.println("\t\t 2 登陆系统");
            System.out.println("\t\t 9 退出系统");
            System.out.print("请输入你的选择：");

            key = Utility.readString(1);

            // 根据用户的输入来处理不同逻辑
            switch (key) {
                case "1":
                    System.out.println("请输入要注册的用户名：");
                    userId = Utility.readString(100);
                    System.out.println("请输入用户密码：");
                    pwd = Utility.readString(50);
                    userClientService.registerUser(userId,pwd);
                    break;
                case "2":
                    System.out.print("请输入用户号：");
                    userId = Utility.readString(50);
                    System.out.print("请输入密 码：");
                    pwd = Utility.readString(50);
                    //需要到服务端验证用户是否合法
                    //有很多代码，在这里编写一个UserClientService[用户登录/注册]
                    if (userClientService.checkUser(userId, pwd)) { //先把逻辑打通
                        //主函数休眠1秒，防止显示过快导致顺序错乱
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("================== 欢迎（用户 " + userId + " 登陆成功）==================");
                        //进入二级菜单
                        while (loop) {
                            System.out.println("\n==================网络通信系统二级菜单（用户 " + userId + "）==================");
                            System.out.println("\t\t 1 显示在线用户列表");
                            System.out.println("\t\t 2 私聊消息");
                            System.out.println("\t\t 3 群发消息");
                            System.out.println("\t\t 4 发送文件");
                            System.out.println("\t\t 9 退出系统");
                            System.out.println("请输入你的选择：");
                            key = Utility.readString(1);
                            String content = "";
                            switch (key) {
                                case "1" :
                                    //这里准备写一个方法获取在线用户列表
                                    //写在 UserClientService 中
                                    userClientService.onlineFriendList();
                                    System.out.println("显示在线用户列表");
                                    break;
                                case "2":
                                    System.out.println("请输入想聊天的用户号(在线)：");
                                    String getterId = Utility.readString(50);
                                    System.out.println("请输入想说的话：");
                                    content = Utility.readString(100);
                                    //写一个方法，将消息发送给服务端
                                    messageClientService.sendMessageToOne(content, userId, getterId);
                                    break;
                                case "3":
                                    System.out.println("请输入想对大家说的话：");
                                    content = Utility.readString(100);
                                    //调用一个方法，将消息封装成 message 对象
                                    messageClientService.sendMessageToAll(content, userId);
                                    break;
                                case "4":
                                    System.out.println("请输入如想要发送文件的对象(在线)：");
                                    getterId = Utility.readString(50);
                                    System.out.println("请输入想要发送的文件路径：");
                                    String src = Utility.readString(100);
                                    System.out.println("请输入想要发送到对方电脑的哪里：");
                                    String dest = Utility.readString(100);
                                    fileClientService.sendFileToOne(src,dest,userId,getterId);
                                    break;
                                case "9":
                                    //调用方法，给服务器发送退出系统的 Message
                                    userClientService.logout();
                                    loop = false;
                                    break;
                            }
                        }
                    }else { //登陆服务器失败
                        System.out.println("========== 登陆失败 ========== ");
                    }
                    break;
                case  "9":
                    loop = false;
                    break;
            }
        }
    }
}
