package com.haili.finance.business.Index;

import com.haili.finance.http.BusinessEnum;
import com.haili.finance.http.InterfaceService;
import com.haili.finance.http.RequestData;

/*
 * Created by Monkey on 2017/3/6.
 */

public class GetHomeNoticeRequest extends RequestData {

    @Override
    public BusinessEnum getBusinessType() {
        return BusinessEnum.BUSINESS_INDEX;
    }

    @Override
    public String getInterfaceName() {
        return InterfaceService.API_HOME_NOTICE;
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
