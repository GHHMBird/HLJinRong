package com.haili.finance.business.user;

import com.haili.finance.http.BusinessEnum;
import com.haili.finance.http.InterfaceService;
import com.haili.finance.http.RequestData;

/**
 *
 * Created by Monkey on 2017/3/8.
 */

public class UpDateRequest extends RequestData {

    @Override
    public BusinessEnum getBusinessType() {
        return BusinessEnum.BUSINESS_USER;
    }

    @Override
    public String getInterfaceName() {
        return InterfaceService.API_UP_DATE;
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
