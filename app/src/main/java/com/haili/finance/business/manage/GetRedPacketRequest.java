package com.haili.finance.business.manage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.haili.finance.http.BusinessEnum;
import com.haili.finance.http.InterfaceService;
import com.haili.finance.http.RequestData;

/*
 * Created by fuliang on 2017/3/15.
 */

public class GetRedPacketRequest extends RequestData{

    @Expose
    @SerializedName("productId")
    public String productId;


    @Override
    public BusinessEnum getBusinessType() {
        return BusinessEnum.BUSINESS_MANAGE;
    }

    @Override
    public String getInterfaceName() {
        return InterfaceService.API_GET_SPLASH_IMAGE;
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
