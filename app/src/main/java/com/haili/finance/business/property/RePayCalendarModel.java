package com.haili.finance.business.property;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/*
 *
 * Created by Monkey on 2017/3/18.
 */

public class RePayCalendarModel implements Serializable {
    @Expose
    @SerializedName("productNumber")
    public int productNumber;

    @Expose
    @SerializedName("totalPrincipalInterest")
    public double totalPrincipalInterest;

    @Expose
    @SerializedName("manageMoneyList")
    public ArrayList<ManageMoney> list;
}
