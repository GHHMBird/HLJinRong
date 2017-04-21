package com.haili.finance.business.manage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/*
 * Created by fuliang on 2017/3/14.
 */

public class AuditListModel implements Serializable{

    @Expose
    @SerializedName("desc")
    public String desc;

    @Expose
    @SerializedName("name")
    public String name;

    @Expose
    @SerializedName("state")
    public String state;

    @Expose
    @SerializedName("type")
    public String type;

}
