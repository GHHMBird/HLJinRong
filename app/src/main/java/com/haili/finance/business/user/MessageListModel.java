package com.haili.finance.business.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/*
 * Created by Monkey on 2017/3/10.
 */

public class MessageListModel implements Serializable {
    @Expose
    @SerializedName("voList")
    public ArrayList<MessageBean> msgList;
}
