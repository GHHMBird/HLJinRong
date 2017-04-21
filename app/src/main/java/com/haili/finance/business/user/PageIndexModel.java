package com.haili.finance.business.user;
/*
 * Created by hhm on 2017/3/21.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PageIndexModel implements Serializable {
    @Expose
    @SerializedName("startIndex")
    public int startIndex;

    @Expose
    @SerializedName("endIndex")
    public int endIndex;
}
