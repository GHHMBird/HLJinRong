package com.haili.finance.business.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Monkey on 2017/3/17.
 */

public class RetrievePasswordModel implements Serializable {
    @Expose
    @SerializedName("updateState")
    public String updateState;
}
