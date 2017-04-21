package com.haili.finance.business.manage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/*
 * Created by lfu on 2017/3/20.
 */

public class GetRedPacketModel {

    @Expose
    @SerializedName("voList")
    public ArrayList<GetRedPacketListModel> voList;

}
