package com.haili.finance.business.property;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/*
 * Created by fuliang on 2017/3/16.
 */

public class ManageMoneyListModel implements Serializable{

    @Expose
    @SerializedName("manageMoneyId")
    public String manageMoneyId;

    @Expose
    @SerializedName("productId")
    public String productId;


    @Expose
    @SerializedName("productName")
    public String productName;

    @Expose
    @SerializedName("repaymentTime")
    public String repaymentTime;

    @Expose
    @SerializedName("investBalance")
    public double investBalance;

    @Expose
    @SerializedName("yieldRateBalance")
    public String yieldRateBalance;

    //（募集中、募集结束、还款中、已还款）（募集中、募集结束、还款中、已还款）
    @Expose
    @SerializedName("productState")
    public int productState;

}
