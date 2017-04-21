package com.haili.finance.business.manage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/*
 * Created by fuliang on 2017/3/14.
 */

public class ManageDetailDataModel implements Serializable{

    @Expose
    @SerializedName("productInfoVo")
    public ProductInfoModel productInfoVo;

    @Expose
    @SerializedName("deadlineList")
    public List<DeadlineListModel> deadlineList;

    @Expose
    @SerializedName("productDescribe")
    public ProductDescribeModel productDescribe;

    @Expose
    @SerializedName("investList")
    public List<InvestListModel> investList;
}
