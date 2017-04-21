package com.haili.finance.modle;

/*
 * Created by Monkey on 2017/1/19.
 * 投资记录MODEL
 */

public class InvestmentHistoryModel {
    private String name;            //名字
    private String state;           //状态：募集中，计息中。。。
    private String inv_money;       //投资资金
    private String profit_return;   //预计收益
    private String date;            //日期
    private String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getInv_money() {
        return inv_money;
    }

    public void setInv_money(String inv_money) {
        this.inv_money = inv_money;
    }

    public String getProfit_return() {
        return profit_return;
    }

    public void setProfit_return(String profit_return) {
        this.profit_return = profit_return;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
