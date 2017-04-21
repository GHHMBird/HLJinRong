package com.haili.finance.business.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.haili.finance.http.BusinessEnum;
import com.haili.finance.http.InterfaceService;
import com.haili.finance.http.RequestData;

/*
 * Created by lfu on 2017/2/22.
 */

public class RegisterMsgCodeRequest extends RequestData{

    @Expose
    @SerializedName("phone")
    public String phone;

    @Expose
    @SerializedName("imgCode")
    public String imgCode;

    /*
     * 1、登录
     * 2、注册
     * 3、找回密码
     * 4、修改手机号
     * 5、银行卡解绑
     * 6、绑定银行卡
     * 7、修改手机密码
     */

    @Expose
    @SerializedName("type")
    public int type;


    @Override
    public BusinessEnum getBusinessType() {
        return BusinessEnum.BUSINESS_USER;
    }

    @Override
    public String getInterfaceName() {
        return InterfaceService.API_REGISTER_SEND_CODE;
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
