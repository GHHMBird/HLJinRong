package com.haili.finance.http;

import com.haili.finance.business.Index.GetHomeBannerResponse;
import com.haili.finance.business.Index.GetHomeIconResponse;
import com.haili.finance.business.Index.GetHomeNoticeResponse;
import com.haili.finance.business.Index.GetHomeProductResponse;
import com.haili.finance.business.Index.SplashImageResponse;
import com.haili.finance.business.manage.GetRedPacketResponse;
import com.haili.finance.business.manage.InvestResponse;
import com.haili.finance.business.manage.ManageDetailResponse;
import com.haili.finance.business.manage.ManageListResponse;
import com.haili.finance.business.property.DoRechargeResponse;
import com.haili.finance.business.property.GetBankCardDataResponse;
import com.haili.finance.business.property.InvestmentDetailResponse;
import com.haili.finance.business.property.InvestmentRecordResponse;
import com.haili.finance.business.property.MyEarningsResponse;
import com.haili.finance.business.property.PiggBankResponse;
import com.haili.finance.business.property.PropertyHomeResponse;
import com.haili.finance.business.property.RePayCalendarResponse;
import com.haili.finance.business.property.RePayPlanResponse;
import com.haili.finance.business.property.RegularResponse;
import com.haili.finance.business.property.SaveDetailResponse;
import com.haili.finance.business.property.WithdrawalsResponse;
import com.haili.finance.business.user.AddBankCardResponse;
import com.haili.finance.business.user.BankListResponse;
import com.haili.finance.business.user.ChangeLoginPsdResponse;
import com.haili.finance.business.user.ChangePhoneNumResponse;
import com.haili.finance.business.user.CheckMessageCodeResponse;
import com.haili.finance.business.user.GetInviteCodeResponse;
import com.haili.finance.business.user.GetRedPackageResponse;
import com.haili.finance.business.user.LoginResponse;
import com.haili.finance.business.user.MessageDetailResponse;
import com.haili.finance.business.user.MessageListResponse;
import com.haili.finance.business.user.MyBankCardResponse;
import com.haili.finance.business.user.MyInviteFriendResponse;
import com.haili.finance.business.user.MyInviteMoneyResponse;
import com.haili.finance.business.user.NewsNoticeDetailResponse;
import com.haili.finance.business.user.NewsNoticeResponse;
import com.haili.finance.business.user.PayPasswordResponse;
import com.haili.finance.business.user.RealNameResponse;
import com.haili.finance.business.user.RegisterMsgCodeResponse;
import com.haili.finance.business.user.RemoveBankCardResponse;
import com.haili.finance.business.user.RetrievePasswordResponse;
import com.haili.finance.business.user.ShareInfoResponse;
import com.haili.finance.business.user.UpDateResponse;
import com.haili.finance.business.user.UserGetVerificationCodeResponse;
import com.haili.finance.business.user.UserSendMsgBackReoponse;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/*
 * Created by lfu on 2017/2/21.
 */

public interface InterfaceService {

    String API_LOGIN = "user/login";//登录
    String API_REGISTER = "user/register";//注册
    String API_HOME_ICON = "index/iconList";//首页gridview信息
    String API_HOME_NOTICE = "index/notice";//首页公告
    String API_INVITE_CODE = "user/v/getQRCode";//邀请好友页面信息
    String API_RED_PACKAGE = "user/v/myRedBag";//红包
    String API_HOME_BANNER = "index/bannerList";//首页banner
    String API_REGISTER_SEND_CODE = "user/sendCode/";//获取短信验证码
    String API_ADD_BANK_CARD = "bank/bankCardBinding";//添加银行卡
    String API_HOME_PRODUCT = "index/recommendProduct";//首页推荐产品
    String API_GET_VERIFICATION_CODE = "user/imgCode/";//获取图形验证码
    String API_CHANGE_LOGIN_PASSWORD = "user/v/editPwd/";//修改登录密码
    String API_REAL_NAME = "user/v/realNameAuthentication";//实名验证第一步
    String API_BANK_LIST = "bank/bankCardList";//银行卡开户行列表
    String API_SHARE_INFO = "user/v/shareInvitation";//分享(公共接口)
    String API_REMOVE_BANK_CARD = "bank/bankCardUnbundling";//银行卡解绑
    String API_MY_INFO = "user/v/getUserInfo";//我的界面
    String API_UP_DATE = "index/appUpdate";//更新
    String API_GET_SPLASH_IMAGE = "index/startPage"; //获取开启页面图片
    String API_VERIFYCODE_LOGIN = "user/verifyCodeLogin"; //验证码登录
    String API_MANAGE_LIST = "loan/loanList";  //理财项目列表
    String API_CHANGE_PHONE_NUM = "user/modifyPhoneNumber"; //更改手机号
    String API_MY_INVITE_FRIEND = "user/v/inviteList"; //获取已经邀请的用户
    String API_MY_INVITE_MONEY = "user/v/inviteRewardList"; //邀请用户获得的奖励
    String API_MY_BANK_CARD = "bank/myBankCard"; //我绑定的银行卡
    String API_NEWS_NOTICE = "news/list"; //新闻，公告
    String API_NEWS_NOTICE_DETAIL = "news/detail"; //新闻，公告详情
    String API_MESSAGE_LIST = "msg/msgList"; //消息列表
    String API_MESSAGE_DETAIL = "msg/msgDetailed"; //消息详情
    String API_MANAGE_DETAIL = "loan/loanDetailed";  //产品详情
    String API_CAN_USE_REDPACKET = "user/v/myRedBagByProduct";//可用红包列表
    String API_PROPERTY_HOME = "assets/assetsInfo"; // 资产首页
    String API_INCOMEDETAILS = "assets/savingBox";//存钱罐
    String API_REGULAR = "assets/regularFinancing";//定期
    String API_INVESTMENT_RECORD = "assets/investmentRecord";//投资还款记录
    String API_INCOME_DETAILS = "assets/incomeDetails";//存钱罐明细
    String API_MY_EARNINGS = "assets/incomeRecord";//收益明细
    String API_USER_MESSAGE_BACK = "user/v/freeback/add"; //意见反馈
    String API_PAY_PASSWORD = "user/v/setPaymentPwd"; //用户反馈
    String API_GET_BANKCARD_DATA = "bank/getBalanceBankCard"; // 获取已经绑定的银行卡的信息和账户余额
    String API_RECHARGE = "assets/recharge";  // 充值
    String API_CHECK_MSG_CODE = "user/checkMobileCode";//验证码检测
    String API_RETRIEVE_PWD = "user/retrievePwd";//找回密码
    String API_INVESTMENT_DETAILS = "assets/investmentDetails";//投资详情（项目概览）
    String API_REPAY_CALENDAR = "assets/repaymentCalendar";//还款日历
    String API_REPAY_PLAN = "assets/repaymentPlan";//还款计划
    String API_INVEST = "invest/invest"; //投资
    String API_WITHDRAWALS = "assets/withdrawals" ;//提现

    @POST(API_GET_VERIFICATION_CODE)
    Observable<UserGetVerificationCodeResponse> getCode(@Body String request);

    @POST(API_REGISTER_SEND_CODE)
    Observable<RegisterMsgCodeResponse> sendCode(@Body String request);

    @POST(API_CHANGE_LOGIN_PASSWORD)
    Observable<ChangeLoginPsdResponse> changeLoginPsd(@Body String request);

    @POST(API_RED_PACKAGE)
    Observable<GetRedPackageResponse> getRedPackage(@Body String request);

    @POST(API_LOGIN)
    Observable<LoginResponse> login(@Body String request);

    @POST(API_HOME_BANNER)
    Observable<GetHomeBannerResponse> getHomeBanner(@Body String request);

    @POST(API_HOME_NOTICE)
    Observable<GetHomeNoticeResponse> getHomeNotice(@Body String request);

    @POST(API_HOME_PRODUCT)
    Observable<GetHomeProductResponse> getHomeProduct(@Body String request);

    @POST(API_INVITE_CODE)
    Observable<GetInviteCodeResponse> getInviteCode(@Body String request);

    @POST(API_HOME_ICON)
    Observable<GetHomeIconResponse> getHomeIcon(@Body String request);

    @POST(API_REAL_NAME)
    Observable<RealNameResponse> realName(@Body String request);

    @POST(API_REGISTER)
    Observable<LoginResponse> register(@Body String request);

    @POST(API_GET_SPLASH_IMAGE)
    Observable<SplashImageResponse> getSplashImage(@Body String request);

    @POST(API_ADD_BANK_CARD)
    Observable<AddBankCardResponse> addBankCard(@Body String request);

    @POST(API_BANK_LIST)
    Observable<BankListResponse> bankList(@Body String request);

    @POST(API_SHARE_INFO)
    Observable<ShareInfoResponse> shareInfo(@Body String request);


    @POST(API_REMOVE_BANK_CARD)
    Observable<RemoveBankCardResponse> removeBankCard(@Body String request);

    @POST(API_MY_INFO)
    Observable<LoginResponse> myInfo(@Body String request);

    @POST(API_VERIFYCODE_LOGIN)
    Observable<LoginResponse> verifyCodeLogin(@Body String request);

    @POST(API_UP_DATE)
    Observable<UpDateResponse> upDate(@Body String request);

    @POST(API_MANAGE_LIST)
    Observable<ManageListResponse> getManageList(@Body String string);

    @POST(API_CHANGE_PHONE_NUM)
    Observable<ChangePhoneNumResponse> changePhoneNum(@Body String request);

    @POST(API_MY_INVITE_FRIEND)
    Observable<MyInviteFriendResponse> myInviteFriend(@Body String request);

    @POST(API_MY_INVITE_MONEY)
    Observable<MyInviteMoneyResponse> myInviteMoney(@Body String request);

    @POST(API_MY_BANK_CARD)
    Observable<MyBankCardResponse> myBankCard(@Body String request);

    @POST(API_NEWS_NOTICE)
    Observable<NewsNoticeResponse> newsNotice(@Body String request);

    @POST(API_NEWS_NOTICE_DETAIL)
    Observable<NewsNoticeDetailResponse> newsNoticeDetail(@Body String request);

    @POST(API_MESSAGE_LIST)
    Observable<MessageListResponse> messageList(@Body String request);

    @POST(API_MESSAGE_DETAIL)
    Observable<MessageDetailResponse> messageDetail(@Body String request);

    @POST(API_MANAGE_DETAIL)
    Observable<ManageDetailResponse> getManageDetail(@Body String request);

    @POST(API_CAN_USE_REDPACKET)
    Observable<GetRedPacketResponse> getCanUseRedPacket(@Body String request);

    @POST(API_PROPERTY_HOME)
    Observable<PropertyHomeResponse> getPropertyData(@Body String request);

    @POST(API_INCOMEDETAILS)
    Observable<PiggBankResponse> getPiggBankData(@Body String request);

    @POST(API_REGULAR)
    Observable<RegularResponse> getRegularData(@Body String request);

    @POST(API_INVESTMENT_RECORD)
    Observable<InvestmentRecordResponse> getTradingRecord(@Body String request);

    @POST(API_INCOME_DETAILS)
    Observable<SaveDetailResponse> getSaveDetails(@Body String request);

    @POST(API_MY_EARNINGS)
    Observable<MyEarningsResponse> getMyEarnings(@Body String request);

    @POST(API_USER_MESSAGE_BACK)
    Observable<UserSendMsgBackReoponse> userSendMessageBack(@Body String request);

    @POST(API_PAY_PASSWORD)
    Observable<PayPasswordResponse> payPassword(@Body String request);

    @POST(API_GET_BANKCARD_DATA)
    Observable<GetBankCardDataResponse> getBankCardData(@Body String request);

    @POST(API_RECHARGE)
    Observable<DoRechargeResponse> doRecharge(@Body String request);

    @POST(API_CHECK_MSG_CODE)
    Observable<CheckMessageCodeResponse> checkMsgCode(@Body String request);

    @POST(API_RETRIEVE_PWD)
    Observable<RetrievePasswordResponse> retrivePassword(@Body String request);

    @POST(API_INVESTMENT_DETAILS)
    Observable<InvestmentDetailResponse> investmentDetail(@Body String request);

    @POST(API_REPAY_CALENDAR)
    Observable<RePayCalendarResponse> rePayCalendar(@Body String request);

    @POST(API_REPAY_PLAN)
    Observable<RePayPlanResponse> rePayPlan(@Body String request);

    @POST(API_INVEST)
    Observable<InvestResponse> doInvest(@Body String request);

    @POST(API_WITHDRAWALS)
    Observable<WithdrawalsResponse> doWithdrawals(@Body String request);
}
