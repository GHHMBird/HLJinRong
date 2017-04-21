package com.haili.finance.business.Index;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/*
 * Created by fuliang on 2017/3/9.
 */

public class SplashImageModel implements Serializable{

    @Expose
    @SerializedName("imgUrl")
    public  String imageUrl;
}
