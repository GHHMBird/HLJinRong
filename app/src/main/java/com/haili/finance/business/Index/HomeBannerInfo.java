package com.haili.finance.business.Index;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/*
 * Created by Monkey on 2017/3/6.
 */

public class HomeBannerInfo implements Serializable {
    @Expose
    @SerializedName("pic")
    public String pic;
    @Expose
    @SerializedName("url")
    public String url;
    @Expose
    @SerializedName("refTarget")
    public String refTarget;
    @Expose
    @SerializedName("valid")
    public String valid;
    @Expose
    @SerializedName("refType")
    public String refType;
    @Expose
    @SerializedName("name")
    public String name;
    @Expose
    @SerializedName("orderNum")
    public int orderNum;
    @Expose
    @SerializedName("expTime")
    public int expTime;
    @Expose
    @SerializedName("productId")
    public int productId;
    @Expose
    @SerializedName("code")
    public String code;
    @Expose
    @SerializedName("title")
    public String title;
    @Expose
    @SerializedName("postion")
    public String postion;
    @Expose
    @SerializedName("id")
    public String id;
    @Expose
    @SerializedName("content")
    public String content;
}
