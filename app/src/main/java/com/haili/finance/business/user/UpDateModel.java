package com.haili.finance.business.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Monkey on 2017/3/9.
 */

public class UpDateModel implements Serializable {
    @Expose
    @SerializedName("updateUrl")//更新地址
    public String updateUrl;

    @Expose
    @SerializedName("versionCode")//版本号
    public String versionCode;

    @Expose
    @SerializedName("versionName")//版本名称
    public String versionName;

    @Expose
    @SerializedName("imgUrl")//图片url
    public String imgUrl;

    @Expose
    @SerializedName("forceUpdate")//强制更新
    public String forceUpdate;
}
