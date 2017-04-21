package com.haili.finance.business.property;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/*
 * Created by fuliang on 2017/3/15.
 */

public class ListBanalceModel implements Serializable{

    @Expose
    @SerializedName("time")
    public String time;
    @Expose
    @SerializedName("balance")
    public double balance;
}
