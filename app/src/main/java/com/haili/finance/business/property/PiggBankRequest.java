package com.haili.finance.business.property;

import com.haili.finance.http.BusinessEnum;
import com.haili.finance.http.InterfaceService;
import com.haili.finance.http.RequestData;

/*
 * Created by fuliang on 2017/3/15.
 */

public class PiggBankRequest extends RequestData {



    @Override
    public BusinessEnum getBusinessType() {
        return BusinessEnum.BUSINESS_PROPERTY;
    }

    @Override
    public String getInterfaceName() {
        return InterfaceService.API_INCOMEDETAILS;
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
