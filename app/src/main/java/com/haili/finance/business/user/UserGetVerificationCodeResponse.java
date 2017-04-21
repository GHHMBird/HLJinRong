package com.haili.finance.business.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.haili.finance.http.ResponseData;

import java.io.Serializable;

/*
 * Created by lfu on 2017/2/22.
 */

public class UserGetVerificationCodeResponse extends ResponseData implements Serializable {
    private static final long serialVersionUID = 1L;

    @Expose
    @SerializedName("data")
    public  GetImageCodeDataModel dataModel;



}
