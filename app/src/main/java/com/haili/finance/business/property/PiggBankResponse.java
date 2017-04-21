package com.haili.finance.business.property;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.haili.finance.http.ResponseData;

import java.io.Serializable;

/*
 * Created by fuliang on 2017/3/15.
 */

public class PiggBankResponse extends ResponseData implements Serializable{

    @Expose
    @SerializedName("data")
    public PiggBankDataModel data;

}
