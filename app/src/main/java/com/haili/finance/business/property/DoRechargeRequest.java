package com.haili.finance.business.property;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.haili.finance.http.BusinessEnum;
import com.haili.finance.http.RequestData;

/*
 * Created by fuliang on 2017/3/17.
 */

public class DoRechargeRequest extends RequestData {

    @Expose
    @SerializedName("balance")
    public double balance;

    @Expose
    @SerializedName("phoneCode")
    public String phoneCode;

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
