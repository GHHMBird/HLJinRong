package com.haili.finance.http;

import com.haili.finance.business.Index.GetHomeBannerRequest;
import com.haili.finance.business.Index.GetHomeBannerResponse;
import com.haili.finance.business.Index.GetHomeIconRequest;
import com.haili.finance.business.Index.GetHomeIconResponse;
import com.haili.finance.business.Index.GetHomeNoticeRequest;
import com.haili.finance.business.Index.GetHomeNoticeResponse;
import com.haili.finance.business.Index.GetHomeProductRequest;
import com.haili.finance.business.Index.GetHomeProductResponse;
import com.haili.finance.business.Index.SplashImageRequest;
import com.haili.finance.business.Index.SplashImageResponse;
import com.haili.finance.business.manage.GetRedPacketRequest;
import com.haili.finance.business.manage.GetRedPacketResponse;
import com.haili.finance.business.manage.InvestRequest;
import com.haili.finance.business.manage.InvestResponse;
import com.haili.finance.business.manage.ManageDetailRequest;
import com.haili.finance.business.manage.ManageDetailResponse;
import com.haili.finance.business.manage.ManageListRequest;
import com.haili.finance.business.manage.ManageListResponse;
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
import com.haili.finance.business.user.AddBankCardRequest;
import com.haili.finance.business.user.AddBankCardResponse;
import com.haili.finance.business.user.BankListRequest;
import com.haili.finance.business.user.BankListResponse;
import com.haili.finance.business.user.ChangeLoginPsdRequest;
import com.haili.finance.business.user.ChangeLoginPsdResponse;
import com.haili.finance.business.user.ChangePhoneNumRequest;
import com.haili.finance.business.user.ChangePhoneNumResponse;
import com.haili.finance.business.user.CheckMessageCodeRequest;
import com.haili.finance.business.user.CheckMessageCodeResponse;
import com.haili.finance.business.user.GetInviteCodeRequest;
import com.haili.finance.business.user.GetInviteCodeResponse;
import com.haili.finance.business.user.GetRedPackageRequest;
import com.haili.finance.business.user.GetRedPackageResponse;
import com.haili.finance.business.user.LoginRequest;
import com.haili.finance.business.user.LoginResponse;
import com.haili.finance.business.user.MessageDetailRequest;
import com.haili.finance.business.user.MessageDetailResponse;
import com.haili.finance.business.user.MessageListRequest;
import com.haili.finance.business.user.MessageListResponse;
import com.haili.finance.business.user.MyBankCardRequest;
import com.haili.finance.business.user.MyBankCardResponse;
import com.haili.finance.business.user.MyInfoRequest;
import com.haili.finance.business.user.MyInviteFriendRequest;
import com.haili.finance.business.user.MyInviteFriendResponse;
import com.haili.finance.business.user.MyInviteMoneyRequest;
import com.haili.finance.business.user.MyInviteMoneyResponse;
import com.haili.finance.business.user.NewsNoticeDetailRequest;
import com.haili.finance.business.user.NewsNoticeDetailResponse;
import com.haili.finance.business.user.NewsNoticeRequest;
import com.haili.finance.business.user.NewsNoticeResponse;
import com.haili.finance.business.user.PayPasswordRequest;
import com.haili.finance.business.user.PayPasswordResponse;
import com.haili.finance.business.user.RealNameRequest;
import com.haili.finance.business.user.RealNameResponse;
import com.haili.finance.business.user.RegisterMsgCodeRequest;
import com.haili.finance.business.user.RegisterMsgCodeResponse;
import com.haili.finance.business.user.RegisterRequest;
import com.haili.finance.business.user.RemoveBankCardRequest;
import com.haili.finance.business.user.RemoveBankCardResponse;
import com.haili.finance.business.user.RetrievePasswordRequest;
import com.haili.finance.business.user.RetrievePasswordResponse;
import com.haili.finance.business.user.ShareInfoRequest;
import com.haili.finance.business.user.ShareInfoResponse;
import com.haili.finance.business.user.UpDateRequest;
import com.haili.finance.business.user.UpDateResponse;
import com.haili.finance.business.user.UserGetVerificationCodeRequest;
import com.haili.finance.business.user.UserGetVerificationCodeResponse;
import com.haili.finance.business.user.UserSendMsgBackReoponse;
import com.haili.finance.business.user.UserSendMsgBackRequest;
import com.haili.finance.business.user.VerifyCodeLoginRequest;
import com.haili.finance.modle.DeviceInfoModel;
import com.haili.finance.rx.RequestErrorThrowable;
import com.haili.finance.rx.RxHttpHelper;
import com.haili.finance.stroage.DataCache;
import com.haili.finance.utils.StringUtils;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/*
 * Created by lfu on 2017/2/21.
 */

public class InterfaceAPI {

    private DeviceInfoModel model;

    private InterfaceService interfaceService;

    public InterfaceAPI() {
        interfaceService = new RetrofitClient().getInterfaceService();
    }


    //注册
    public Observable<LoginResponse> register(RegisterRequest registerRequest) {
        return interfaceService.register(RxHttpHelper.convertRequestToJson(registerRequest))
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends LoginResponse>>() {
                    @Override
                    public Observable<? extends LoginResponse> call(Throwable throwable) {
                        return Observable.error(RxHttpHelper.convertIOEError(throwable));
                    }
                })
                .flatMap(new Func1<LoginResponse, Observable<LoginResponse>>() {
                    @Override
                    public Observable<LoginResponse> call(LoginResponse registerResponse) {
                        if (registerResponse == null) {
                            return Observable.error(new RequestErrorThrowable(HttpErrorInfo.CODE_OF_PARSE_REQUEST_FAILURE,
                                    HttpErrorInfo.MSG_OF_PARSE_REQUEST_FAILURE));
                        }

                        if (registerResponse.result) {
                            model = DataCache.instance.getCacheData("haili", "DeviceInfoModel");
                            if (model != null) {
                                model.accessToken = registerResponse.loginModel.accessToken;
                            }
                            DataCache.instance.saveCacheData("haili", "DeviceInfoModel", model);
                            DataCache.instance.saveCacheData("haili", "LoginResponse", registerResponse);
                            return Observable.just(registerResponse);
                        } else {
                            if (StringUtils.isEmpty(registerResponse.errorMsg)) {
                                return Observable.error(new RequestErrorThrowable("-1", "系统错误"));
                            }
                            return Observable.error(new RequestErrorThrowable("-1", registerResponse.errorMsg));
                        }
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    //修改登录密码
    public Observable<ChangeLoginPsdResponse> changeLoginPsd(ChangeLoginPsdRequest request) {
        return interfaceService.changeLoginPsd(RxHttpHelper.convertRequestToJson(request))
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends ChangeLoginPsdResponse>>() {
                    @Override
                    public Observable<? extends ChangeLoginPsdResponse> call(Throwable throwable) {
                        return Observable.error(RxHttpHelper.convertIOEError(throwable));
                    }
                }).flatMap(new Func1<ChangeLoginPsdResponse, Observable<ChangeLoginPsdResponse>>() {
                    @Override
                    public Observable<ChangeLoginPsdResponse> call(ChangeLoginPsdResponse changeLoginPsdResponse) {
                        if (changeLoginPsdResponse == null) {
                            return Observable.error(new RequestErrorThrowable(HttpErrorInfo.CODE_OF_PARSE_REQUEST_FAILURE,
                                    HttpErrorInfo.MSG_OF_PARSE_REQUEST_FAILURE));
                        }
                        if (changeLoginPsdResponse.result) {
                            return Observable.just(changeLoginPsdResponse);
                        } else {
                            if (StringUtils.isEmpty(changeLoginPsdResponse.errorMsg)) {
                                return Observable.error(new RequestErrorThrowable("-1", "系统错误"));
                            }
                            return Observable.error(new RequestErrorThrowable("-1", changeLoginPsdResponse.errorMsg));
                        }
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    //获取红包列表
    public Observable<GetRedPackageResponse> getRedPackage(GetRedPackageRequest request) {
        return interfaceService.getRedPackage(RxHttpHelper.convertRequestToJson(request))
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends GetRedPackageResponse>>() {
                    @Override
                    public Observable<? extends GetRedPackageResponse> call(Throwable throwable) {
                        return Observable.error(RxHttpHelper.convertIOEError(throwable));
                    }
                }).flatMap(new Func1<GetRedPackageResponse, Observable<GetRedPackageResponse>>() {
                    @Override
                    public Observable<GetRedPackageResponse> call(GetRedPackageResponse getRedPackageResponse) {
                        if (getRedPackageResponse == null) {
                            return Observable.error(new RequestErrorThrowable(HttpErrorInfo.CODE_OF_PARSE_REQUEST_FAILURE,
                                    HttpErrorInfo.MSG_OF_PARSE_REQUEST_FAILURE));
                        }
                        if (getRedPackageResponse.result) {
                            DataCache.instance.saveCacheData("haili", "GetRedPackageResponse", getRedPackageResponse);
                            return Observable.just(getRedPackageResponse);
                        } else {
                            if (StringUtils.isEmpty(getRedPackageResponse.errorMsg)) {
                                return Observable.error(new RequestErrorThrowable("-1", "系统错误"));
                            }
                            return Observable.error(new RequestErrorThrowable("-1", getRedPackageResponse.errorMsg));
                        }
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    //获取首页轮播图
    public Observable<GetHomeBannerResponse> getHomeBanner(GetHomeBannerRequest request) {
        return interfaceService.getHomeBanner(RxHttpHelper.convertRequestToJson(request))
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends GetHomeBannerResponse>>() {
                    @Override
                    public Observable<? extends GetHomeBannerResponse> call(Throwable throwable) {
                        return Observable.error(RxHttpHelper.convertIOEError(throwable));
                    }
                }).flatMap(new Func1<GetHomeBannerResponse, Observable<GetHomeBannerResponse>>() {
                    @Override
                    public Observable<GetHomeBannerResponse> call(GetHomeBannerResponse getHomeBannerResponse) {
                        if (getHomeBannerResponse == null) {
                            return Observable.error(new RequestErrorThrowable(HttpErrorInfo.CODE_OF_PARSE_REQUEST_FAILURE,
                                    HttpErrorInfo.MSG_OF_PARSE_REQUEST_FAILURE));
                        }
                        if (getHomeBannerResponse.result) {
                            DataCache.instance.saveCacheData("haili", "GetHomeBannerResponse", getHomeBannerResponse);
                            return Observable.just(getHomeBannerResponse);
                        } else {
                            if (StringUtils.isEmpty(getHomeBannerResponse.errorMsg)) {
                                return Observable.error(new RequestErrorThrowable("-1", "系统错误"));
                            }
                            return Observable.error(new RequestErrorThrowable("-1", getHomeBannerResponse.errorMsg));
                        }
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    //获取首页公告
    public Observable<GetHomeNoticeResponse> getHomeNotice(GetHomeNoticeRequest request) {
        return interfaceService.getHomeNotice(RxHttpHelper.convertRequestToJson(request))
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends GetHomeNoticeResponse>>() {
                    @Override
                    public Observable<? extends GetHomeNoticeResponse> call(Throwable throwable) {
                        return Observable.error(RxHttpHelper.convertIOEError(throwable));
                    }
                })
                .flatMap(new Func1<GetHomeNoticeResponse, Observable<GetHomeNoticeResponse>>() {
                    @Override
                    public Observable<GetHomeNoticeResponse> call(GetHomeNoticeResponse getHomeNoticeResponse) {
                        if (getHomeNoticeResponse == null) {
                            return Observable.error(new RequestErrorThrowable(HttpErrorInfo.CODE_OF_PARSE_REQUEST_FAILURE,
                                    HttpErrorInfo.MSG_OF_PARSE_REQUEST_FAILURE));
                        }
                        if (getHomeNoticeResponse.result) {
                            return Observable.just(getHomeNoticeResponse);
                        } else {
                            if (StringUtils.isEmpty(getHomeNoticeResponse.errorMsg)) {
                                return Observable.error(new RequestErrorThrowable("-1", "系统错误"));
                            }
                        }
                        return Observable.error(new RequestErrorThrowable("-1", getHomeNoticeResponse.errorMsg));
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    //获取邀请信息
    public Observable<GetInviteCodeResponse> getInviteInfo(GetInviteCodeRequest request) {
        return interfaceService.getInviteCode(RxHttpHelper.convertRequestToJson(request))
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends GetInviteCodeResponse>>() {
                    @Override
                    public Observable<? extends GetInviteCodeResponse> call(Throwable throwable) {
                        return Observable.error(RxHttpHelper.convertIOEError(throwable));
                    }
                }).flatMap(new Func1<GetInviteCodeResponse, Observable<GetInviteCodeResponse>>() {
                    @Override
                    public Observable<GetInviteCodeResponse> call(GetInviteCodeResponse getInviteCodeResponse) {
                        if (getInviteCodeResponse == null) {
                            return Observable.error(new RequestErrorThrowable(HttpErrorInfo.CODE_OF_PARSE_REQUEST_FAILURE,
                                    HttpErrorInfo.MSG_OF_PARSE_REQUEST_FAILURE));
                        }
                        if (getInviteCodeResponse.result) {
                            DataCache.instance.saveCacheData("haili", "GetInviteCodeRequest", getInviteCodeResponse);
                            return Observable.just(getInviteCodeResponse);
                        } else {
                            if (StringUtils.isEmpty(getInviteCodeResponse.errorMsg)) {
                                return Observable.error(new RequestErrorThrowable("-1", "系统错误"));
                            }
                        }
                        return Observable.error(new RequestErrorThrowable("-1", getInviteCodeResponse.errorMsg));
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    //获取首页ICON
    public Observable<GetHomeIconResponse> getHomeIcon(GetHomeIconRequest request) {
        return interfaceService.getHomeIcon(RxHttpHelper.convertRequestToJson(request))
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends GetHomeIconResponse>>() {
                    @Override
                    public Observable<? extends GetHomeIconResponse> call(Throwable throwable) {
                        return Observable.error(RxHttpHelper.convertIOEError(throwable));
                    }
                }).flatMap(new Func1<GetHomeIconResponse, Observable<GetHomeIconResponse>>() {
                    @Override
                    public Observable<GetHomeIconResponse> call(GetHomeIconResponse getHomeIconResponse) {
                        if (getHomeIconResponse == null) {
                            return Observable.error(new RequestErrorThrowable(HttpErrorInfo.CODE_OF_PARSE_REQUEST_FAILURE,
                                    HttpErrorInfo.MSG_OF_PARSE_REQUEST_FAILURE));
                        }
                        if (getHomeIconResponse.result) {
                            DataCache.instance.saveCacheData("haili", "GetHomeIconResponse", getHomeIconResponse);
                            return Observable.just(getHomeIconResponse);
                        } else {
                            if (StringUtils.isEmpty(getHomeIconResponse.errorMsg)) {
                                return Observable.error(new RequestErrorThrowable("-1", "系统错误"));
                            }
                        }
                        return Observable.error(new RequestErrorThrowable("-1", getHomeIconResponse.errorMsg));
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    //实名认证
    public Observable<RealNameResponse> realName(RealNameRequest request) {
        return interfaceService.realName(RxHttpHelper.convertRequestToJson(request))
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends RealNameResponse>>() {
                    @Override
                    public Observable<? extends RealNameResponse> call(Throwable throwable) {
                        return Observable.error(RxHttpHelper.convertIOEError(throwable));
                    }
                }).flatMap(new Func1<RealNameResponse, Observable<RealNameResponse>>() {
                    @Override
                    public Observable<RealNameResponse> call(RealNameResponse realNameResponse) {
                        if (realNameResponse == null) {
                            return Observable.error(new RequestErrorThrowable(HttpErrorInfo.CODE_OF_PARSE_REQUEST_FAILURE,
                                    HttpErrorInfo.MSG_OF_PARSE_REQUEST_FAILURE));
                        }
                        if (realNameResponse.result) {
                            return Observable.just(realNameResponse);
                        } else {
                            if (StringUtils.isEmpty(realNameResponse.errorMsg)) {
                                return Observable.error(new RequestErrorThrowable("-1", "系统错误"));
                            }
                        }
                        return Observable.error(new RequestErrorThrowable(realNameResponse.errorCode, realNameResponse.errorMsg));
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    //密码登录
    public Observable<LoginResponse> pasLogin(LoginRequest request) {
        return interfaceService.login(RxHttpHelper.convertRequestToJson(request))
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends LoginResponse>>() {
                    @Override
                    public Observable<? extends LoginResponse> call(Throwable throwable) {
                        return Observable.error(RxHttpHelper.convertIOEError(throwable));
                    }
                }).flatMap(new Func1<LoginResponse, Observable<LoginResponse>>() {
                    @Override
                    public Observable<LoginResponse> call(LoginResponse loginResponse) {
                        if (loginResponse == null) {
                            return Observable.error(new RequestErrorThrowable(HttpErrorInfo.CODE_OF_PARSE_REQUEST_FAILURE,
                                    HttpErrorInfo.MSG_OF_PARSE_REQUEST_FAILURE));
                        }

                        if (loginResponse.result) {
                            model = DataCache.instance.getCacheData("haili", "DeviceInfoModel");
                            if (model != null) {
                                model.accessToken = loginResponse.loginModel.accessToken;
                            }
                            DataCache.instance.saveCacheData("haili", "DeviceInfoModel", model);
                            DataCache.instance.saveCacheData("haili", "LoginResponse", loginResponse);
                            return Observable.just(loginResponse);
                        } else {
                            if (StringUtils.isEmpty(loginResponse.errorMsg)) {
                                return Observable.error(new RequestErrorThrowable("-1", "系统错误"));
                            }
                            return Observable.error(new RequestErrorThrowable(loginResponse.errorCode, loginResponse.errorMsg));
                        }
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    //获取短信验证码
    public Observable<RegisterMsgCodeResponse> getMsgCode(RegisterMsgCodeRequest request) {
        return interfaceService.sendCode(RxHttpHelper.convertRequestToJson(request))
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends RegisterMsgCodeResponse>>() {
                    @Override
                    public Observable<? extends RegisterMsgCodeResponse> call(Throwable throwable) {
                        return Observable.error(RxHttpHelper.convertIOEError(throwable));
                    }
                }).flatMap(new Func1<RegisterMsgCodeResponse, Observable<RegisterMsgCodeResponse>>() {
                    @Override
                    public Observable<RegisterMsgCodeResponse> call(RegisterMsgCodeResponse getPaymentMethodResponse) {
                        if (getPaymentMethodResponse == null) {
                            return Observable.error(new RequestErrorThrowable(HttpErrorInfo.CODE_OF_PARSE_REQUEST_FAILURE,
                                    HttpErrorInfo.MSG_OF_PARSE_REQUEST_FAILURE));
                        }
                        if (getPaymentMethodResponse.result) {
                            return Observable.just(getPaymentMethodResponse);
                        } else {
                            if (StringUtils.isEmpty(getPaymentMethodResponse.errorMsg)) {
                                return Observable.error(new RequestErrorThrowable("-1", "系统错误"));
                            }
                            return Observable.error(new RequestErrorThrowable(getPaymentMethodResponse.errorCode, getPaymentMethodResponse.errorMsg));
                        }
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    //获取图形验证码
    public Observable<UserGetVerificationCodeResponse> getImageCode(UserGetVerificationCodeRequest request) {
        return interfaceService.getCode(RxHttpHelper.convertRequestToJson(request))
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends UserGetVerificationCodeResponse>>() {
                    @Override
                    public Observable<? extends UserGetVerificationCodeResponse> call(Throwable throwable) {
                        return Observable.error(RxHttpHelper.convertIOEError(throwable));
                    }
                }).flatMap(new Func1<UserGetVerificationCodeResponse, Observable<UserGetVerificationCodeResponse>>() {
                    @Override
                    public Observable<UserGetVerificationCodeResponse> call(UserGetVerificationCodeResponse userGetVerificationCodeResponse) {
                        if (userGetVerificationCodeResponse == null) {
                            return Observable.error(new RequestErrorThrowable(HttpErrorInfo.CODE_OF_PARSE_REQUEST_FAILURE,
                                    HttpErrorInfo.MSG_OF_PARSE_REQUEST_FAILURE));
                        }
                        if (userGetVerificationCodeResponse.result) {
                            return Observable.just(userGetVerificationCodeResponse);
                        } else {
                            if (StringUtils.isEmpty(userGetVerificationCodeResponse.errorMsg)) {
                                return Observable.error(new RequestErrorThrowable("-1", "系统错误"));
                            }
                            return Observable.error(new RequestErrorThrowable(userGetVerificationCodeResponse.errorCode, userGetVerificationCodeResponse.errorMsg));
                        }
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    //绑卡/添加银行卡
    public Observable<AddBankCardResponse> addBankCard(AddBankCardRequest request) {
        return interfaceService.addBankCard(RxHttpHelper.convertRequestToJson(request))
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends AddBankCardResponse>>() {
                    @Override
                    public Observable<? extends AddBankCardResponse> call(Throwable throwable) {
                        return Observable.error(RxHttpHelper.convertIOEError(throwable));
                    }
                }).flatMap(new Func1<AddBankCardResponse, Observable<AddBankCardResponse>>() {
                    @Override
                    public Observable<AddBankCardResponse> call(AddBankCardResponse response) {
                        if (response == null) {
                            return Observable.error(new RequestErrorThrowable(HttpErrorInfo.CODE_OF_PARSE_REQUEST_FAILURE,
                                    HttpErrorInfo.MSG_OF_PARSE_REQUEST_FAILURE));
                        }
                        if (response.result) {
                            return Observable.just(response);
                        } else {
                            if (StringUtils.isEmpty(response.errorMsg)) {
                                return Observable.error(new RequestErrorThrowable("-1", "系统错误"));
                            }
                            return Observable.error(new RequestErrorThrowable(response.errorCode, response.errorMsg));
                        }
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    //分享功能
    public Observable<ShareInfoResponse> shareInfo(ShareInfoRequest request) {
        return interfaceService.shareInfo(RxHttpHelper.convertRequestToJson(request))
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends ShareInfoResponse>>() {
                    @Override
                    public Observable<? extends ShareInfoResponse> call(Throwable throwable) {
                        return Observable.error(RxHttpHelper.convertIOEError(throwable));
                    }
                }).flatMap(new Func1<ShareInfoResponse, Observable<ShareInfoResponse>>() {
                    @Override
                    public Observable<ShareInfoResponse> call(ShareInfoResponse shareInfoResponse) {
                        if (shareInfoResponse == null) {
                            return Observable.error(new RequestErrorThrowable(HttpErrorInfo.CODE_OF_PARSE_REQUEST_FAILURE,
                                    HttpErrorInfo.MSG_OF_PARSE_REQUEST_FAILURE));
                        }
                        if (shareInfoResponse.result) {
                            return Observable.just(shareInfoResponse);
                        } else {
                            if (StringUtils.isEmpty(shareInfoResponse.errorMsg)) {
                                return Observable.error(new RequestErrorThrowable("-1", "系统错误"));
                            }
                            return Observable.error(new RequestErrorThrowable(shareInfoResponse.errorCode, shareInfoResponse.errorMsg));
                        }
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    //解绑银行卡
    public Observable<RemoveBankCardResponse> removeBankCard(RemoveBankCardRequest request) {
        return interfaceService.removeBankCard(RxHttpHelper.convertRequestToJson(request))
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends RemoveBankCardResponse>>() {
                    @Override
                    public Observable<? extends RemoveBankCardResponse> call(Throwable throwable) {
                        return Observable.error(RxHttpHelper.convertIOEError(throwable));
                    }
                }).flatMap(new Func1<RemoveBankCardResponse, Observable<RemoveBankCardResponse>>() {
                    @Override
                    public Observable<RemoveBankCardResponse> call(RemoveBankCardResponse removeBankCardResponse) {
                        if (removeBankCardResponse == null) {
                            return Observable.error(new RequestErrorThrowable(HttpErrorInfo.CODE_OF_PARSE_REQUEST_FAILURE,
                                    HttpErrorInfo.MSG_OF_PARSE_REQUEST_FAILURE));
                        }
                        if (removeBankCardResponse.result) {
                            return Observable.just(removeBankCardResponse);
                        } else {
                            if (StringUtils.isEmpty(removeBankCardResponse.errorMsg)) {
                                return Observable.error(new RequestErrorThrowable("-1", "系统错误"));
                            }
                            return Observable.error(new RequestErrorThrowable(removeBankCardResponse.errorCode, removeBankCardResponse.errorMsg));
                        }
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    //用户信息
    public Observable<LoginResponse> myInfo(MyInfoRequest request) {
        return interfaceService.myInfo(RxHttpHelper.convertRequestToJson(request))
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends LoginResponse>>() {
                    @Override
                    public Observable<? extends LoginResponse> call(Throwable throwable) {
                        return Observable.error(RxHttpHelper.convertIOEError(throwable));
                    }
                }).flatMap(new Func1<LoginResponse, Observable<LoginResponse>>() {
                    @Override
                    public Observable<LoginResponse> call(LoginResponse myInfoResponse) {
                        if (myInfoResponse == null) {
                            return Observable.error(new RequestErrorThrowable(HttpErrorInfo.CODE_OF_PARSE_REQUEST_FAILURE,
                                    HttpErrorInfo.MSG_OF_PARSE_REQUEST_FAILURE));
                        }
                        if (myInfoResponse.result) {
                            DataCache.instance.saveCacheData("haili", "MyInfoResponse", myInfoResponse);
                            return Observable.just(myInfoResponse);
                        } else {
                            if (StringUtils.isEmpty(myInfoResponse.errorMsg)) {
                                return Observable.error(new RequestErrorThrowable("-1", "系统错误"));
                            }
                            return Observable.error(new RequestErrorThrowable(myInfoResponse.errorCode, myInfoResponse.errorMsg));
                        }
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    //验证码登录
    public Observable<LoginResponse> verifyCodeLogin(VerifyCodeLoginRequest request) {
        return interfaceService.verifyCodeLogin(RxHttpHelper.convertRequestToJson(request))
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends LoginResponse>>() {
                    @Override
                    public Observable<? extends LoginResponse> call(Throwable throwable) {
                        return Observable.error(RxHttpHelper.convertIOEError(throwable));
                    }
                })
                .flatMap(new Func1<LoginResponse, Observable<LoginResponse>>() {
                    @Override
                    public Observable<LoginResponse> call(LoginResponse loginResponse) {
                        if (loginResponse == null) {
                            return Observable.error(new RequestErrorThrowable(HttpErrorInfo.CODE_OF_PARSE_REQUEST_FAILURE,
                                    HttpErrorInfo.MSG_OF_PARSE_REQUEST_FAILURE));
                        }
                        if (loginResponse.result) {
                            model = DataCache.instance.getCacheData("haili", "DeviceInfoModel");
                            if (model != null) {
                                model.accessToken = loginResponse.loginModel.accessToken;
                            }
                            DataCache.instance.saveCacheData("haili", "DeviceInfoModel", model);
                            DataCache.instance.saveCacheData("haili", "LoginResponse", loginResponse);
                            return Observable.just(loginResponse);
                        } else {
                            if (StringUtils.isEmpty(loginResponse.errorMsg)) {
                                return Observable.error(new RequestErrorThrowable("-1", "系统错误"));
                            }
                            return Observable.error(new RequestErrorThrowable(loginResponse.errorCode, loginResponse.errorMsg));
                        }
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    //获取银行卡列表
    public Observable<BankListResponse> getOpenBankList(BankListRequest request) {
        return interfaceService.bankList(RxHttpHelper.convertRequestToJson(request))
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends BankListResponse>>() {
                    @Override
                    public Observable<? extends BankListResponse> call(Throwable throwable) {
                        return Observable.error(RxHttpHelper.convertIOEError(throwable));
                    }
                }).flatMap(new Func1<BankListResponse, Observable<BankListResponse>>() {
                    @Override
                    public Observable<BankListResponse> call(BankListResponse bankListResponse) {
                        if (bankListResponse == null) {
                            return Observable.error(new RequestErrorThrowable(HttpErrorInfo.CODE_OF_PARSE_REQUEST_FAILURE,
                                    HttpErrorInfo.MSG_OF_PARSE_REQUEST_FAILURE));
                        }
                        if (bankListResponse.result) {
                            DataCache.instance.saveCacheData("haili", "BankListResponse", bankListResponse);
                            return Observable.just(bankListResponse);
                        } else {
                            if (StringUtils.isEmpty(bankListResponse.errorMsg)) {
                                return Observable.error(new RequestErrorThrowable("-1", "系统错误"));
                            }
                            return Observable.error(new RequestErrorThrowable(bankListResponse.errorCode, bankListResponse.errorMsg));
                        }
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    //获取首页推荐产品
    public Observable<GetHomeProductResponse> getHomeProduct(GetHomeProductRequest request) {
        return interfaceService.getHomeProduct(RxHttpHelper.convertRequestToJson(request))
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends GetHomeProductResponse>>() {
                    @Override
                    public Observable<? extends GetHomeProductResponse> call(Throwable throwable) {
                        return Observable.error(RxHttpHelper.convertIOEError(throwable));
                    }
                }).flatMap(new Func1<GetHomeProductResponse, Observable<GetHomeProductResponse>>() {
                    @Override
                    public Observable<GetHomeProductResponse> call(GetHomeProductResponse getHomeProductResponse) {
                        if (getHomeProductResponse == null) {
                            return Observable.error(new RequestErrorThrowable(HttpErrorInfo.CODE_OF_PARSE_REQUEST_FAILURE,
                                    HttpErrorInfo.MSG_OF_PARSE_REQUEST_FAILURE));
                        }
                        if (getHomeProductResponse.result) {
                            DataCache.instance.saveCacheData("haili", "GetHomeProductResponse", getHomeProductResponse);
                            return Observable.just(getHomeProductResponse);
                        } else {
                            if (StringUtils.isEmpty(getHomeProductResponse.errorMsg)) {
                                return Observable.error(new RequestErrorThrowable("-1", "系统错误"));
                            }
                            return Observable.error(new RequestErrorThrowable(getHomeProductResponse.errorCode, getHomeProductResponse.errorMsg));
                        }
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }


    //检查更新
    public Observable<UpDateResponse> upDate(UpDateRequest request) {
        return interfaceService.upDate(RxHttpHelper.convertRequestToJson(request))
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends UpDateResponse>>() {
                    @Override
                    public Observable<? extends UpDateResponse> call(Throwable throwable) {
                        return Observable.error(RxHttpHelper.convertIOEError(throwable));
                    }
                }).flatMap(new Func1<UpDateResponse, Observable<UpDateResponse>>() {
                    @Override
                    public Observable<UpDateResponse> call(UpDateResponse upDateResponse) {
                        if (upDateResponse == null) {
                            return Observable.error(new RequestErrorThrowable(HttpErrorInfo.CODE_OF_PARSE_REQUEST_FAILURE,
                                    HttpErrorInfo.MSG_OF_PARSE_REQUEST_FAILURE));
                        }
                        if (upDateResponse.result) {
                            return Observable.just(upDateResponse);
                        } else {
                            if (StringUtils.isEmpty(upDateResponse.errorMsg)) {
                                return Observable.error(new RequestErrorThrowable("-1", "系统错误"));
                            }
                            return Observable.error(new RequestErrorThrowable(upDateResponse.errorCode, upDateResponse.errorMsg));
                        }
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    //获取启动页图片
    public Observable<SplashImageResponse> getSplashImage(SplashImageRequest request) {
        return interfaceService.getSplashImage(RxHttpHelper.convertRequestToJson(request))
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends SplashImageResponse>>() {
                    @Override
                    public Observable<? extends SplashImageResponse> call(Throwable throwable) {
                        return Observable.error(RxHttpHelper.convertIOEError(throwable));
                    }
                }).flatMap(new Func1<SplashImageResponse, Observable<SplashImageResponse>>() {
                    @Override
                    public Observable<SplashImageResponse> call(SplashImageResponse splashImageResponse) {
                        if (splashImageResponse == null) {
                            return Observable.error(new RequestErrorThrowable(HttpErrorInfo.CODE_OF_PARSE_REQUEST_FAILURE,
                                    HttpErrorInfo.MSG_OF_PARSE_REQUEST_FAILURE));
                        }
                        if (splashImageResponse.result) {
                            return Observable.just(splashImageResponse);
                        } else {
                            if (StringUtils.isEmpty(splashImageResponse.errorMsg)) {
                                return Observable.error(new RequestErrorThrowable("-1", "系统错误"));
                            }
                            return Observable.error(new RequestErrorThrowable(splashImageResponse.errorCode, splashImageResponse.errorMsg));
                        }
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }


    //获取理财产品列表
    public Observable<ManageListResponse> getManageList(ManageListRequest requset) {
        return interfaceService.getManageList(RxHttpHelper.convertRequestToJson(requset))
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends ManageListResponse>>() {
                    @Override
                    public Observable<? extends ManageListResponse> call(Throwable throwable) {
                        return Observable.error(RxHttpHelper.convertIOEError(throwable));
                    }
                }).flatMap(new Func1<ManageListResponse, Observable<ManageListResponse>>() {
                    @Override
                    public Observable<ManageListResponse> call(ManageListResponse manageListResponse) {
                        if (manageListResponse == null) {
                            return Observable.error(new RequestErrorThrowable(HttpErrorInfo.CODE_OF_PARSE_REQUEST_FAILURE,
                                    HttpErrorInfo.MSG_OF_PARSE_REQUEST_FAILURE));
                        }
                        if (manageListResponse.result) {
                            return Observable.just(manageListResponse);
                        } else {
                            if (StringUtils.isEmpty(manageListResponse.errorMsg)) {
                                return Observable.error(new RequestErrorThrowable("-1", "系统错误"));
                            }
                            return Observable.error(new RequestErrorThrowable(manageListResponse.errorCode, manageListResponse.errorMsg));
                        }
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }


    //修改手机号码
    public Observable<ChangePhoneNumResponse> changePhoneNum(ChangePhoneNumRequest request) {
        return interfaceService.changePhoneNum(RxHttpHelper.convertRequestToJson(request))
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends ChangePhoneNumResponse>>() {
                    @Override
                    public Observable<? extends ChangePhoneNumResponse> call(Throwable throwable) {
                        return Observable.error(RxHttpHelper.convertIOEError(throwable));
                    }
                }).flatMap(new Func1<ChangePhoneNumResponse, Observable<ChangePhoneNumResponse>>() {
                    @Override
                    public Observable<ChangePhoneNumResponse> call(ChangePhoneNumResponse changePhoneNumResponse) {
                        if (changePhoneNumResponse == null) {
                            return Observable.error(new RequestErrorThrowable(HttpErrorInfo.CODE_OF_PARSE_REQUEST_FAILURE,
                                    HttpErrorInfo.MSG_OF_PARSE_REQUEST_FAILURE));
                        }
                        if (changePhoneNumResponse.result) {
                            return Observable.just(changePhoneNumResponse);
                        } else {
                            if (StringUtils.isEmpty(changePhoneNumResponse.errorMsg)) {
                                return Observable.error(new RequestErrorThrowable("-1", "系统错误"));
                            }
                            return Observable.error(new RequestErrorThrowable(changePhoneNumResponse.errorCode, changePhoneNumResponse.errorMsg));
                        }
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    //我的邀请的好友
    public Observable<MyInviteFriendResponse> myInviteFriendInfo(MyInviteFriendRequest request) {
        return interfaceService.myInviteFriend(RxHttpHelper.convertRequestToJson(request))
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends MyInviteFriendResponse>>() {
                    @Override
                    public Observable<? extends MyInviteFriendResponse> call(Throwable throwable) {
                        return Observable.error(RxHttpHelper.convertIOEError(throwable));
                    }
                }).flatMap(new Func1<MyInviteFriendResponse, Observable<MyInviteFriendResponse>>() {
                    @Override
                    public Observable<MyInviteFriendResponse> call(MyInviteFriendResponse myInviteFriendResponse) {
                        if (myInviteFriendResponse == null) {
                            return Observable.error(new RequestErrorThrowable(HttpErrorInfo.CODE_OF_PARSE_REQUEST_FAILURE,
                                    HttpErrorInfo.MSG_OF_PARSE_REQUEST_FAILURE));
                        }
                        if (myInviteFriendResponse.result) {
                            return Observable.just(myInviteFriendResponse);
                        } else {
                            if (StringUtils.isEmpty(myInviteFriendResponse.errorMsg)) {
                                return Observable.error(new RequestErrorThrowable("-1", "系统错误"));
                            }
                            return Observable.error(new RequestErrorThrowable(myInviteFriendResponse.errorCode, myInviteFriendResponse.errorMsg));
                        }
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    //我的邀请的奖励
    public Observable<MyInviteMoneyResponse> myInviteMoney(MyInviteMoneyRequest request) {
        return interfaceService.myInviteMoney(RxHttpHelper.convertRequestToJson(request))
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends MyInviteMoneyResponse>>() {
                    @Override
                    public Observable<? extends MyInviteMoneyResponse> call(Throwable throwable) {
                        return Observable.error(RxHttpHelper.convertIOEError(throwable));
                    }
                }).flatMap(new Func1<MyInviteMoneyResponse, Observable<MyInviteMoneyResponse>>() {
                    @Override
                    public Observable<MyInviteMoneyResponse> call(MyInviteMoneyResponse myInviteMoneyResponse) {
                        if (myInviteMoneyResponse == null) {
                            return Observable.error(new RequestErrorThrowable(HttpErrorInfo.CODE_OF_PARSE_REQUEST_FAILURE,
                                    HttpErrorInfo.MSG_OF_PARSE_REQUEST_FAILURE));
                        }
                        if (myInviteMoneyResponse.result) {
                            return Observable.just(myInviteMoneyResponse);
                        } else {
                            if (StringUtils.isEmpty(myInviteMoneyResponse.errorMsg)) {
                                return Observable.error(new RequestErrorThrowable("-1", "系统错误"));
                            }
                            return Observable.error(new RequestErrorThrowable(myInviteMoneyResponse.errorCode, myInviteMoneyResponse.errorMsg));
                        }
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    //银行卡信息
    public Observable<MyBankCardResponse> myBankCard(MyBankCardRequest request) {
        return interfaceService.myBankCard(RxHttpHelper.convertRequestToJson(request))
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends MyBankCardResponse>>() {
                    @Override
                    public Observable<? extends MyBankCardResponse> call(Throwable throwable) {
                        return Observable.error(RxHttpHelper.convertIOEError(throwable));
                    }
                }).flatMap(new Func1<MyBankCardResponse, Observable<MyBankCardResponse>>() {
                    @Override
                    public Observable<MyBankCardResponse> call(MyBankCardResponse myBankCardResponse) {
                        if (myBankCardResponse == null) {
                            return Observable.error(new RequestErrorThrowable(HttpErrorInfo.CODE_OF_PARSE_REQUEST_FAILURE,
                                    HttpErrorInfo.MSG_OF_PARSE_REQUEST_FAILURE));
                        }
                        if (myBankCardResponse.result) {
                            return Observable.just(myBankCardResponse);
                        } else {
                            if (StringUtils.isEmpty(myBankCardResponse.errorMsg)) {
                                return Observable.error(new RequestErrorThrowable("-1", "系统错误"));
                            }
                            return Observable.error(new RequestErrorThrowable(myBankCardResponse.errorCode, myBankCardResponse.errorMsg));
                        }
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    //发现里的新闻和消息
    public Observable<NewsNoticeResponse> newsNotice(NewsNoticeRequest request) {
        return interfaceService.newsNotice(RxHttpHelper.convertRequestToJson(request))
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends NewsNoticeResponse>>() {
                    @Override
                    public Observable<? extends NewsNoticeResponse> call(Throwable throwable) {
                        return Observable.error(RxHttpHelper.convertIOEError(throwable));
                    }
                }).flatMap(new Func1<NewsNoticeResponse, Observable<NewsNoticeResponse>>() {
                    @Override
                    public Observable<NewsNoticeResponse> call(NewsNoticeResponse newsNoticeResponse) {
                        if (newsNoticeResponse == null) {
                            return Observable.error(new RequestErrorThrowable(HttpErrorInfo.CODE_OF_PARSE_REQUEST_FAILURE,
                                    HttpErrorInfo.MSG_OF_PARSE_REQUEST_FAILURE));
                        }
                        if (newsNoticeResponse.result) {
                            return Observable.just(newsNoticeResponse);
                        } else {
                            if (StringUtils.isEmpty(newsNoticeResponse.errorMsg)) {
                                return Observable.error(new RequestErrorThrowable("-1", "系统错误"));
                            }
                            return Observable.error(new RequestErrorThrowable(newsNoticeResponse.errorCode, newsNoticeResponse.errorMsg));
                        }
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    //新闻、消息详情
    public Observable<NewsNoticeDetailResponse> newsNoticeDetail(NewsNoticeDetailRequest request) {
        return interfaceService.newsNoticeDetail(RxHttpHelper.convertRequestToJson(request))
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends NewsNoticeDetailResponse>>() {
                    @Override
                    public Observable<? extends NewsNoticeDetailResponse> call(Throwable throwable) {
                        return Observable.error(RxHttpHelper.convertIOEError(throwable));
                    }
                }).flatMap(new Func1<NewsNoticeDetailResponse, Observable<NewsNoticeDetailResponse>>() {
                    @Override
                    public Observable<NewsNoticeDetailResponse> call(NewsNoticeDetailResponse newsNoticeDetailResponse) {
                        if (newsNoticeDetailResponse == null) {
                            return Observable.error(new RequestErrorThrowable(HttpErrorInfo.CODE_OF_PARSE_REQUEST_FAILURE,
                                    HttpErrorInfo.MSG_OF_PARSE_REQUEST_FAILURE));
                        }
                        if (newsNoticeDetailResponse.result) {
                            return Observable.just(newsNoticeDetailResponse);
                        } else {
                            if (StringUtils.isEmpty(newsNoticeDetailResponse.errorMsg)) {
                                return Observable.error(new RequestErrorThrowable("-1", "系统错误"));
                            }
                            return Observable.error(new RequestErrorThrowable(newsNoticeDetailResponse.errorCode, newsNoticeDetailResponse.errorMsg));
                        }
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    //我的消息列表
    public Observable<MessageListResponse> getMessageList(MessageListRequest request) {
        return interfaceService.messageList(RxHttpHelper.convertRequestToJson(request))
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends MessageListResponse>>() {
                    @Override
                    public Observable<? extends MessageListResponse> call(Throwable throwable) {
                        return Observable.error(RxHttpHelper.convertIOEError(throwable));
                    }
                }).flatMap(new Func1<MessageListResponse, Observable<MessageListResponse>>() {
                    @Override
                    public Observable<MessageListResponse> call(MessageListResponse messageListResponse) {
                        if (messageListResponse == null) {
                            return Observable.error(new RequestErrorThrowable(HttpErrorInfo.CODE_OF_PARSE_REQUEST_FAILURE,
                                    HttpErrorInfo.MSG_OF_PARSE_REQUEST_FAILURE));
                        }
                        if (messageListResponse.result) {
                            return Observable.just(messageListResponse);
                        } else {
                            if (StringUtils.isEmpty(messageListResponse.errorMsg)) {
                                return Observable.error(new RequestErrorThrowable("-1", "系统错误"));
                            }
                            return Observable.error(new RequestErrorThrowable(messageListResponse.errorCode, messageListResponse.errorMsg));
                        }
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    //我的消息详情
    public Observable<MessageDetailResponse> messageDetail(MessageDetailRequest request) {
        return interfaceService.messageDetail(RxHttpHelper.convertRequestToJson(request))
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends MessageDetailResponse>>() {
                    @Override
                    public Observable<? extends MessageDetailResponse> call(Throwable throwable) {
                        return Observable.error(RxHttpHelper.convertIOEError(throwable));
                    }
                }).flatMap(new Func1<MessageDetailResponse, Observable<MessageDetailResponse>>() {
                    @Override
                    public Observable<MessageDetailResponse> call(MessageDetailResponse messageDetailResponse) {
                        if (messageDetailResponse == null) {
                            return Observable.error(new RequestErrorThrowable(HttpErrorInfo.CODE_OF_PARSE_REQUEST_FAILURE,
                                    HttpErrorInfo.MSG_OF_PARSE_REQUEST_FAILURE));
                        }
                        if (messageDetailResponse.result) {
                            return Observable.just(messageDetailResponse);
                        } else {
                            if (StringUtils.isEmpty(messageDetailResponse.errorMsg)) {
                                return Observable.error(new RequestErrorThrowable("-1", "系统错误"));
                            }
                            return Observable.error(new RequestErrorThrowable(messageDetailResponse.errorCode, messageDetailResponse.errorMsg));
                        }
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    //获取产品详情
    public Observable<ManageDetailResponse> getManageDetail(ManageDetailRequest request) {
        return interfaceService.getManageDetail(RxHttpHelper.convertRequestToJson(request))
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends ManageDetailResponse>>() {
                    @Override
                    public Observable<? extends ManageDetailResponse> call(Throwable throwable) {
                        return Observable.error(RxHttpHelper.convertIOEError(throwable));
                    }
                })
                .flatMap(new Func1<ManageDetailResponse, Observable<ManageDetailResponse>>() {
                    @Override
                    public Observable<ManageDetailResponse> call(ManageDetailResponse manageDetailResponse) {
                        if (manageDetailResponse == null) {
                            return Observable.error(new RequestErrorThrowable(HttpErrorInfo.CODE_OF_PARSE_REQUEST_FAILURE,
                                    HttpErrorInfo.MSG_OF_PARSE_REQUEST_FAILURE));
                        }
                        if (manageDetailResponse.result) {
                            return Observable.just(manageDetailResponse);
                        } else {
                            if (StringUtils.isEmpty(manageDetailResponse.errorMsg)) {
                                return Observable.error(new RequestErrorThrowable("-1", "系统错误"));
                            }
                            return Observable.error(new RequestErrorThrowable(manageDetailResponse.errorCode, manageDetailResponse.errorMsg));
                        }
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    //获取项目可用红包列表
    public Observable<GetRedPacketResponse> getCanUseRedPacket(GetRedPacketRequest redPacketRequest) {
        return interfaceService.getCanUseRedPacket(RxHttpHelper.convertRequestToJson(redPacketRequest))
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends GetRedPacketResponse>>() {
                    @Override
                    public Observable<? extends GetRedPacketResponse> call(Throwable throwable) {
                        return Observable.error(RxHttpHelper.convertIOEError(throwable));
                    }
                }).flatMap(new Func1<GetRedPacketResponse, Observable<GetRedPacketResponse>>() {
                    @Override
                    public Observable<GetRedPacketResponse> call(GetRedPacketResponse getRedPacketResponse) {
                        if (getRedPacketResponse == null) {
                            return Observable.error(new RequestErrorThrowable(HttpErrorInfo.CODE_OF_PARSE_REQUEST_FAILURE,
                                    HttpErrorInfo.MSG_OF_PARSE_REQUEST_FAILURE));
                        }
                        if (getRedPacketResponse.result) {
                            return Observable.just(getRedPacketResponse);
                        } else {
                            if (StringUtils.isEmpty(getRedPacketResponse.errorMsg)) {
                                return Observable.error(new RequestErrorThrowable("-1", "系统错误"));
                            }
                            return Observable.error(new RequestErrorThrowable(getRedPacketResponse.errorCode, getRedPacketResponse.errorMsg));
                        }
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

    }

    //资产首页数据
    public Observable<PropertyHomeResponse> getPropertyData(PropertyHomeRequest request) {
        return interfaceService.getPropertyData(RxHttpHelper.convertRequestToJson(request))
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends PropertyHomeResponse>>() {
                    @Override
                    public Observable<? extends PropertyHomeResponse> call(Throwable throwable) {
                        return Observable.error(RxHttpHelper.convertIOEError(throwable));
                    }
                }).flatMap(new Func1<PropertyHomeResponse, Observable<PropertyHomeResponse>>() {
                    @Override
                    public Observable<PropertyHomeResponse> call(PropertyHomeResponse propertyHomeResponse) {
                        if (propertyHomeResponse == null) {
                            return Observable.error(new RequestErrorThrowable(HttpErrorInfo.CODE_OF_PARSE_REQUEST_FAILURE,
                                    HttpErrorInfo.MSG_OF_PARSE_REQUEST_FAILURE));
                        }
                        if (propertyHomeResponse.result) {
                            DataCache.instance.saveCacheData("haili", "PropertyHomeResponse", propertyHomeResponse);
                            return Observable.just(propertyHomeResponse);
                        } else {
                            if (StringUtils.isEmpty(propertyHomeResponse.errorMsg)) {
                                return Observable.error(new RequestErrorThrowable("-1", "系统错误"));
                            }
                            return Observable.error(new RequestErrorThrowable(propertyHomeResponse.errorCode, propertyHomeResponse.errorMsg));
                        }
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    //存钱罐
    public Observable<PiggBankResponse> getPiggBankData(PiggBankRequest request) {
        return interfaceService.getPiggBankData(RxHttpHelper.convertRequestToJson(request))
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends PiggBankResponse>>() {
                    @Override
                    public Observable<? extends PiggBankResponse> call(Throwable throwable) {
                        return Observable.error(RxHttpHelper.convertIOEError(throwable));
                    }
                }).flatMap(new Func1<PiggBankResponse, Observable<PiggBankResponse>>() {
                    @Override
                    public Observable<PiggBankResponse> call(PiggBankResponse piggBankResponse) {
                        if (piggBankResponse == null) {
                            return Observable.error(new RequestErrorThrowable(HttpErrorInfo.CODE_OF_PARSE_REQUEST_FAILURE,
                                    HttpErrorInfo.MSG_OF_PARSE_REQUEST_FAILURE));
                        }
                        if (piggBankResponse.result) {
                            return Observable.just(piggBankResponse);
                        } else {
                            if (StringUtils.isEmpty(piggBankResponse.errorMsg)) {
                                return Observable.error(new RequestErrorThrowable("-1", "系统错误"));
                            }
                            return Observable.error(new RequestErrorThrowable(piggBankResponse.errorCode, piggBankResponse.errorMsg));
                        }
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    //定期
    public Observable<RegularResponse> getRegularData(final RegularRequest regularRequest) {
        return interfaceService.getRegularData(RxHttpHelper.convertRequestToJson(regularRequest))
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends RegularResponse>>() {
                    @Override
                    public Observable<? extends RegularResponse> call(Throwable throwable) {
                        return Observable.error(RxHttpHelper.convertIOEError(throwable));
                    }
                }).flatMap(new Func1<RegularResponse, Observable<RegularResponse>>() {
                    @Override
                    public Observable<RegularResponse> call(RegularResponse regularResponse) {
                        if (regularResponse == null) {
                            return Observable.error(new RequestErrorThrowable(HttpErrorInfo.CODE_OF_PARSE_REQUEST_FAILURE,
                                    HttpErrorInfo.MSG_OF_PARSE_REQUEST_FAILURE));
                        }
                        if (regularResponse.result) {
                            return Observable.just(regularResponse);
                        } else {
                            if (StringUtils.isEmpty(regularResponse.errorMsg)) {
                                return Observable.error(new RequestErrorThrowable("-1", "系统错误"));
                            }
                            return Observable.error(new RequestErrorThrowable(regularResponse.errorCode, regularResponse.errorMsg));
                        }
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    //交易记录
    public Observable<InvestmentRecordResponse> getTradingRecord(InvestmentRecordRequest request) {
        return interfaceService.getTradingRecord(RxHttpHelper.convertRequestToJson(request))
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends InvestmentRecordResponse>>() {
                    @Override
                    public Observable<? extends InvestmentRecordResponse> call(Throwable throwable) {
                        return Observable.error(RxHttpHelper.convertIOEError(throwable));
                    }
                })
                .flatMap(new Func1<InvestmentRecordResponse, Observable<InvestmentRecordResponse>>() {
                    @Override
                    public Observable<InvestmentRecordResponse> call(InvestmentRecordResponse investmentRecordResponse) {
                        if (investmentRecordResponse == null) {
                            return Observable.error(new RequestErrorThrowable(HttpErrorInfo.CODE_OF_PARSE_REQUEST_FAILURE,
                                    HttpErrorInfo.MSG_OF_PARSE_REQUEST_FAILURE));
                        }
                        if (investmentRecordResponse.result) {
                            return Observable.just(investmentRecordResponse);
                        } else {
                            if (StringUtils.isEmpty(investmentRecordResponse.errorMsg)) {
                                return Observable.error(new RequestErrorThrowable("-1", "系统错误"));
                            }
                            return Observable.error(new RequestErrorThrowable(investmentRecordResponse.errorCode, investmentRecordResponse.errorMsg));
                        }
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    //存钱罐明细
    public Observable<SaveDetailResponse> getSaveDetails(SaveDetailRequest request) {
        return interfaceService.getSaveDetails(RxHttpHelper.convertRequestToJson(request))
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends SaveDetailResponse>>() {
                    @Override
                    public Observable<? extends SaveDetailResponse> call(Throwable throwable) {
                        return Observable.error(RxHttpHelper.convertIOEError(throwable));
                    }
                })
                .flatMap(new Func1<SaveDetailResponse, Observable<SaveDetailResponse>>() {
                    @Override
                    public Observable<SaveDetailResponse> call(SaveDetailResponse saveDetailResponse) {
                        if (saveDetailResponse == null) {
                            return Observable.error(new RequestErrorThrowable(HttpErrorInfo.CODE_OF_PARSE_REQUEST_FAILURE,
                                    HttpErrorInfo.MSG_OF_PARSE_REQUEST_FAILURE));
                        }
                        if (saveDetailResponse.result) {
                            return Observable.just(saveDetailResponse);
                        } else {
                            if (StringUtils.isEmpty(saveDetailResponse.errorMsg)) {
                                return Observable.error(new RequestErrorThrowable("-1", "系统错误"));
                            }
                            return Observable.error(new RequestErrorThrowable(saveDetailResponse.errorCode, saveDetailResponse.errorMsg));
                        }
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    //收益明细
    public Observable<MyEarningsResponse> getMyEarnings(MyEarningsRequest request) {
        return interfaceService.getMyEarnings(RxHttpHelper.convertRequestToJson(request))
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends MyEarningsResponse>>() {
                    @Override
                    public Observable<? extends MyEarningsResponse> call(Throwable throwable) {
                        return Observable.error(RxHttpHelper.convertIOEError(throwable));
                    }
                })
                .flatMap(new Func1<MyEarningsResponse, Observable<MyEarningsResponse>>() {
                    @Override
                    public Observable<MyEarningsResponse> call(MyEarningsResponse myEarningsResponse) {
                        if (myEarningsResponse == null) {
                            return Observable.error(new RequestErrorThrowable(HttpErrorInfo.CODE_OF_PARSE_REQUEST_FAILURE,
                                    HttpErrorInfo.MSG_OF_PARSE_REQUEST_FAILURE));
                        }
                        if (myEarningsResponse.result) {
                            return Observable.just(myEarningsResponse);
                        } else {
                            if (StringUtils.isEmpty(myEarningsResponse.errorMsg)) {
                                return Observable.error(new RequestErrorThrowable("-1", "系统错误"));
                            }
                            return Observable.error(new RequestErrorThrowable(myEarningsResponse.errorCode, myEarningsResponse.errorMsg));
                        }
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    //用户意见反馈
    public Observable<UserSendMsgBackReoponse> userMessageBack(UserSendMsgBackRequest request) {
        return interfaceService.userSendMessageBack(RxHttpHelper.convertRequestToJson(request))
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends UserSendMsgBackReoponse>>() {
                    @Override
                    public Observable<? extends UserSendMsgBackReoponse> call(Throwable throwable) {
                        return Observable.error(RxHttpHelper.convertIOEError(throwable));
                    }
                }).flatMap(new Func1<UserSendMsgBackReoponse, Observable<UserSendMsgBackReoponse>>() {
                    @Override
                    public Observable<UserSendMsgBackReoponse> call(UserSendMsgBackReoponse userSendMsgBackReoponse) {
                        if (userSendMsgBackReoponse == null) {
                            return Observable.error(new RequestErrorThrowable(HttpErrorInfo.CODE_OF_PARSE_REQUEST_FAILURE,
                                    HttpErrorInfo.MSG_OF_PARSE_REQUEST_FAILURE));
                        }
                        if (userSendMsgBackReoponse.result) {
                            return Observable.just(userSendMsgBackReoponse);
                        } else {
                            if (StringUtils.isEmpty(userSendMsgBackReoponse.errorMsg)) {
                                return Observable.error(new RequestErrorThrowable("-1", "系统错误"));
                            }
                            return Observable.error(new RequestErrorThrowable(userSendMsgBackReoponse.errorCode, userSendMsgBackReoponse.errorMsg));
                        }
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

    }

    public Observable<PayPasswordResponse> payPassword(PayPasswordRequest request) {
        return interfaceService.payPassword(RxHttpHelper.convertRequestToJson(request))
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends PayPasswordResponse>>() {
                    @Override
                    public Observable<? extends PayPasswordResponse> call(Throwable throwable) {
                        return Observable.error(RxHttpHelper.convertIOEError(throwable));
                    }
                }).flatMap(new Func1<PayPasswordResponse, Observable<PayPasswordResponse>>() {
                    @Override
                    public Observable<PayPasswordResponse> call(PayPasswordResponse payPasswordResponse) {
                        if (payPasswordResponse == null) {
                            return Observable.error(new RequestErrorThrowable(HttpErrorInfo.CODE_OF_PARSE_REQUEST_FAILURE,
                                    HttpErrorInfo.MSG_OF_PARSE_REQUEST_FAILURE));
                        }
                        if (payPasswordResponse.result) {
                            return Observable.just(payPasswordResponse);
                        } else {
                            if (StringUtils.isEmpty(payPasswordResponse.errorMsg)) {
                                return Observable.error(new RequestErrorThrowable("-1", "系统错误"));
                            }
                            return Observable.error(new RequestErrorThrowable(payPasswordResponse.errorCode, payPasswordResponse.errorMsg));
                        }
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    //获取银行卡信息
    public Observable<GetBankCardDataResponse> getBankCardData(GetBankCardDataRequest request) {
        return interfaceService.getBankCardData(RxHttpHelper.convertRequestToJson(request))
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends GetBankCardDataResponse>>() {
                    @Override
                    public Observable<? extends GetBankCardDataResponse> call(Throwable throwable) {
                        return Observable.error(RxHttpHelper.convertIOEError(throwable));
                    }
                })
                .flatMap(new Func1<GetBankCardDataResponse, Observable<GetBankCardDataResponse>>() {
                    @Override
                    public Observable<GetBankCardDataResponse> call(GetBankCardDataResponse getBankCardDataResponse) {
                        if (getBankCardDataResponse == null) {
                            return Observable.error(new RequestErrorThrowable(HttpErrorInfo.CODE_OF_PARSE_REQUEST_FAILURE,
                                    HttpErrorInfo.MSG_OF_PARSE_REQUEST_FAILURE));
                        }
                        if (getBankCardDataResponse.result) {
                            return Observable.just(getBankCardDataResponse);
                        } else {
                            if (StringUtils.isEmpty(getBankCardDataResponse.errorMsg)) {
                                return Observable.error(new RequestErrorThrowable("-1", "系统错误"));
                            }
                            return Observable.error(new RequestErrorThrowable(getBankCardDataResponse.errorCode, getBankCardDataResponse.errorMsg));
                        }
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    //充值
    public Observable<DoRechargeResponse> doRecharge(DoRechargeRequest request) {
        return interfaceService.doRecharge(RxHttpHelper.convertRequestToJson(request))
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends DoRechargeResponse>>() {
                    @Override
                    public Observable<? extends DoRechargeResponse> call(Throwable throwable) {
                        return Observable.error(RxHttpHelper.convertIOEError(throwable));
                    }
                })
                .flatMap(new Func1<DoRechargeResponse, Observable<DoRechargeResponse>>() {
                    @Override
                    public Observable<DoRechargeResponse> call(DoRechargeResponse doRechargeResponse) {
                        if (doRechargeResponse == null) {
                            return Observable.error(new RequestErrorThrowable(HttpErrorInfo.CODE_OF_PARSE_REQUEST_FAILURE,
                                    HttpErrorInfo.MSG_OF_PARSE_REQUEST_FAILURE));
                        }
                        if (doRechargeResponse.result) {
                            return Observable.just(doRechargeResponse);
                        } else {
                            if (StringUtils.isEmpty(doRechargeResponse.errorMsg)) {
                                return Observable.error(new RequestErrorThrowable("-1", "系统错误"));
                            }
                            return Observable.error(new RequestErrorThrowable(doRechargeResponse.errorCode, doRechargeResponse.errorMsg));
                        }
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    //手机短信验证码检测
    public Observable<CheckMessageCodeResponse> checkMessageCode(CheckMessageCodeRequest request) {
        return interfaceService.checkMsgCode(RxHttpHelper.convertRequestToJson(request))
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends CheckMessageCodeResponse>>() {
                    @Override
                    public Observable<? extends CheckMessageCodeResponse> call(Throwable throwable) {
                        return Observable.error(RxHttpHelper.convertIOEError(throwable));
                    }
                }).flatMap(new Func1<CheckMessageCodeResponse, Observable<CheckMessageCodeResponse>>() {
                    @Override
                    public Observable<CheckMessageCodeResponse> call(CheckMessageCodeResponse checkMessageCodeResponse) {
                        if (checkMessageCodeResponse == null) {
                            return Observable.error(new RequestErrorThrowable(HttpErrorInfo.CODE_OF_PARSE_REQUEST_FAILURE,
                                    HttpErrorInfo.MSG_OF_PARSE_REQUEST_FAILURE));
                        }
                        if (checkMessageCodeResponse.result) {
                            return Observable.just(checkMessageCodeResponse);
                        } else {
                            if (StringUtils.isEmpty(checkMessageCodeResponse.errorMsg)) {
                                return Observable.error(new RequestErrorThrowable("-1", "系统错误"));
                            }
                            return Observable.error(new RequestErrorThrowable(checkMessageCodeResponse.errorCode, checkMessageCodeResponse.errorMsg));
                        }
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<RetrievePasswordResponse> retrivePsd(RetrievePasswordRequest request) {
        return interfaceService.retrivePassword(RxHttpHelper.convertRequestToJson(request))
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends RetrievePasswordResponse>>() {
                    @Override
                    public Observable<? extends RetrievePasswordResponse> call(Throwable throwable) {
                        return Observable.error(RxHttpHelper.convertIOEError(throwable));
                    }
                }).flatMap(new Func1<RetrievePasswordResponse, Observable<RetrievePasswordResponse>>() {
                    @Override
                    public Observable<RetrievePasswordResponse> call(RetrievePasswordResponse retrievePasswordResponse) {
                        if (retrievePasswordResponse == null) {
                            return Observable.error(new RequestErrorThrowable(HttpErrorInfo.CODE_OF_PARSE_REQUEST_FAILURE,
                                    HttpErrorInfo.MSG_OF_PARSE_REQUEST_FAILURE));
                        }
                        if (retrievePasswordResponse.result) {
                            return Observable.just(retrievePasswordResponse);
                        } else {
                            if (StringUtils.isEmpty(retrievePasswordResponse.errorMsg)) {
                                return Observable.error(new RequestErrorThrowable("-1", "系统错误"));
                            }
                            return Observable.error(new RequestErrorThrowable(retrievePasswordResponse.errorCode, retrievePasswordResponse.errorMsg));
                        }
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    //投资详情
    public Observable<InvestmentDetailResponse> investmentDetail(InvestmentDetailRequest request) {
        return interfaceService.investmentDetail(RxHttpHelper.convertRequestToJson(request))
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends InvestmentDetailResponse>>() {
                    @Override
                    public Observable<? extends InvestmentDetailResponse> call(Throwable throwable) {
                        return Observable.error(RxHttpHelper.convertIOEError(throwable));
                    }
                }).flatMap(new Func1<InvestmentDetailResponse, Observable<InvestmentDetailResponse>>() {
                    @Override
                    public Observable<InvestmentDetailResponse> call(InvestmentDetailResponse investmentDetailResponse) {
                        if (investmentDetailResponse == null) {
                            return Observable.error(new RequestErrorThrowable(HttpErrorInfo.CODE_OF_PARSE_REQUEST_FAILURE,
                                    HttpErrorInfo.MSG_OF_PARSE_REQUEST_FAILURE));
                        }
                        if (investmentDetailResponse.result) {
                            return Observable.just(investmentDetailResponse);
                        } else {
                            if (StringUtils.isEmpty(investmentDetailResponse.errorMsg)) {
                                return Observable.error(new RequestErrorThrowable("-1", "系统错误"));
                            }
                            return Observable.error(new RequestErrorThrowable(investmentDetailResponse.errorCode, investmentDetailResponse.errorMsg));
                        }
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    //还款日历
    public Observable<RePayCalendarResponse> repayCalendar(RePayCalendarRequest request) {
        return interfaceService.rePayCalendar(RxHttpHelper.convertRequestToJson(request))
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends RePayCalendarResponse>>() {
                    @Override
                    public Observable<? extends RePayCalendarResponse> call(Throwable throwable) {
                        return Observable.error(RxHttpHelper.convertIOEError(throwable));
                    }
                }).flatMap(new Func1<RePayCalendarResponse, Observable<RePayCalendarResponse>>() {
                    @Override
                    public Observable<RePayCalendarResponse> call(RePayCalendarResponse rePayCalendarResponse) {
                        if (rePayCalendarResponse == null) {
                            return Observable.error(new RequestErrorThrowable(HttpErrorInfo.CODE_OF_PARSE_REQUEST_FAILURE,
                                    HttpErrorInfo.MSG_OF_PARSE_REQUEST_FAILURE));
                        }
                        if (rePayCalendarResponse.result) {
                            return Observable.just(rePayCalendarResponse);
                        } else {
                            if (StringUtils.isEmpty(rePayCalendarResponse.errorMsg)) {
                                return Observable.error(new RequestErrorThrowable("-1", "系统错误"));
                            }
                            return Observable.error(new RequestErrorThrowable(rePayCalendarResponse.errorCode, rePayCalendarResponse.errorMsg));
                        }
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    //还款计划
    public Observable<RePayPlanResponse> repayPlan(RePayPlanRequest request) {
        return interfaceService.rePayPlan(RxHttpHelper.convertRequestToJson(request))
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends RePayPlanResponse>>() {
                    @Override
                    public Observable<? extends RePayPlanResponse> call(Throwable throwable) {
                        return Observable.error(RxHttpHelper.convertIOEError(throwable));
                    }
                }).flatMap(new Func1<RePayPlanResponse, Observable<RePayPlanResponse>>() {
                    @Override
                    public Observable<RePayPlanResponse> call(RePayPlanResponse rePayPlanResponse) {
                        if (rePayPlanResponse == null) {
                            return Observable.error(new RequestErrorThrowable(HttpErrorInfo.CODE_OF_PARSE_REQUEST_FAILURE,
                                    HttpErrorInfo.MSG_OF_PARSE_REQUEST_FAILURE));
                        }
                        if (rePayPlanResponse.result) {
                            return Observable.just(rePayPlanResponse);
                        } else {
                            if (StringUtils.isEmpty(rePayPlanResponse.errorMsg)) {
                                return Observable.error(new RequestErrorThrowable("-1", "系统错误"));
                            }
                            return Observable.error(new RequestErrorThrowable(rePayPlanResponse.errorCode, rePayPlanResponse.errorMsg));
                        }
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    //投资
    public Observable<InvestResponse> doInvest(InvestRequest request) {
        return interfaceService.doInvest(RxHttpHelper.convertRequestToJson(request))
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends InvestResponse>>() {
                    @Override
                    public Observable<? extends InvestResponse> call(Throwable throwable) {
                        return Observable.error(RxHttpHelper.convertIOEError(throwable));
                    }
                })
                .flatMap(new Func1<InvestResponse, Observable<InvestResponse>>() {
                    @Override
                    public Observable<InvestResponse> call(InvestResponse investResponse) {
                        if (investResponse == null) {
                            return Observable.error(new RequestErrorThrowable(HttpErrorInfo.CODE_OF_PARSE_REQUEST_FAILURE,
                                    HttpErrorInfo.MSG_OF_PARSE_REQUEST_FAILURE));
                        }
                        if (investResponse.result) {
                            return Observable.just(investResponse);
                        } else {
                            if (StringUtils.isEmpty(investResponse.errorMsg)) {
                                return Observable.error(new RequestErrorThrowable("-1", "系统错误"));
                            }
                            return Observable.error(new RequestErrorThrowable(investResponse.errorCode, investResponse.errorMsg));
                        }
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    //提现
    public Observable<WithdrawalsResponse> doWithdrawals(WithdrawalsRequest request) {
        return interfaceService.doWithdrawals(RxHttpHelper.convertRequestToJson(request))
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends WithdrawalsResponse>>() {
                    @Override
                    public Observable<? extends WithdrawalsResponse> call(Throwable throwable) {
                        return Observable.error(RxHttpHelper.convertIOEError(throwable));
                    }
                })
                .flatMap(new Func1<WithdrawalsResponse, Observable<WithdrawalsResponse>>() {
                    @Override
                    public Observable<WithdrawalsResponse> call(WithdrawalsResponse withdrawalsResponse) {
                        if (withdrawalsResponse == null) {
                            return Observable.error(new RequestErrorThrowable(HttpErrorInfo.CODE_OF_PARSE_REQUEST_FAILURE,
                                    HttpErrorInfo.MSG_OF_PARSE_REQUEST_FAILURE));
                        }
                        if (withdrawalsResponse.result) {
                            return Observable.just(withdrawalsResponse);
                        } else {
                            if (StringUtils.isEmpty(withdrawalsResponse.errorMsg)) {
                                return Observable.error(new RequestErrorThrowable("-1", "系统错误"));
                            }
                            return Observable.error(new RequestErrorThrowable(withdrawalsResponse.errorCode, withdrawalsResponse.errorMsg));
                        }
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
