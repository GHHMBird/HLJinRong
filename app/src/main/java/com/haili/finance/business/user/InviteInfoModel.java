package com.haili.finance.business.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/*
 * Created by Monkey on 2017/3/6.
 */

public class InviteInfoModel implements Serializable {
    @Expose
    @SerializedName("imgUrl")
    public String imgUrl;

    @Expose
    @SerializedName("bonusAmount")
    public String bonusAmount;

    @Expose
    @SerializedName("alreadyInvitedNum")
    public String alreadyInvitedNum;

    @Expose
    @SerializedName("effectiveNum")
    public String effectiveNum;

    @Expose
    @SerializedName("myInvitationCode")
    public String myInvitationCode;
}
