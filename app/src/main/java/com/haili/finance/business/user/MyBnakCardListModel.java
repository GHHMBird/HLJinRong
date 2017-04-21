package com.haili.finance.business.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/*
 * Created by Monkey on 2017/3/15.
 */

public class MyBnakCardListModel implements Serializable {
    @Expose
    @SerializedName("bankList")
    public ArrayList<MyBankCardModel> bankList;
}
