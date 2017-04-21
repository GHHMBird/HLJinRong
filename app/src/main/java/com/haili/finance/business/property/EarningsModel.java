package com.haili.finance.business.property;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/*
 * Created by fuliang on 2017/3/15.
 */

public class EarningsModel implements Serializable {

    //时间
    @Expose
    @SerializedName("time")
    public String time;

    //金额
    @Expose
    @SerializedName("balance")
    public double balance;

}
