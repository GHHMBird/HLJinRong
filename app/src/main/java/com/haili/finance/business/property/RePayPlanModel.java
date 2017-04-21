package com.haili.finance.business.property;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/*
 * Created by Monkey on 2017/3/20.
 */

public class RePayPlanModel implements Serializable {
    @Expose
    @SerializedName("repaymentPlan")
    public ArrayList<RePayPlanItem> repaymentPlan;

}
