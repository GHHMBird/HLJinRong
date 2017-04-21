package com.haili.finance.business.property;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/*
 * Created by fuliang on 2017/3/16.
 */

public class SaveDetailListModel implements Serializable {

    @Expose
    @SerializedName("time")
    public String time;

    @Expose
    @SerializedName("remark")
    public String remark;

    @Expose
    @SerializedName("type")
    public String type;

    @Expose
    @SerializedName("banalce")
    public double banalce;

}
