package com.haili.finance.business.property;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/*
 * Created by fuliang on 2017/3/15.
 */

public class PropertyHomeModel implements Serializable {

    //用户ID
    @Expose
    @SerializedName("userId")
    public String userId;

    //总金额
    @Expose
    @SerializedName("totalBalance")
    public double totalBanalce;

    //累计收益
    @Expose
    @SerializedName("totalIncome")
    public double totalIncome;

    //存钱罐的金额
    @Expose
    @SerializedName("wxStoreBalance")
    public double wxStoreBalance;

    //存钱罐收益额
    @Expose
    @SerializedName("wxStoreReceiveBalance")
    public double wxStoreReceiveBalance;

    //定期金额
    @Expose
    @SerializedName("fixDateBalance")
    public double fixDateBalance;

    //定期收益额
    @Expose
    @SerializedName("fixDateReceiveBalance")
    public double fixDateReceiveBalance;

    //近一个月的收益额
    @Expose
    @SerializedName("listDayBalance")
    public ArrayList<EarningsModel> listDayBalance;

    //结束日期
    @Expose
    @SerializedName("endDate")
    public String endDate;

    //排名
    @Expose
    @SerializedName("ranking")
    public int ranking;

    //排名上升（下降）的数量
    @Expose
    @SerializedName("rankNum")
    public int rankNum;

    //是否有存钱罐
    @Expose
    @SerializedName("isHaveDeposit")
    public String isHaveDeposit;


}
