package com.acotrun.bean;

public class User {

    private static String u_id;	           //用户id
    private static String u_pwd;           //密码
    private static String u_avatar;        //头像
    private static String u_sex;			//性别
    private static String u_model;		    //用户类别
    private static boolean u_login;        //是否登录

    private static User instance = null;
    private User() {
        u_model = "0";
        u_login = false;
    }
    public static User getInstance() {
        if (instance == null) {
            instance = new User();
        }
        return instance;
    }
    /*
    普通用户 u_model = 0
    Android管理员 u_model = 1
     */

    public String getU_id() {
        return u_id;
    }
    public void setU_id(String id) {
        u_id = id;
    }

    public void setU_pwd(String pwd) {
        u_pwd = pwd;
    }
    public String getU_pwd() {
        return u_pwd;
    }

    public void setU_avatar(String avatar) {
        u_avatar = avatar;
    }
    public String getU_avatar() {
        return u_avatar;
    }

    public void setU_sex(String sex) {
        u_sex = sex;
    }
    public String getU_sex() {
        return u_sex;
    }

    public String getU_model() {
        return u_model;
    }
    public void setU_model(String model) {
        u_model = model;
    }

    public boolean getU_login() {
        return u_login;
    }
    public void setU_login(boolean login) {
        u_login = login;
    }

}
