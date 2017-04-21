package com.haili.finance.business.Index;

import com.haili.finance.http.BusinessEnum;
import com.haili.finance.http.InterfaceService;
import com.haili.finance.http.RequestData;

/*
 * Created by fuliang on 2017/3/7.
 */

public class SplashImageRequest extends RequestData {



    @Override
    public BusinessEnum getBusinessType() {
        return BusinessEnum.BUSINESS_INDEX;
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
