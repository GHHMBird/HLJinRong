package com.haili.finance.business.manage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.haili.finance.http.BusinessEnum;
import com.haili.finance.http.RequestData;

/*
 * Created by fuliang on 2017/3/9.
 */

public class ManageListRequest extends RequestData{

    @Expose
    @SerializedName("type")
    public String type;

    @Expose
    @SerializedName("pageNum")
    public int pageNum;

    @Expose
    @SerializedName("pageSize")
    public int pageSize;

//    @Expose
//    @SerializedName("redPacket")
//    public String redPacket;

    @Expose
    @SerializedName("terminal")
    public String terminal;


    @Override
    public BusinessEnum getBusinessType() {
        return null;
    }

    @Override
    public String getInterfaceName() {
        return null;
    }

    @Override
    public boolean isNeedCache() {
        return false;
    }

    @Override
    public long getCachePeriod() {
        return 0;
    }

    @Override
    public String getRequestKey() {
        return null;
    }
}
