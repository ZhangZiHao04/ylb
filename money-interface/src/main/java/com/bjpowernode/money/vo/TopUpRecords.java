package com.bjpowernode.money.vo;

import java.io.Serializable;
import java.util.Date;

public class TopUpRecords implements Serializable {
    private Date time;
    private String desc;
    private String money;
    private String status;

    public TopUpRecords() {
    }

    public TopUpRecords(Date time, String desc, String money, String status) {
        this.time = time;
        this.desc = desc;
        this.money = money;
        this.status = status;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
