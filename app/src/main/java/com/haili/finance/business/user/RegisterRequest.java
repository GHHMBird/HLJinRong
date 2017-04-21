package com.haili.finance.business.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.haili.finance.http.BusinessEnum;
import com.haili.finance.http.InterfaceService;
import com.haili.finance.http.RequestData;

/*
 * Created by fuliang on 2017/3/6.
 */

public class RegisterRequest extends RequestData{

    @Expose
    @SerializedName("phone")
    public String phone;

    @Expose
    @SerializedName("pwd")
    public String pwd;

    @Expose
    @SerializedName("verifyCode")
    public String verifyCode;

    @Expose
    @SerializedName("invateCode")
    public String invateCode;

    @Expose
    @SerializedName("imgCode")
    public String imgCode;


    @Override
    public BusinessEnum getBusinessType() {
        return BusinessEnum.BUSINESS_USER;
    }

    @Override
    public String getInterfaceName() {
        return InterfaceService.API_REGISTER;
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
