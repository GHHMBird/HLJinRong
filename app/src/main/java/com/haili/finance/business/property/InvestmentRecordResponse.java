package com.haili.finance.business.property;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.haili.finance.http.ResponseData;

import java.io.Serializable;
import java.util.ArrayList;

/*
 * Created by fuliang on 2017/3/16.
 */

public class InvestmentRecordResponse extends ResponseData implements Serializable{

    @Expose
    @SerializedName("data")
    public ArrayList<InvestmentRecordListModel> investmentRecord;

}
