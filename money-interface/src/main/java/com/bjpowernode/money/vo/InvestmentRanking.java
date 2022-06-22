package com.bjpowernode.money.vo;

import java.io.Serializable;
import java.util.Date;

public class InvestmentRanking implements Serializable {
    private String phone;
    private double money;
    private Date time;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
