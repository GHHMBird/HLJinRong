package com.haili.finance.business.property;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/*
 * Created by fuliang on 2017/3/16.
 */

public class InvestmentRecordListModel implements Serializable {

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
    @SerializedName("time")
    public String time;

    @Expose
    @SerializedName("balance")
    public double banalce;

}
