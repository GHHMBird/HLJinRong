package com.haili.finance.business.property;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.haili.finance.http.ResponseData;

import java.io.Serializable;

/*
 * Created by Monkey on 2017/3/18.
 */

public class RePayCalendarResponse extends ResponseData implements Serializable {
    private static final long serialVersionUID = 1L;

    @Expose
    @SerializedName("data")
    public RePayCalendarModel model;
}
