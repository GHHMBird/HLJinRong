package com.haili.finance.business.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.haili.finance.http.ResponseData;

import java.io.Serializable;

/*
 * Created by Monkey on 2017/3/8.
 */

public class UpDateResponse extends ResponseData  implements Serializable {
    private static final long serialVersionUID = 1L;
    @Expose
    @SerializedName("data")
    public UpDateModel dataModel;
}
