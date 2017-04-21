package com.haili.finance.business.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/*
 * Created by Monkey on 2017/3/10.
 */

public class MessageDetailModel implements Serializable {
    @Expose
    @SerializedName("title")
    public String title;

    @Expose
    @SerializedName("content")
    public String content;

    @Expose
    @SerializedName("time")
    public String time;

    @Expose
    @SerializedName("readState")
    public String readState;
}
