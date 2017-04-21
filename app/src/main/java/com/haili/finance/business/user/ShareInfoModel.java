package com.haili.finance.business.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Monkey on 2017/3/7.
 */

public class ShareInfoModel {
    @Expose
    @SerializedName("imgUrl")//图片url
    public String imgUrl;

    @Expose
    @SerializedName("title")//标题
    public String title;

    @Expose
    @SerializedName("url")//网址
    public String url;

    @Expose
    @SerializedName("describe")//描述
    public String describe;
}
