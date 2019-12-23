package com.acotrun.bean;

public class Schedule {
    private String sc_name;  //活动名称
    private String sc_time;  //时间
    private String sc_rem;      //提醒方式
    private String sc_state; //描述
    private String sc_kind;    //计划表类别
    private String sc_model;    //计划模式
    /*
    运动计划表 sc_model = 1
    作息计划表 sc_model = 2
     */

    public String getSc_name() {
        return sc_name;
    }
    public void setSc_name(String sc_name) {
        this.sc_name = sc_name;
    }

    public String getSc_rem() {
        return sc_rem;
    }
    public void setSc_rem(String sc_rem) {
        this.sc_rem = sc_rem;
    }

    public String getSc_state() {
        return sc_state;
    }
    public void setSc_state(String sc_state) {
        this.sc_state = sc_state;
    }

    public String getSc_time() {
        return sc_time;
    }
    public void setSc_time(String sc_time) {
        this.sc_time = sc_time;
    }

    public String getSc_model() {
        return sc_model;
    }
    public void setSc_model(String sc_model) {
        this.sc_model = sc_model;
    }

    public String getSc_kind() {
        return sc_kind;
    }
    public void setSc_kind(String sc_kind) {
        this.sc_kind = sc_kind;
    }

}
