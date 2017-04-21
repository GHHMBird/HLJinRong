package com.haili.finance.business.manage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.haili.finance.http.ResponseData;

/*
 * Created by fuliang on 2017/3/15.
 */

public class GetRedPacketResponse extends ResponseData{

    @Expose
    @SerializedName("data")
    public  GetRedPacketModel data;

}
