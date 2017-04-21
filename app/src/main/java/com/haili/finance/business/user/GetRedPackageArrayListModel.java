package com.haili.finance.business.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/*
 * Created by Monkey on 2017/3/6.
 */

public class GetRedPackageArrayListModel implements Serializable{

    @Expose
    @SerializedName("banalce")
    public ArrayList<GetRedPackageModel> redPackageArrayList;

}
