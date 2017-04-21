package com.haili.finance.business.Index;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/*
 * Created by Monkey on 2017/3/8.
 */

public class HomeProductModel implements Serializable {
    @Expose
    @SerializedName("productId")
    public String productId;
  @Expose
    @SerializedName("limitTime")
    public String limitTime;

    @Expose
    @SerializedName("productName")
    public String productName;

    @Expose
    @SerializedName("productState")
    public String productState;

    @Expose
    @SerializedName("yieldRate")
    public double yieldRate;

    @Expose
    @SerializedName("yieldRateAdd")
    public double yieldRateAdd;

    @Expose
    @SerializedName("isUseRedPacket")
    public String isUseRedPacket;

    @Expose
    @SerializedName("startInvestorBanalce")
    public double startInvestorBanalce;
    @Expose
    @SerializedName("totalAmountOfFinancing")
    public double totalAmountOfFinancing;

    @Expose
    @SerializedName("investorTime")
    public String investorTime;

    @Expose
    @SerializedName("type")
    public String type;

    @Expose
    @SerializedName("isInvetsment")
    public String isInvetsment;

    @Expose
    @SerializedName("projectType")
    public String projectType;
    @Expose
    @SerializedName("pemainingAmount")
    public double pemainingAmount;
    @Expose
    @SerializedName("shareurl")
    public String shareurl;

    @Expose
    @SerializedName("carryInterest")
    public String carryInterest;
    @Expose
    @SerializedName("safefyUrl")
    public String safefyUrl;
    @Expose
    @SerializedName("investmentProgress")
    public double investmentProgress;


}
