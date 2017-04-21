package com.haili.finance.business.manage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

/*
 * Created by fuliang on 2017/3/14.
 */

public class InvestListModel implements Serializable {

    @Expose
    @SerializedName("investName")
    public String investName;

    @Expose
    @SerializedName("time")
    public String time;

    @Expose
    @SerializedName("investBanalce")
    public double investBanalce;
}
