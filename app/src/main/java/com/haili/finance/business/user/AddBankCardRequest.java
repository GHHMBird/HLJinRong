package com.haili.finance.business.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.haili.finance.http.BusinessEnum;
import com.haili.finance.http.InterfaceService;
import com.haili.finance.http.RequestData;

/*
 * Created by Monkey on 2017/3/7.
 */

public class AddBankCardRequest extends RequestData {

    @Expose
    @SerializedName("bankCardNo")
    public String bank_account_no;

    @Expose
    @SerializedName("CBDNo")
    public String CBDNo;

    @Expose
    @SerializedName("phone")
    public String phone_no;

    @Expose
    @SerializedName("MsgCode")
    public String MsgCode;


    @Override
    public BusinessEnum getBusinessType() {
        return BusinessEnum.BUSINESS_USER;
    }

    @Override
    public String getInterfaceName() {
        return InterfaceService.API_ADD_BANK_CARD;
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
