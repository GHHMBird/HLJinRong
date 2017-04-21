package com.haili.finance.business.manage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/*
 * Created by lfu on 2017/3/23.
 */

public class ProductInfoModel implements Serializable {

    @Expose
    @SerializedName("productId")
    public String productId;

    @Expose
    @SerializedName("productName")
    public String productName;

    @Expose
    @SerializedName("yieldRate")
    public double yieldRate;

    @Expose
    @SerializedName("yieldRateAdd")
    public double yieldRateAdd;

    @Expose
    @SerializedName("startInvestorBanalce")
    public double startInvestorBanalce;

    @Expose
    @SerializedName("totalAmountOfFinancing")
    public double totalAmountOfFinancing;

    @Expose
    @SerializedName("remainingAmount")
    public double remainingAmount;

    @Expose
    @SerializedName("investorTime")
    public String investorTime;

    @Expose
    @SerializedName("type")
    public String type;

    @Expose
    @SerializedName("carryInterest")
    public String carryInterest;

    @Expose
    @SerializedName("investmentTime")
    public String investmentTime;

    @Expose
    @SerializedName("investmentProgress")
    public double investmentProgress;

    @Expose
    @SerializedName("productState")
    public String productState;

    @Expose
    @SerializedName("isInvetsment")
    public String isInvetsment;

    @Expose
    @SerializedName("isUseRedPacket")
    public String isUseRedPacket;

    @Expose
    @SerializedName("safefyUrl")
    public String safefyUrl;

    @Expose
    @SerializedName("shareurl")
    public String Shareurl;

    @Expose
    @SerializedName("limitTime")
    public String limitTime;

    @Expose
    @SerializedName("isCanInvest")
    public String isCanInvest;

    @Expose
    @SerializedName("repaymentMode")
    public String repaymentMode;

}
