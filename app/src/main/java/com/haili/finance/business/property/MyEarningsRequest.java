package com.haili.finance.business.property;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.haili.finance.http.BusinessEnum;
import com.haili.finance.http.InterfaceService;
import com.haili.finance.http.RequestData;

/*
 * Created by fuliang on 2017/3/16.
 */

public class MyEarningsRequest extends RequestData {

    @Expose
    @SerializedName("timeSlot")
    public String timeSlot;
    @Expose
    @SerializedName("pageNum")
    public String pageNum;

    @Override
    public BusinessEnum getBusinessType() {
        return BusinessEnum.BUSINESS_PROPERTY;
    }

    @Override
    public String getInterfaceName() {
        return InterfaceService.API_MY_EARNINGS;
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
