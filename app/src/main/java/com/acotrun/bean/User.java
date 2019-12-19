package com.acotrun.bean;

/**
 * Created by XHDY on 2017/9/10.
 */
public class User {

    private String u_id;	     //用户id
    private String u_password; //密码
    private String u_img;		 //头像
    private String u_nickname; //昵称
    private String u_sex;       //性别
    private int u_age;			//年龄
    private int u_model;		//用户类别
    private String operation;
    private String u_entry1;		//养生词条1
    private String u_entry2;		//养生词条2
    private String u_entry3;		//养生词条3


    public String getU_id() {
        return u_id;
    }
    public String getOperation() {
        return operation;
    }
    public void setOperation(String operation) {
        this.operation = operation;
    }
    public void setU_id(String u_id) {
        this.u_id = u_id;
    }
    public String getU_nickname() {
        return u_nickname;
    }
    public void setU_nickname(String u_nickname) {
        this.u_nickname = u_nickname;
    }
    public String getU_img() {
        return u_img;
    }
    public void setU_img(String u_img) {
        this.u_img = u_img;
    }
    public int getU_age() {
        return u_age;
    }
    public void setU_age(int u_age) {
        this.u_age = u_age;
    }
    public int getU_model() {
        return u_model;
    }
    public void setU_model(int u_model) {
        this.u_model = u_model;
    }
    public void setU_password(String u_password) {
        this.u_password = u_password;
    }
    public String getU_password() {
        return u_password;
    }

    public String getU_sex() {
        return u_sex;
    }

    public void setU_sex(String u_sex) {
        this.u_sex = u_sex;
    }

    public String getU_entry1() {
        return u_entry1;
    }

    public void setU_entry1(String u_entry1) {
        this.u_entry1 = u_entry1;
    }

    public String getU_entry2() {
        return u_entry2;
    }

    public void setU_entry2(String u_entry2) {
        this.u_entry2 = u_entry2;
    }

    public String getU_entry3() {
        return u_entry3;
    }

    public void setU_entry3(String u_entry3) {
        this.u_entry3 = u_entry3;
    }
}
