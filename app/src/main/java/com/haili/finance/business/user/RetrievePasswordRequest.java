package com.haili.finance.business.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.haili.finance.http.BusinessEnum;
import com.haili.finance.http.InterfaceService;
import com.haili.finance.http.RequestData;

/**
 * Created by Monkey on 2017/3/17.
 */

public class RetrievePasswordRequest extends RequestData {

    @Expose
    @SerializedName("phone")
    public String phone;

    @Expose
    @SerializedName("phoneCode")
    public String phoneCode;

    @Expose
    @SerializedName("newPwd")
    public String newPwd;

    @Override
    public BusinessEnum getBusinessType() {
        return BusinessEnum.BUSINESS_USER;
    }

    @Override
    public String getInterfaceName() {
        return InterfaceService.API_RETRIEVE_PWD;
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
