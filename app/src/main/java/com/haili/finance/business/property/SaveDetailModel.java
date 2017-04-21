package com.haili.finance.business.property;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/*
 * Created by fuliang on 2017/3/16.
 */

public class SaveDetailModel implements Serializable {

    @Expose
    @SerializedName("listBalance")
    public ArrayList<SaveDetailListModel> listModels;

}
