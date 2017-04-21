package com.haili.finance.business.property;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/*
 * Created by fuliang on 2017/3/16.
 */

public class MyEarningsListModel implements Serializable {

    @Expose
    @SerializedName("time")
    public String time;

    @Expose
    @SerializedName("banalce")
    public double balance;
}
