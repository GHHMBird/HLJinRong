package com.haili.finance.business.property;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/*
 * Created by Monkey on 2017/3/18.
 */

public class ManageMoney implements Serializable {
    @Expose
    @SerializedName("manageMoneyId")
    public String manageMoneyId;

    @Expose
    @SerializedName("productId")
    public String productId;

    @Expose
    @SerializedName("productName")
    public String productName;

    @Expose
    @SerializedName("repaymentTime")
    public String repaymentTime;

    @Expose
    @SerializedName("investBanalce")
    public double investBalance;

    @Expose
    @SerializedName("yieldRateBalance")
    public double yieldRateBalance;

    @Expose
    @SerializedName("productState")
    public String productState;
}
