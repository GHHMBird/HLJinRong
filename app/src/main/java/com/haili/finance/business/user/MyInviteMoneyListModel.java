package com.haili.finance.business.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/*
 * Created by Monkey on 2017/3/10.
 */

public class MyInviteMoneyListModel implements Serializable {
    @Expose
    @SerializedName("voList")
    public ArrayList<MyInviteMoneyModel> dataModel;

    @Expose
    @SerializedName("recordCount")
    public int recordCount;

    @Expose
    @SerializedName("pageSize")
    public int pageSize;

    @Expose
    @SerializedName("ascORdesc")
    public int ascORdesc;

    @Expose
    @SerializedName("pageCount")
    public int pageCount;

    @Expose
    @SerializedName("orderby")
    public String orderby;

    @Expose
    @SerializedName("pagecode")
    public int pagecode;

    @Expose
    @SerializedName("currentPage")
    public int currentPage;

    @Expose
    @SerializedName("pageIndex")
    public PageIndexModel pageIndexModel;
}
