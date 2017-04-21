package com.haili.finance.business.Index;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/*
 * Created by Monkey on 2017/3/6.
 */

public class GetHomeNoticeListModel {
    @Expose
    @SerializedName("list")
    public ArrayList<HomeNoticeInfo> noticeList;
}
