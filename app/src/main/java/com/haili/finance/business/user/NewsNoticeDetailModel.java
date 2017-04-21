package com.haili.finance.business.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/*
 * Created by Monkey on 2017/3/10.
 */

public class NewsNoticeDetailModel implements Serializable {
    @Expose
    @SerializedName("content")
    public String content;

    @Expose
    @SerializedName("title")
    public String title;

    @Expose
    @SerializedName("publishTime")
    public String publishTime;

    @Expose
    @SerializedName("imageUrl")
    public String imageUrl;

    @Expose
    @SerializedName("pic")
    public String pic;

    @Expose
    @SerializedName("contentType")
    public String contentType;

    @Expose
    @SerializedName("type")
    public String type;

}
