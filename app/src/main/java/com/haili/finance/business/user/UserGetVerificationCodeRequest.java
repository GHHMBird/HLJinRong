package com.haili.finance.business.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.haili.finance.http.BusinessEnum;
import com.haili.finance.http.RequestData;

/*
 * Created by lfu on 2017/2/22.
 */

public class UserGetVerificationCodeRequest extends RequestData{


    @Expose
    @SerializedName("phone")
    public String phone;

    @Expose
    @SerializedName("type")
    public int type;

    @Override
    public BusinessEnum getBusinessType() {
        return null;
    }

    @Override
    public String getInterfaceName() {
        return null;
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
