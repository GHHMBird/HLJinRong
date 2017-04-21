package com.haili.finance.helper;

import com.haili.finance.business.property.DoRechargeRequest;
import com.haili.finance.business.property.DoRechargeResponse;
import com.haili.finance.business.property.GetBankCardDataRequest;
import com.haili.finance.business.property.GetBankCardDataResponse;
import com.haili.finance.business.property.InvestmentDetailRequest;
import com.haili.finance.business.property.InvestmentDetailResponse;
import com.haili.finance.business.property.InvestmentRecordRequest;
import com.haili.finance.business.property.InvestmentRecordResponse;
import com.haili.finance.business.property.MyEarningsRequest;
import com.haili.finance.business.property.MyEarningsResponse;
import com.haili.finance.business.property.PiggBankRequest;
import com.haili.finance.business.property.PiggBankResponse;
import com.haili.finance.business.property.PropertyHomeRequest;
import com.haili.finance.business.property.PropertyHomeResponse;
import com.haili.finance.business.property.RePayCalendarRequest;
import com.haili.finance.business.property.RePayCalendarResponse;
import com.haili.finance.business.property.RePayPlanRequest;
import com.haili.finance.business.property.RePayPlanResponse;
import com.haili.finance.business.property.RegularRequest;
import com.haili.finance.business.property.RegularResponse;
import com.haili.finance.business.property.SaveDetailRequest;
import com.haili.finance.business.property.SaveDetailResponse;
import com.haili.finance.business.property.WithdrawalsRequest;
import com.haili.finance.business.property.WithdrawalsResponse;
import com.haili.finance.http.InterfaceAPI;

import rx.Observable;

/*
 * Created by fuliang on 2017/3/9.
 */

public class PropertyBusinessHelper {

    public static Observable<PropertyHomeResponse> getCanUseRedPacket(PropertyHomeRequest request) {
        return new InterfaceAPI().getPropertyData(request);
    }

    public static Observable<PiggBankResponse> getPiggBankData(PiggBankRequest request) {
        return new InterfaceAPI().getPiggBankData(request);
    }

    public static Observable<RegularResponse> getRegular(RegularRequest request) {
        return new InterfaceAPI().getRegularData(request);
    }

    public static Observable<InvestmentRecordResponse> getTradingRecord(InvestmentRecordRequest request) {
        return new InterfaceAPI().getTradingRecord(request);
    }

    public static Observable<SaveDetailResponse> getSaveDetails(SaveDetailRequest request) {
        return new InterfaceAPI().getSaveDetails(request);
    }

    public static Observable<MyEarningsResponse> getMyEarnings(MyEarningsRequest request) {
        return new InterfaceAPI().getMyEarnings(request);
    }

    public static Observable<GetBankCardDataResponse> getBankCardData(GetBankCardDataRequest request) {
        return new InterfaceAPI().getBankCardData(request);
    }

    public static Observable<DoRechargeResponse> doRecharge(DoRechargeRequest request) {
        return new InterfaceAPI().doRecharge(request);
    }

    public static Observable<InvestmentDetailResponse> investmentDetail(InvestmentDetailRequest request) {
        return new InterfaceAPI().investmentDetail(request);
    }

    public static Observable<RePayCalendarResponse> repayCalendar(RePayCalendarRequest request) {
        return new InterfaceAPI().repayCalendar(request);
    }

    public static Observable<RePayPlanResponse> repayPlan(RePayPlanRequest request) {
        return new InterfaceAPI().repayPlan(request);
    }

    public static Observable<WithdrawalsResponse> doWithdrawals(WithdrawalsRequest request){
        return new InterfaceAPI().doWithdrawals(request);
    }

}
