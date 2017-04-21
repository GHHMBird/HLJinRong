package com.haili.finance.modle;

/*
 * Created by Monkey on 2017/1/18.
 * 明细MODEL
 */

public class RetailedModel {

    private String date;            // 时间
    private String money;           //金额
    private String type;            //类型
    private String content;         //备注

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
