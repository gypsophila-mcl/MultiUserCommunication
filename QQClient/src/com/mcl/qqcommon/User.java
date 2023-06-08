package com.mcl.qqcommon;

import java.io.Serializable;

/**
 * @projectName: QQServer
 * @package: com.mcl.qqcommon
 * @className: User
 * @author: 马仓龙
 * @description: 表示一个用户信息
 * @date: 2023/3/19 10:53
 * @version: 1.0
 */
@SuppressWarnings("all")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String userId;//用户ID/用户名
    private String passwd;//密码
    private String userType;//用户类型(登录还是注册)

    public User() {
    }

    public User(String userId, String passwd) {
        this.userId = userId;
        this.passwd = passwd;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
