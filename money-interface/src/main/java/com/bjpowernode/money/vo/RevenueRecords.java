package com.bjpowernode.money.vo;

import java.io.Serializable;
import java.util.Date;

public class RevenueRecords implements Serializable {
    private Date time;
    private String rMoney;
    private String name;
    private String iMoney;
    private String status;

    public RevenueRecords() {
    }

    public RevenueRecords(Date time, String rMoney, String name, String iMoney, String status) {
        this.time = time;
        this.rMoney = rMoney;
        this.name = name;
        this.iMoney = iMoney;
        this.status = status;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getrMoney() {
        return rMoney;
    }

    public void setrMoney(String rMoney) {
        this.rMoney = rMoney;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getiMoney() {
        return iMoney;
    }

    public void setiMoney(String iMoney) {
        this.iMoney = iMoney;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
