package com.haili.finance.business.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/*
 * Created by Monkey on 2017/3/14.
 */

public class UserSendMsgBackModel implements Serializable {
    @Expose
    @SerializedName("feedBackState")
    public String feedBackState;
}
