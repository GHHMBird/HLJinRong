package com.haili.finance.business.property;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.haili.finance.http.BusinessEnum;
import com.haili.finance.http.InterfaceService;
import com.haili.finance.http.RequestData;

/*
 * Created by Monkey on 2017/3/18.
 */

public class RePayCalendarRequest extends RequestData {
    @Expose
    @SerializedName("pageSize")
    public String pageSize;//一页多少条

    @Expose
    @SerializedName("pageNum")
    public String pageNum;//第几页

    @Expose
    @SerializedName("date")
    public String date;

    @Override
    public BusinessEnum getBusinessType() {
        return BusinessEnum.BUSINESS_PROPERTY;
    }

    @Override
    public String getInterfaceName() {
        return InterfaceService.API_REPAY_CALENDAR;
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
