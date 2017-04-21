package com.haili.finance.helper;

import com.haili.finance.business.manage.GetRedPacketRequest;
import com.haili.finance.business.manage.GetRedPacketResponse;
import com.haili.finance.business.manage.InvestRequest;
import com.haili.finance.business.manage.InvestResponse;
import com.haili.finance.business.manage.ManageDetailRequest;
import com.haili.finance.business.manage.ManageDetailResponse;
import com.haili.finance.business.manage.ManageListRequest;
import com.haili.finance.business.manage.ManageListResponse;
import com.haili.finance.http.InterfaceAPI;

import rx.Observable;

/*
 * Created by fuliang on 2017/3/9.
 */

public class ManageBusinessHelper {


    public static Observable<ManageListResponse> getManageList(ManageListRequest request) {
        return new InterfaceAPI().getManageList(request);
    }

    public static Observable<ManageDetailResponse> getManageDetail(ManageDetailRequest request){
        return  new InterfaceAPI().getManageDetail(request);
    }

    public static Observable<GetRedPacketResponse>getCanUseRedPacket(GetRedPacketRequest request){
        return new InterfaceAPI().getCanUseRedPacket(request);
    }

    public static Observable<InvestResponse> doInvest(InvestRequest request){
        return new InterfaceAPI().doInvest(request);
    }
}
