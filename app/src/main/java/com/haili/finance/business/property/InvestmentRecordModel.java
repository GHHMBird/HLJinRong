package com.haili.finance.business.property;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/*
 * Created by fuliang on 2017/3/16.
 */

public class InvestmentRecordModel implements Serializable {

    @Expose
    @SerializedName("investmentRecord")
    public ArrayList<InvestmentRecordListModel> investmentRecord;

}
