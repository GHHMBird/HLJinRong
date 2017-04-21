package com.haili.finance.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haili.finance.R;
import com.haili.finance.base.BaseFragment;
import com.haili.finance.business.user.LoginResponse;
import com.haili.finance.business.user.MyInfoRequest;
import com.haili.finance.helper.UserBusinessHelper;
import com.haili.finance.helper.ViewHelper;
import com.haili.finance.rx.RequestErrorThrowable;
import com.haili.finance.stroage.DataCache;
import com.haili.finance.user.activity.BankCardActivity;
import com.haili.finance.user.activity.FindActivity;
import com.haili.finance.user.activity.IDInformationActivity;
import com.haili.finance.user.activity.InviteFriendActivity;
import com.haili.finance.user.activity.LoginActivity;
import com.haili.finance.user.activity.MessageActivity;
import com.haili.finance.user.activity.MyRedPacketActivity;
import com.haili.finance.user.activity.UserAccountSecurityActivity;
import com.haili.finance.user.activity.UserMoreActivity;
import com.mcxiaoke.bus.Bus;
import com.mcxiaoke.bus.annotation.BusReceiver;

import rx.Subscription;
import rx.functions.Action1;

/*
 * Created by Monkey on 2017/1/7.
 * 我的Fragment
 */

public class MyFragment extends BaseFragment implements View.OnClickListener {
    private LinearLayout userMoreLayout, accountSecurity, myRedPacket, invite_friend, userLoginLayout;
    private View placeView;
    private Subscription subscrip;
    private TextView userId, nameReZheng, nameReZheng2, redPackageNum;
    private int nowState = -1;
    private ImageView ivMesg;
    private String ruleName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bus.getDefault().register(this);
        View root = inflater.inflate(R.layout.fragment_my, container, false);
        initView(root);
        placeViewControl();
        init();
        return root;
    }

    private void init() {
        initListener();
    }

    @BusReceiver
    public void StringEvent(String event) {
        switch (event) {
            case "my_loading":
                getData();
                break;
            case "unLogin":
                nowState = 0;
                reSetTextAndIcon(null, 0, 0);
                break;
            case "GetUserInfoDone":
            case "REFRESH_MY_FRAGMENT":
                LoginResponse cacheData = DataCache.instance.getCacheData("haili", "MyInfoResponse");
                analyseData(cacheData);
                break;
        }
    }

    private void initListener() {
        userLoginLayout.setOnClickListener(this);
        userMoreLayout.setOnClickListener(this);
        invite_friend.setOnClickListener(this);
        accountSecurity.setOnClickListener(this);
        myRedPacket.setOnClickListener(this);
    }

    private void initView(View root) {
        root.findViewById(R.id.bank_layout).setOnClickListener(this);
        root.findViewById(R.id.find_layout).setOnClickListener(this);
        ivMesg = (ImageView) root.findViewById(R.id.my_message);
        root.findViewById(R.id.my_message).setOnClickListener(this);
        userId = (TextView) root.findViewById(R.id.my_fragment_userid);
        nameReZheng = (TextView) root.findViewById(R.id.my_is_renzheng);
        nameReZheng2 = (TextView) root.findViewById(R.id.my_is_renzheng_2);
        redPackageNum = (TextView) root.findViewById(R.id.my_red_package_num);
        userLoginLayout = (LinearLayout) root.findViewById(R.id.user_info_login);
        userMoreLayout = (LinearLayout) root.findViewById(R.id.user_more);
        accountSecurity = (LinearLayout) root.findViewById(R.id.account_security);
        placeView = root.findViewById(R.id.place_holder_view);
        invite_friend = (LinearLayout) root.findViewById(R.id.invite_friend);
        myRedPacket = (LinearLayout) root.findViewById(R.id.red_packet_layout);
        setCacheData();
    }

    private void setCacheData() {
        LoginResponse myData = DataCache.instance.getCacheData("haili", "LoginResponse");
        if (myData == null) {
            nowState = 0;
            reSetTextAndIcon(null, 0, 0);
            return;
        }
        analyseData((LoginResponse) DataCache.instance.getCacheData("haili", "MyInfoResponse"));
    }

    private void placeViewControl() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
            getActivity().getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            placeView.setVisibility(View.VISIBLE);
        } else {
            placeView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_info_login:
                if (nowState == 0) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));//登录
                } else if (nowState == 1) {
                    startActivity(new Intent(getActivity(), IDInformationActivity.class));//用户实名认证
                }
                break;
            case R.id.user_more:
                startActivity(new Intent(getActivity(), UserMoreActivity.class));//更多
                break;
            case R.id.account_security:
                if (nowState == 0) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));//登录
                } else {
                    Intent intent = new Intent(getActivity(), UserAccountSecurityActivity.class);
                    startActivity(intent);//账户安全
                }
                break;
            case R.id.red_packet_layout:
                if (nowState == 0) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));//登录
                } else {
                    startActivity(new Intent(getActivity(), MyRedPacketActivity.class));//红包
                }
                break;
            case R.id.invite_friend:
                if (nowState == 0) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));//登录
                } else {
                    startActivity(new Intent(getActivity(), InviteFriendActivity.class));//邀请好友
                }
                break;
            case R.id.my_message:
                if (nowState == 0) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));//登录
                } else {
                    startActivityForResult(new Intent(getActivity(), MessageActivity.class), 100123);//消息
                    ivMesg.setBackground(getResources().getDrawable(R.mipmap.ic_msg_my));
                }
                break;
            case R.id.find_layout:
                startActivity(new Intent(getActivity(), FindActivity.class));//发现
                break;
            case R.id.bank_layout:
                if (nowState == 0) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));//登录
                } else if (nowState == 1 || nowState == 2 || nowState == 3 || nowState == 4 || nowState == 5) {
                    Intent intent = new Intent(getActivity(), BankCardActivity.class);//绑卡界面
                    intent.putExtra("type", nowState);
                    startActivity(intent);//银行卡管理
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100123 && resultCode == 12300012) {
            ivMesg.setBackground(getResources().getDrawable(R.mipmap.message_unread));
        }
    }

    public void getData() {//更新数据使用

        subscrip= UserBusinessHelper.myInfo(new MyInfoRequest())
                .subscribe(new Action1<LoginResponse>() {
                    @Override
                    public void call(LoginResponse loginResponse) {//判断登录没登录，登录的话是否实名，是否绑卡

                        LoadingFragment loadingFragment = findLoadingFragment();
                        if (loadingFragment != null) {
                            removeLoadingFragment();
                        }

                        if (loginResponse.loginModel != null) {
                            DataCache.instance.saveCacheData("haili", "MyInfoResponse", loginResponse);
                            analyseData(loginResponse);//分析数据
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                        LoadingFragment loadingFragment = findLoadingFragment();
                        if (loadingFragment != null) {
                            removeLoadingFragment();
                        }

                        if (throwable instanceof RequestErrorThrowable) {
                            RequestErrorThrowable requestErrorThrowable = (RequestErrorThrowable) throwable;
                            if (getActivity() != null) {
                                ViewHelper.showToast(getActivity(), requestErrorThrowable.getMessage());
                            }
                        }
                    }
                });
    }

    private void analyseData(LoginResponse loginResponse) {

        //登录了
        if ("2".equals(loginResponse.loginModel.certificationState)) {//实名认证了,要去綁卡
            nowState = 2;
        } else if ("1".equals(loginResponse.loginModel.certificationState)) {//都没有，要去實名
            nowState = 1;
        } else if ("3".equals(loginResponse.loginModel.certificationState)) {//绑了卡了
            nowState = 3;
        } else if ("4".equals(loginResponse.loginModel.certificationState)) {//支付密码设置过了
            nowState = 4;
        } else if ("5".equals(loginResponse.loginModel.certificationState)) {//解绑银行卡了
            nowState = 5;
        }
        if (loginResponse.loginModel.unReadmsg > 0) {
            ivMesg.setBackground(getResources().getDrawable(R.mipmap.message_unread));
        } else {
            ivMesg.setBackground(getResources().getDrawable(R.mipmap.ic_msg_my));
        }

        if (loginResponse.loginModel.ruleName != null) {
            ruleName = loginResponse.loginModel.ruleName;
        }
        int redPacketNum = loginResponse.loginModel.redPacketNum;
        String phone = loginResponse.loginModel.phone;
        phone = phone.substring(0, 3) +  /*phone.substring(3, 7) */"****" + phone.substring(7, 11);
        if (TextUtils.isEmpty(phone)) {
            phone = "";
        }
        int bankCardNum = loginResponse.loginModel.bankNum;
        reSetTextAndIcon(phone, redPacketNum, bankCardNum);
    }

    private void reSetTextAndIcon(String phone, int redPacketNum, int bankCardNum) {
        String textString;
        switch (nowState) {
            case 0://未登录
                userId.setText("登录/注册");
                nameReZheng.setVisibility(View.GONE);
                nameReZheng2.setVisibility(View.GONE);
                redPackageNum.setVisibility(View.GONE);
                break;
            case 1://登录未实名
                userId.setText(phone);
                if (redPacketNum == 0) {
                    redPackageNum.setVisibility(View.GONE);
                } else {
                    redPackageNum.setVisibility(View.VISIBLE);
                    redPackageNum.setText(redPacketNum + "个");
                }
                nameReZheng.setText("未认证");
                nameReZheng.setVisibility(View.VISIBLE);
                nameReZheng.setBackground(getResources().getDrawable(R.drawable.bg_renzheng));
                nameReZheng2.setText("未绑卡");
                nameReZheng2.setVisibility(View.VISIBLE);
                break;
            case 2://实名未绑卡
            case 5:
                if (ruleName.length() <= 2) {
                    textString = ruleName.substring(0, 1) + "*";
                } else {
                    textString = ruleName.substring(0, 1);
                    for (int i = 0; i < ruleName.length() - 2; i++) {
                        textString += "*";
                    }
                    textString += ruleName.substring(ruleName.length() - 1);
                }
                userId.setText(phone + "\n" + textString);
                if (redPacketNum == 0) {
                    redPackageNum.setVisibility(View.GONE);
                } else {
                    redPackageNum.setVisibility(View.VISIBLE);
                    redPackageNum.setText(redPacketNum + "个");
                }
                nameReZheng.setText("未认证");
                nameReZheng.setVisibility(View.VISIBLE);
                nameReZheng.setBackground(getResources().getDrawable(R.drawable.bg_renzheng));
                nameReZheng2.setText("未绑卡");
                nameReZheng2.setVisibility(View.VISIBLE);
                break;
            case 3://绑定银行卡
            case 4:
                if (ruleName.length() <= 2) {
                    textString = ruleName.substring(0, 1) + "*";
                } else {
                    textString = ruleName.substring(0, 1);
                    for (int i = 0; i < ruleName.length() - 2; i++) {
                        textString += "*";
                    }
                    textString += ruleName.substring(ruleName.length() - 1);
                }
                userId.setText(phone + "\n" + textString);
                if (redPacketNum == 0) {
                    redPackageNum.setVisibility(View.GONE);
                } else {
                    redPackageNum.setVisibility(View.VISIBLE);
                    redPackageNum.setText(redPacketNum + "个");
                }
                nameReZheng2.setText(bankCardNum + "张");
                nameReZheng2.setVisibility(View.VISIBLE);
                if (nowState == 4) {
                    nameReZheng.setText("已认证");
                    nameReZheng.setVisibility(View.VISIBLE);
                    nameReZheng.setBackground(getResources().getDrawable(R.drawable.bg_un_ren_zheng));
                } else if (nowState == 3) {
                    nameReZheng.setText("未认证");
                    nameReZheng.setVisibility(View.VISIBLE);
                    nameReZheng.setBackground(getResources().getDrawable(R.drawable.bg_renzheng));
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Bus.getDefault().unregister(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (subscrip!=null) {
            subscrip.unsubscribe();
        }
    }
}