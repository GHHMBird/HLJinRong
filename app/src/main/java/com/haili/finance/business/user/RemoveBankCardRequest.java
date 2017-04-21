package com.haili.finance.business.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.haili.finance.http.BusinessEnum;
import com.haili.finance.http.InterfaceService;
import com.haili.finance.http.RequestData;

/**
 *
 * Created by Monkey on 2017/3/7.
 */

public class RemoveBankCardRequest extends RequestData {

    @Expose
    @SerializedName("backCBDNO")
    public String backCBDNO;

    @Expose
    @SerializedName("ruleName")
    public String ruleName;

    @Expose
    @SerializedName("bankCardId")
    public String IDCard;

    @Expose
    @SerializedName("phone")
    public String phone;

    @Expose
    @SerializedName("verifyCode")
    public String verifyCode;

    @Override
    public BusinessEnum getBusinessType() {
        return BusinessEnum.BUSINESS_USER;
    }

    @Override
    public String getInterfaceName() {
        return InterfaceService.API_REMOVE_BANK_CARD;
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
