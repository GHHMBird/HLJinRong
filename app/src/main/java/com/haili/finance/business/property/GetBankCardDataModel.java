package com.haili.finance.business.property;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/*
 * Created by fuliang on 2017/3/17.
 */

public class GetBankCardDataModel implements Serializable{

    @Expose
    @SerializedName("balance")
    public double  balance;

    @Expose
    @SerializedName("monthLimit")
    public double  monthLimit;

    @Expose
    @SerializedName("singleLimit")
    public double  singleLimit;

    @Expose
    @SerializedName("dayLimit")
    public double  dayLimit;

    @Expose
    @SerializedName("bankcardIcon")
    public String  bankcardIcon;

    @Expose
    @SerializedName("bankName")
    public String  bankName;

    @Expose
    @SerializedName("bankCard")
    public String  bankCard;

    @Expose
    @SerializedName("dcFlag")
    public String  dcFlag;

//    @Expose
//    @SerializedName("bankList")
//    public ArrayList<GetBankCardDataListModel>  bankList;

}
