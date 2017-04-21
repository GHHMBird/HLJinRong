package com.haili.finance.business.manage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/*
 * Created by lfu on 2017/3/20.
 */

public class GetRedPacketListModel {

    @Expose
    @SerializedName("id")
    public String id;

    @Expose
    @SerializedName("name")
    public String name;

    @Expose
    @SerializedName("balance")
    public double banalce;

    @Expose
    @SerializedName("investBalance")
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


    public boolean isClicked = false;

}
