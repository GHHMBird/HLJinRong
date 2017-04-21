package com.haili.finance.business.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/*
 * Created by Monkey on 2017/3/6.
 */

public class GetRedPackageModel implements Serializable{
    @Expose
    @SerializedName("banalce")
    public double banalce;

    @Expose
    @SerializedName("investBanalce")
    public double investBanalce;

    @Expose
    @SerializedName("startTime")
    public String startTime;

    @Expose
    @SerializedName("endTime")
    public String endTime;

    @Expose
    @SerializedName("productType")
    public String productType;
}
