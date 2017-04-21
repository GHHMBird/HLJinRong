package com.haili.finance.business.property;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.haili.finance.http.ResponseData;

import java.io.Serializable;

/*
 * Created by fuliang on 2017/3/16.
 */

public class RegularResponse extends ResponseData implements Serializable{

    @Expose
    @SerializedName("data")
    public RegularModel data;


}
