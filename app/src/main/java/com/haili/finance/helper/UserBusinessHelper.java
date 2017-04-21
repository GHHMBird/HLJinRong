package com.haili.finance.helper;

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
import com.haili.finance.http.InterfaceAPI;

import rx.Observable;

/*
 * Created by lfu on 2017/2/22.
 */

public class UserBusinessHelper {

    /*
     * 注册
     */
    public static Observable<LoginResponse> register(RegisterRequest request) {
        return new InterfaceAPI().register(request);
    }

    /*
     * 密码登录
     */
    public static Observable<LoginResponse> pasLogin(LoginRequest request) {
        return new InterfaceAPI().pasLogin(request);
    }

    /*
     * 验证码登录
     */
    public static Observable<LoginResponse> verifyCodeLogin(VerifyCodeLoginRequest request) {
        return new InterfaceAPI().verifyCodeLogin(request);
    }

    /*
     * 获取图形验证码
     */
    public static Observable<UserGetVerificationCodeResponse> getImageCode(UserGetVerificationCodeRequest request) {
        return new InterfaceAPI().getImageCode(request);
    }

    /*
     * 获取短信验证码
     */
    public static Observable<RegisterMsgCodeResponse> getMsgCode(RegisterMsgCodeRequest request) {
        return new InterfaceAPI().getMsgCode(request);
    }

    /*
     * 更改登录密码
     */
    public static Observable<ChangeLoginPsdResponse> changeLoginPsd(ChangeLoginPsdRequest request) {
        return new InterfaceAPI().changeLoginPsd(request);
    }

    //获取红包信息
    public static Observable<GetRedPackageResponse> getRedPackage(GetRedPackageRequest request) {
        return new InterfaceAPI().getRedPackage(request);
    }

    //获取邀请页面信息
    public static Observable<GetInviteCodeResponse> getInviteInfo(GetInviteCodeRequest request) {
        return new InterfaceAPI().getInviteInfo(request);
    }

    //实名认证第一步
    public static Observable<RealNameResponse> realName(RealNameRequest request) {
        return new InterfaceAPI().realName(request);
    }

    //绑卡/添加银行卡
    public static Observable<AddBankCardResponse> addBankCard(AddBankCardRequest request) {
        return new InterfaceAPI().addBankCard(request);
    }

    //分享功能
    public static Observable<ShareInfoResponse> shareInfo(ShareInfoRequest request) {
        return new InterfaceAPI().shareInfo(request);
    }

    //银行卡解绑
    public static Observable<RemoveBankCardResponse> removeBankCard(RemoveBankCardRequest request) {
        return new InterfaceAPI().removeBankCard(request);
    }

    //个人中心
    public static Observable<LoginResponse> myInfo(MyInfoRequest request) {
        return new InterfaceAPI().myInfo(request);
    }

    //银行卡开户行列表
    public static Observable<BankListResponse> bankOpenList(BankListRequest request) {
        return new InterfaceAPI().getOpenBankList(request);
    }

    //更新版本
    public static Observable<UpDateResponse> upDateVersion(UpDateRequest request) {
        return new InterfaceAPI().upDate(request);
    }

    //更改手机号
    public static Observable<ChangePhoneNumResponse> changePhoneNum(ChangePhoneNumRequest request) {
        return new InterfaceAPI().changePhoneNum(request);
    }

    //我的邀请人
    public static Observable<MyInviteFriendResponse> myInviteFriend(MyInviteFriendRequest request) {
        return new InterfaceAPI().myInviteFriendInfo(request);
    }

    //我的邀请奖励
    public static Observable<MyInviteMoneyResponse> myInviteMoney(MyInviteMoneyRequest request) {
        return new InterfaceAPI().myInviteMoney(request);
    }

    //我的银行卡管理
    public static Observable<MyBankCardResponse> myBankCard(MyBankCardRequest request) {
        return new InterfaceAPI().myBankCard(request);
    }

    //新闻/公告获取
    public static Observable<NewsNoticeResponse> newsNoticeList(NewsNoticeRequest request) {
        return new InterfaceAPI().newsNotice(request);
    }

    //新闻/公告详情
    public static Observable<NewsNoticeDetailResponse> newsNoticeDetail(NewsNoticeDetailRequest request) {
        return new InterfaceAPI().newsNoticeDetail(request);
    }

    //消息列表
    public static Observable<MessageListResponse> getMessageList(MessageListRequest request) {
        return new InterfaceAPI().getMessageList(request);
    }

    //消息详情
    public static Observable<MessageDetailResponse> getMessageDetail(MessageDetailRequest request) {
        return new InterfaceAPI().messageDetail(request);
    }

    //用户反馈
    public static Observable<UserSendMsgBackReoponse> userMessageBack(UserSendMsgBackRequest request) {
        return new InterfaceAPI().userMessageBack(request);
    }

    //支付密码
    public static Observable<PayPasswordResponse> payPassword(PayPasswordRequest request) {
        return new InterfaceAPI().payPassword(request);
    }

    //检查手机短信验证码
    public static Observable<CheckMessageCodeResponse> checkMessageCode(CheckMessageCodeRequest request) {
        return new InterfaceAPI().checkMessageCode(request);
    }

    //召回登录密码
    public static Observable<RetrievePasswordResponse> retrivePassword(RetrievePasswordRequest request) {
        return new InterfaceAPI().retrivePsd(request);
    }
}
