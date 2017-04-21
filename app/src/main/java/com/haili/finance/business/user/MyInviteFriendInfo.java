package com.haili.finance.business.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/*
 * Created by Monkey on 2017/3/10.
 */

public class MyInviteFriendInfo implements Serializable{
    @Expose
    @SerializedName("phone")
    public String phone;

    @Expose
    @SerializedName("investAmount")
    public double investAmount;

    @Expose
    @SerializedName("registerTime")
    public String registerTime;
}
