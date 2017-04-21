package com.haili.finance.business.manage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/*
 * Created by fuliang on 2017/3/14.
 */

public class DeadlineListModel implements Serializable {

    @Expose
    @SerializedName("productState")
    public String productState;

    @Expose
    @SerializedName("time")
    public String time;

    @Expose
    @SerializedName("stateDescribe")
    public String stateDescribe;

}
