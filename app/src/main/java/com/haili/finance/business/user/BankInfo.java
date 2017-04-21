package com.haili.finance.business.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/*
 * Created by Monkey on 2017/3/7.
 */

public class BankInfo implements Serializable {
    @Expose
    @SerializedName("bankIcon")
    public String bankIcon;

    @Expose
    @SerializedName("bankName")
    public String backName;

    @Expose
    @SerializedName("bankCBDNO")
    public String backCBDNO;

    @Expose
    @SerializedName("singleLimit")
    public String singleLimit;

    @Expose
    @SerializedName("dayLimit")
    public String dayLimit;

    @Expose
    @SerializedName("monthLimit")
    public String monthLimit;
}
