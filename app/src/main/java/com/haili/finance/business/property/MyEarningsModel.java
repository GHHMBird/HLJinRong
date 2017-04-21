package com.haili.finance.business.property;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/*
 * Created by fuliang on 2017/3/16.
 */

public class MyEarningsModel implements Serializable {

    @Expose
    @SerializedName("totalBalance")
    public double totalBanalce;

    @Expose
    @SerializedName("listDayBalance")
    public ArrayList<MyEarningsListModel> listBalance;


}
