package com.haili.finance.business.manage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.haili.finance.http.ResponseData;

import java.io.Serializable;

/*
 * Created by fuliang on 2017/3/9.
 */

public class ManageListResponse extends ResponseData implements Serializable{

    @Expose
    @SerializedName("data")
    public ManageModel data;





}
