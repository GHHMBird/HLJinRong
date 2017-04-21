package com.haili.finance.business.Index;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/*
 * Created by Monkey on 2017/3/6.
 */

public class HomeIcon implements Serializable {

    @Expose
    @SerializedName("name")
    public String name;

    @Expose
    @SerializedName("type")
    public String type;
    @Expose
    @SerializedName("pic")
    public String imgUrl;

    @Expose
    @SerializedName("refTarget")
    public String refTarget;

}
