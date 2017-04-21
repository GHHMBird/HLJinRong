package com.haili.finance.business.manage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.haili.finance.http.BusinessEnum;
import com.haili.finance.http.InterfaceService;
import com.haili.finance.http.RequestData;

/*
 * Created by fuliang on 2017/3/13.
 */

public class ManageDetailRequest extends RequestData{

    @Expose
    @SerializedName("type")
    public  String  type;

    @Expose
    @SerializedName("productId")
    public  String  productId;

    @Expose
    @SerializedName("terminal")
    public  String  terminal = "app";

    @Override
    public BusinessEnum getBusinessType() {
        return BusinessEnum.BUSINESS_MANAGE;
    }

    @Override
    public String getInterfaceName() {
        return InterfaceService.API_MANAGE_DETAIL;
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
