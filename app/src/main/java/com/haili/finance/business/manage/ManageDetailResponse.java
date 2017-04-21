package com.haili.finance.business.manage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.haili.finance.http.ResponseData;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/*
 * Created by fuliang on 2017/3/13.
 */

public class ManageDetailResponse extends ResponseData implements Serializable{

    @Expose
    @SerializedName("data")
    public ManageDetailDataModel data;

}
