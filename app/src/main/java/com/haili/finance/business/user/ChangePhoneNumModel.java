package com.haili.finance.business.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/*
 * Created by Monkey on 2017/3/9.
 */

public class ChangePhoneNumModel implements Serializable {
    @Expose
    @SerializedName("state")
    public String state;
}
