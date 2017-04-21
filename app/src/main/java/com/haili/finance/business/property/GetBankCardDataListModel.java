package com.haili.finance.business.property;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/*
 * Created by fuliang on 2017/3/17.
 */

public class GetBankCardDataListModel implements Serializable{

    @Expose
    @SerializedName("bankIcon")
    public String  bankIcon;

    @Expose
    @SerializedName("bankName")
    public String  bankName;

    @Expose
    @SerializedName("bankCBDNO")
    public String  bankCBDNO;

    @Expose
    @SerializedName("bankCardNo")
    public String  bankCardNo;

    @Expose
    @SerializedName("bankCardType")
    public String  bankCardType;

    @Expose
    @SerializedName("remark")
    public String  remark;

    @Expose
    @SerializedName("dayLimit")
    public String  dayLimit;

    @Expose
    @SerializedName("singleLimit")
    public String  singleLimit;

    @Expose
    @SerializedName("monthLimit")
    public String  monthLimit;

    @Expose
    @SerializedName("bankCardId")
    public String  bankCardId;

}
