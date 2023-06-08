package com.mcl.qqframe;

import com.mcl.qqserver.service.QQServer;

/**
 * @projectName: QQServer
 * @package: com.mcl.qqframe
 * @className: QQFrame
 * @author: 马仓龙
 * @description: 该类创建了QQServer对象，启动后台服务
 * @date: 2023/3/19 20:58
 * @version: 1.0
 */
public class QQFrame {
    public static void main(String[] args) {
        QQServer qqServer = new QQServer();
    }
}
