package com.haili.finance.business.property;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/*
 * Created by fuliang on 2017/3/16.
 */

public class RegularModel implements Serializable{

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
    @SerializedName("manageMoneyList")
    public ArrayList<ManageMoneyListModel> manageMoneyList ;


}
