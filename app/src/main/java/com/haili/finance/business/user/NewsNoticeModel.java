package com.haili.finance.business.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/*
 * Created by Monkey on 2017/3/10.
 */

public class NewsNoticeModel implements Serializable {
    @Expose
    @SerializedName("id")
    public String id;

    @Expose
    @SerializedName("url")
    public String url;

    @Expose
    @SerializedName("title")
    public String title;

    @Expose
    @SerializedName("content")
    public String content;

    @Expose
    @SerializedName("readSate")
    public String readSate;

    @Expose
    @SerializedName("time")
    public String time;

    @Expose
    @SerializedName("contentType")
    public String contentType;

    @Expose
    @SerializedName("imageUrl")
    public String imageUrl;
}
