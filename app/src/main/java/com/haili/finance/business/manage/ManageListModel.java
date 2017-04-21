package com.haili.finance.business.manage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/*
 * Created by fuliang on 2017/3/13.
 */

public class ManageListModel implements Serializable{

    //收益
    @Expose
    @SerializedName("yieldRate")
    public double yieldRate;

    //叠加的收益率
    @Expose
    @SerializedName("yieldRateAdd")
    public double yieldRateAdd;

    //起投金额
    @Expose
    @SerializedName("startInvestorBanalce")
    public double startInvestorBanalce;

    //起息方式
    @Expose
    @SerializedName("carryInterest")
    public String carryInterest;

    //期限
    @Expose
    @SerializedName("loanPeriod")
    public String loanPeriod;

    @Expose
    @SerializedName("type")
    public String type;

    //投资进度
    @Expose
    @SerializedName("investmentProgress")
    public double investmentProgress;

    //产品名称
    @Expose
    @SerializedName("productName")
    public String productName;

    //产品ID
    @Expose
    @SerializedName("productId")
    public String productId;

    //是否可用红包
    @Expose
    @SerializedName("isCanUseRedBag")
    public String isCanUseRedBag;

    //投资状态
    @Expose
    @SerializedName("productState")
    public String productState;

    public int isLoading = 0;

    public int fromWhere = 1;

}
