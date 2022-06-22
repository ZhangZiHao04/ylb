package com.bjpowernode.money.vo;

import java.io.Serializable;

public class TopRank implements Serializable {
    private String phone;
    private double money;

    public TopRank() {
    }

    public TopRank(String phone, double money) {
        this.phone = phone;
        this.money = money;
    }

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

    @Override
    public String toString() {
        return "TopRank{" +
                "phone='" + phone + '\'' +
                ", money=" + money +
                '}';
    }
}
