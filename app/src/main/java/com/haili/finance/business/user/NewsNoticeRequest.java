package com.haili.finance.business.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.haili.finance.http.BusinessEnum;
import com.haili.finance.http.InterfaceService;
import com.haili.finance.http.RequestData;

/*
 *
 * Created by Monkey on 2017/3/10.
 */

public class NewsNoticeRequest extends RequestData {

    @Expose
    @SerializedName("type")
    public String type;

    @Expose
    @SerializedName("pageSize")
    public String pageSize;

    @Expose
    @SerializedName("pageNum")
    public String pageNum;

    @Override
    public BusinessEnum getBusinessType() {
        return BusinessEnum.BUSINESS_USER;
    }

    @Override
    public String getInterfaceName() {
        return InterfaceService.API_NEWS_NOTICE;
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
