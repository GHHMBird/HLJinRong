package com.haili.finance.business.property;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/*
 * Created by Monkey on 2017/3/20.
 */

public class InvestmentDetails implements Serializable{
    @Expose
    @SerializedName("investmentDetails")
    public InvestmentDetailModel responseModel;
}
