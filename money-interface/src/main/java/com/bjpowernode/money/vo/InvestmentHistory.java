package com.bjpowernode.money.vo;

import java.io.Serializable;
import java.util.Date;

public class InvestmentHistory implements Serializable {
    private Date time;
    private String productName;
    private String status;
    private String money;

    public InvestmentHistory() {
    }

    public InvestmentHistory(Date time, String productName, String status, String money) {
        this.time = time;
        this.productName = productName;
        this.status = status;
        this.money = money;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}
