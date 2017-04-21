package com.haili.finance.business.Index;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.haili.finance.http.BusinessEnum;
import com.haili.finance.http.InterfaceService;
import com.haili.finance.http.RequestData;

/*
 * Created by Monkey on 2017/3/8.
 */

public class GetHomeProductRequest extends RequestData {

    @Expose
    @SerializedName("terminal")
    public String terminal;


    @Override
    public BusinessEnum getBusinessType() {
        return BusinessEnum.BUSINESS_INDEX;
    }

    @Override
    public String getInterfaceName() {
        return InterfaceService.API_HOME_PRODUCT;
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
