package com.haili.finance.business.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/*
 * Created by Monkey on 2017/3/10.
 */

public class MyBankCardModel implements Serializable {

    @Expose
    @SerializedName("bankIcon")
    public String bankIcon;

    @Expose
    @SerializedName("bankName")
    public String bankName;

    @Expose
    @SerializedName("bankCBDNO")
    public String bankCBDNO;

    @Expose
    @SerializedName("bankCardNo")
    public String bankCardNo;

    @Expose
    @SerializedName("bankCardType")
    public String bankCardType;

    @Expose
    @SerializedName("remark")
    public String remark;

    @Expose
    @SerializedName("singleLimit")
    public double singleLimit;

    @Expose
    @SerializedName("dayLimit")
    public double dayLimit;

    @Expose
    @SerializedName("monthLimit")
    public double monthLimit;

    @Expose
    @SerializedName("bankCardId")
    public String bankCardId;
    @Expose
    @SerializedName("isCanUnbundlingBankCard")
    public String isCanUnbundlingBankCard;
}
