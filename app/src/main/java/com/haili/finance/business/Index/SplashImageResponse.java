package com.haili.finance.business.Index;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.haili.finance.http.ResponseData;

import java.io.Serializable;

/*
 * Created by fuliang on 2017/3/7.
 */

public class SplashImageResponse extends ResponseData implements Serializable{

    @Expose
    @SerializedName("data")
    public  SplashImageModel dataModel;
}
