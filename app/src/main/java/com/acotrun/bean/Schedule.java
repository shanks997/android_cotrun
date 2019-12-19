package com.acotrun.bean;

/**
 * Created by XHDY on 2017/10/15.
 */
public class Schedule {
    private int sc_no;       //
    private String sc_name;  //活动名称
    private String sc_time;  //时间
    private int sc_rem;     //提醒方式
    private int sc_mode;    //计划模式
    private String u_id;     //用户id
    private String sc_state; //描述
    private String operation;//


    public int getSc_no() {
        return sc_no;
    }

    public void setSc_no(int sc_no) {
        this.sc_no = sc_no;
    }

    public String getSc_name() {
        return sc_name;
    }

    public void setSc_name(String sc_name) {
        this.sc_name = sc_name;
    }


    public int getSc_rem() {
        return sc_rem;
    }

    public void setSc_rem(int sc_rem) {
        this.sc_rem = sc_rem;
    }

    public int getSc_mode() {
        return sc_mode;
    }

    public void setSc_mode(int sc_mode) {
        this.sc_mode = sc_mode;
    }

    public String getU_id() {
        return u_id;
    }

    public void setU_id(String u_id) {
        this.u_id = u_id;
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

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
}
