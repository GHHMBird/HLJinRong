package com.haili.finance.business.manage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.haili.finance.http.BusinessEnum;
import com.haili.finance.http.InterfaceService;
import com.haili.finance.http.RequestData;

/*
 * Created by lfu on 2017/3/22.
 */

public class InvestRequest extends RequestData {

    @Expose
    @SerializedName("investAmount")
    public String investBalance;
    @Expose
    @SerializedName("projectId")
    public String productId;
    @Expose
    @SerializedName("payAmount")
    public String  payAmount;
    @Expose
    @SerializedName("redPackId")
    public String redPackId;
    @Expose
    @SerializedName("paymentPassword")
    public String paymentPassword;
    @Expose
    @SerializedName("investInterest")
    public String investInterest;


    @Override
    public BusinessEnum getBusinessType() {
        return BusinessEnum.BUSINESS_MANAGE;
    }

    @Override
    public String getInterfaceName() {
        return InterfaceService.API_INVEST;
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
