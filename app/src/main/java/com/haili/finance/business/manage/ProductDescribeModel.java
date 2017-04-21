package com.haili.finance.business.manage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/*
 * Created by fuliang on 2017/3/14.
 */

public class ProductDescribeModel implements Serializable {

    @Expose
    @SerializedName("productDescribe")
    public List<ProductDescribeDetailModel> productDescribe;

    @Expose
    @SerializedName("imgList")
    public List<ImgListModel> imgList;

    @Expose
    @SerializedName("auditList")
    public List<AuditListModel> auditList;


}
