package com.haili.finance.business.manage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/*
 * Created by fuliang on 2017/3/9.
 */

public class ManageModel implements Serializable{

    @Expose
    @SerializedName("recordCount")
    public String recordCount;

    @Expose
    @SerializedName("pageSize")
    public String pageSize;

    //总页数
    @Expose
    @SerializedName("pageCount")
    public int pageCount;

    @Expose
    @SerializedName("currentPage")
    public String currentPage;

    @Expose
    @SerializedName("pagecode")
    public String pagecode;

    @Expose
    @SerializedName("orderby")
    public String orderby;

    @Expose
    @SerializedName("ascORdesc")
    public String ascORdesc;

    @Expose
    @SerializedName("voList")
    public ArrayList<ManageListModel> manageListDetail;

}
