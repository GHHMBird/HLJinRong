package com.haili.finance.business.property;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/*
 * Created by fuliang on 2017/3/15.
 */

public class PiggBankDataModel implements Serializable {

    @Expose
    @SerializedName("yesterdayBalance")
    public double yesterdayBanalce;

    @Expose
    @SerializedName("totalBalance")
    public double totalBanalce;

    @Expose
    @SerializedName("totalgetBalance")
    public double totalgetBalance;

    @Expose
    @SerializedName("endDate")
    public String endDate;

    @Expose
    @SerializedName("sevenYearsYield")
    public double sevenYearsYield;

    @Expose
    @SerializedName("millionProfit")
    public double millionProfit;

    @Expose
    @SerializedName("listBalance")
    public ArrayList<ListBanalceModel> listBanalce;

}
