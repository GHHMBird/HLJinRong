package com.haili.finance.business.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/*
 * Created by Monkey on 2017/3/10.
 */

public class MyInviteMoneyModel implements Serializable {
    @Expose
    @SerializedName("serNo")
    public int serNo;

    @Expose
    @SerializedName("time")
    public String time;

    @Expose
    @SerializedName("type")
    public String type;

    @Expose
    @SerializedName("amount")
    public double amount;
}
