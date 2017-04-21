package com.haili.finance.business.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/*
 * Created by Monkey on 2017/3/7.
 */

public class MyInfoModel implements Serializable {

    @Expose
    @SerializedName("phone")
    public String phone;

    @Expose
    @SerializedName("balance")
    public double balance;
    @Expose
    @SerializedName("certificationState")
    public String certificationState;

    @Expose
    @SerializedName("bindBankCard")
    public String bindBankCard;

    @Expose
    @SerializedName("realNameStatus")
    public String realNameStatus;

    @Expose
    @SerializedName("accessToken")
    public String accessToken;

    @Expose
    @SerializedName("ruleName")
    public String ruleName;

    @Expose
    @SerializedName("entrustState")
    public String entrustState;
    @Expose
    @SerializedName("redBagMoney")
    public double redBagMoney;

    @Expose
    @SerializedName("idcard")
    public String IDCard;

    @Expose
    @SerializedName("redPacketNum")
    public int redPacketNum;

    @Expose
    @SerializedName("userId")
    public String userId;
    @Expose
    @SerializedName("bankNum")
    public int bankNum;

    @Expose
    @SerializedName("isCanReplacePhone")
    public String isCanReplacePhone;

    @Expose
    @SerializedName("isCanUnbundlingBankCard")
    public String isCanUnbundlingBankCard;

//    @Expose
//    @SerializedName("IntegerunReadmsg")
//    public String IntegerunReadmsg;
}
