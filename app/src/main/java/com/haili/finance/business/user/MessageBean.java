package com.haili.finance.business.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/*
 * Created by Monkey on 2017/3/10.
 */

public class MessageBean implements Serializable {
    @Expose
    @SerializedName("title")
    public String title;

    @Expose
    @SerializedName("id")
    public String id;

    @Expose
    @SerializedName("time")
    public String time;

    @Expose
    @SerializedName("readState")
    public String readState;
}
