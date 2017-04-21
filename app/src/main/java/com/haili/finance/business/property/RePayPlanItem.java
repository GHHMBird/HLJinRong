package com.haili.finance.business.property;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/*
 * Created by hhm on 2017/3/20.
 */

public class RePayPlanItem implements Serializable {
    @Expose
    @SerializedName("repaymentNo")
    public String repaymentNo;

    @Expose
    @SerializedName("totalRepaymentBanalce")
    public double    totalRepaymentBalance;

    @Expose
    @SerializedName("repaymentState")
    public int repaymentState;

    @Expose
    @SerializedName("repaymentDate")
    public String repaymentDate;
}
