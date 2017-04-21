package com.haili.finance.business.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.haili.finance.http.BusinessEnum;
import com.haili.finance.http.InterfaceService;
import com.haili.finance.http.RequestData;

/*
 * Created by Monkey on 2017/3/6.
 */

public class GetRedPackageRequest extends RequestData {

    @Expose
    @SerializedName("pageSize")
    public String pageSize;//一页多少条

    @Expose
    @SerializedName("pageNum")
    public String pageNum;//第几页

    @Expose
    @SerializedName("state")
    public String state;

    @Override
    public BusinessEnum getBusinessType() {
        return BusinessEnum.BUSINESS_USER;
    }

    @Override
    public String getInterfaceName() {
        return InterfaceService.API_RED_PACKAGE;
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
