package com.haili.finance.business.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.haili.finance.http.ResponseData;

import java.io.Serializable;
import java.util.ArrayList;

/*
 * Created by Monkey on 2017/3/10.
 */

public class NewsNoticeResponse extends ResponseData implements Serializable{
    private static final long serialVersionUID = 1L;

    @Expose
    @SerializedName("data")
    public ArrayList<NewsNoticeModel> dataModel;
}
