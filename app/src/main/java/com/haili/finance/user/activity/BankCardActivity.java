package com.haili.finance.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.haili.finance.R;
import com.haili.finance.base.BaseActivity;
import com.haili.finance.business.user.LoginResponse;
import com.haili.finance.business.user.MyBankCardModel;
import com.haili.finance.business.user.MyBankCardRequest;
import com.haili.finance.business.user.MyBankCardResponse;
import com.haili.finance.business.user.RegisterMsgCodeRequest;
import com.haili.finance.business.user.RegisterMsgCodeResponse;
import com.haili.finance.business.user.RemoveBankCardRequest;
import com.haili.finance.business.user.RemoveBankCardResponse;
import com.haili.finance.fragment.LoadingFragment;
import com.haili.finance.helper.UserBusinessHelper;
import com.haili.finance.helper.ViewHelper;
import com.haili.finance.rx.RequestErrorThrowable;
import com.haili.finance.stroage.DataCache;
import com.haili.finance.utils.StringUtils;
import com.haili.finance.utils.TimeCount;
import com.mcxiaoke.bus.Bus;
import com.mcxiaoke.bus.annotation.BusReceiver;
import com.umeng.analytics.MobclickAgent;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.functions.Action1;

/**
 * 银行卡管理界面
 * Created by Monkey on 2017/3/1.
 */

public class BankCardActivity extends BaseActivity implements TextWatcher {
    //主要的几个页面view，用来控制显示与隐藏(包括titleBar)
    @Bind(R.id.activity_rebank)
    LinearLayout reBankLL;//解绑卡页面view
    @Bind(R.id.activity_bank_info)
    LinearLayout bankLL;//信息展示页面view
    @Bind(R.id.activity_nobankcard)
    LinearLayout noBankLL;//去绑卡页面view
    @Bind(R.id.menu_layout)
    RelativeLayout menuLayout;
    @Bind(R.id.menu_text)
    TextView tvReBunding;

    //各个view页面的可变参数控件
    //信息展示页面view
    @Bind(R.id.activity_bank_bind_icon)
    ImageView bankBindIcon;
    @Bind(R.id.activity_bank_bind_type)
    TextView bankBindtype;
    @Bind(R.id.activity_bank_bind_card_num)
    TextView bankBindCardNum;
    @Bind(R.id.activity_bank_message)
    TextView bankBindCardMsgNews;
    @Bind(R.id.activity_bank_bind_card_message)
    TextView bankBindCardMsg;

    //解绑卡页面view
    @Bind(R.id.activity_reband_type)
    TextView bankRebindType;
    @Bind(R.id.activity_reband_icon)
    ImageView bankUnbindIcon;
    @Bind(R.id.activity_reband_card)
    TextView bankRebindCardNum;
    @Bind(R.id.activity_reband_rulename)
    TextView bankRebindCardName;
    @Bind(R.id.activity_reband_id_code)
    TextView bankRebindCardIdNum;

    //解绑卡界面的用户输入控件
    @Bind(R.id.activity_reband_phone)
    EditText etPhone;
    @Bind(R.id.activity_reband_put_code)
    EditText etMsgCode;
    @Bind(R.id.activity_reband_getcde)
    TextView etGetMsgCode;

    //去绑卡按钮
    @Bind(R.id.activity_reband_btn)
    Button goToReBindBtn;

    private TimeCount timeCount;
    private int type;
    private boolean isUnBundding = true;
    private MyBankCardModel dataModel;
    private Subscription subscrip, subscrip2, subscrip3;
    private String isCanRemoveBankCard;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank);
        ButterKnife.bind(this);
        setUpToolbar();
        getSupportActionBar().setTitle("");
        setBarTitle("银行卡管理");
        initView();
        init();
    }

    private void init() {
        initClick();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timeCount != null) {
            timeCount.cancel();
        }
        Bus.getDefault().post("REFRESH_MY_FRAGMENT");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 888 && resultCode == 666) {
            type = Integer.parseInt(data.getStringExtra("certificationState"));
            bindingLayoutShow();
            getUserBankInfo();
        }
    }

    private void initData() {
        if (type == 1 || type == 2 || type == 5) {//1未实名-2未绑卡-5解绑卡了
            noBankLL.setVisibility(View.VISIBLE);
            bankLL.setVisibility(View.GONE);
            reBankLL.setVisibility(View.GONE);
            menuLayout.setVisibility(View.GONE);
        } else if (type == 3 || type == 4) {
            addLoadingFragment(R.id.activity_bank_fl, "BankCardActivityInitData");
        }
    }

    private void initClick() {
        etPhone.addTextChangedListener(this);
        etMsgCode.addTextChangedListener(this);
    }

    private void initView() {
        Intent intent = getIntent();
        type = intent.getIntExtra("type", -1);
        if (type == -1) {
            finish();
        }
        initCacheData();
    }

    private void initCacheData() {
        if (type == 3 || type == 4) {
            MyBankCardResponse cacheData = DataCache.instance.getCacheData("haili", "MyBankCardResponse");
            if (cacheData == null) {
                return;
            }
            analyseData(cacheData);
        }
    }

    @BusReceiver
    public void StringEvent(String event) {
        switch (event) {
            case "BankCardActivityGoToUnBinding": //解绑界面提交解除绑定银行卡
                goToUnBindCard();
                break;
            case "BankCardActivityInitData":
            case "REFRESH_BANK_CARD":
                getUserBankInfo();
                break;
            case "BankCardActivityGoToUnBindingGETMSG":
                getMsgCode();
                break;
        }
    }

    @OnClick({R.id.menu_layout, R.id.bank_noband_card_btn, R.id.activity_reband_getcde, R.id.activity_reband_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.menu_layout://解绑
                if (isUnBundding) {
                    if ("1".equals(isCanRemoveBankCard)) {//可以解绑
                        ViewHelper.buildNoTitleTextDialog(BankCardActivity.this, "您确定要解绑此银行卡吗？", new ViewHelper.OnPositiveBtnClickedListener() {
                            @Override
                            public void onPositiveBtnClicked(MaterialDialog dialog) {
                                unBundingLayoutShow();
                            }
                        }).show();
                    } else if ("0".equals(isCanRemoveBankCard)) {//不可解绑
                        ViewHelper.showToast(BankCardActivity.this, "您有在投的理财产品,暂时无法解绑");
                    }
                } else {
                    bindingLayoutShow();
                }
                break;
            case R.id.bank_noband_card_btn:
                MobclickAgent.onEvent(BankCardActivity.this, "BandCard_gotoband_action");
                if (type == 1) {
                    startActivity(new Intent(BankCardActivity.this, IDInformationActivity.class));
                } else if (type == 2 || type == 5) {
                    Intent intent = new Intent(BankCardActivity.this, AddBankCardActivity.class);
                    startActivityForResult(intent, 888);
                }
                break;
            case R.id.activity_reband_getcde:
                if (checkValue(1)) {
                    addLoadingFragment(R.id.activity_bank_fl, "BankCardActivityGoToUnBindingGETMSG");
                }
                break;
            case R.id.activity_reband_btn:
                if (checkValue(2)) {
                    addLoadingFragment(R.id.activity_bank_fl, "BankCardActivityGoToUnBinding");
                }
                break;
        }
    }

    private void goToUnBindCard() {
        RemoveBankCardRequest request = new RemoveBankCardRequest();
        request.IDCard = dataModel.bankCardId;
        request.phone = etPhone.getText().toString();
        request.verifyCode = etMsgCode.getText().toString();
        subscrip3 = UserBusinessHelper.removeBankCard(request)
                .subscribe(new Action1<RemoveBankCardResponse>() {
                    @Override
                    public void call(RemoveBankCardResponse removeBankCardResponse) {

                        LoadingFragment loadingFragment = findLoadingFragment();
                        if (loadingFragment != null) {
                            removeLoadingFragment();
                        }

                        if ("000000".equals(removeBankCardResponse.errorCode)) {

                            LoginResponse cacheData = DataCache.instance.getCacheData("haili", "LoginResponse");
                            cacheData.loginModel.bankNum -= 1;
                            if ("4".equals(cacheData.loginModel.certificationState)) {
                                cacheData.loginModel.certificationState = "5";
                                type = 5;
                            } else if ("3".equals(cacheData.loginModel.certificationState)) {
                                cacheData.loginModel.certificationState = "2";
                                type = 2;
                            }
                            DataCache.instance.saveCacheData("haili", "LoginResponse", cacheData);
                            LoginResponse myinfo = DataCache.instance.getCacheData("haili", "MyInfoResponse");
                            myinfo.loginModel.bankNum -= 1;
                            if ("4".equals(myinfo.loginModel.certificationState)) {
                                myinfo.loginModel.certificationState = "5";
                            } else if ("3".equals(myinfo.loginModel.certificationState)) {
                                myinfo.loginModel.certificationState = "2";
                            }
                            DataCache.instance.saveCacheData("haili", "MyInfoResponse", myinfo);

                            noBankLL.setVisibility(View.VISIBLE);
                            bankLL.setVisibility(View.GONE);
                            reBankLL.setVisibility(View.GONE);
                            menuLayout.setVisibility(View.GONE);
                            setBarTitle("银行卡管理");
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
                            ViewHelper.showToast(BankCardActivity.this, requestErrorThrowable.getMessage());
                        }
                    }
                });
    }

    private void getMsgCode() {

        RegisterMsgCodeRequest request = new RegisterMsgCodeRequest();
        request.phone = etPhone.getText().toString();
        request.type = 5;
        subscrip = UserBusinessHelper.getMsgCode(request)
                .subscribe(new Action1<RegisterMsgCodeResponse>() {
                    @Override
                    public void call(RegisterMsgCodeResponse registerMsgCodeResponse) {

                        LoadingFragment loadingFragment = findLoadingFragment();
                        if (loadingFragment != null) {
                            removeLoadingFragment();
                        }

                        timeCount = new TimeCount(60000, 1000, etGetMsgCode, BankCardActivity.this, 1);
                        timeCount.start();

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
                            ViewHelper.showToast(BankCardActivity.this, requestErrorThrowable.getMessage());
                        }
                    }
                });

    }

    private boolean checkValue(int type) {

        if (etPhone.getText().toString().length() < 11 || !StringUtils.isPhone(etPhone.getText().toString())) {
            ViewHelper.showToast(this, "请输入正确的手机号码");
            return false;
        }
        if (type != 1) {
            if (etMsgCode.getText().toString().length() < 6) {
                ViewHelper.showToast(this, "请输入正确的验证码");
                return false;
            }
        }

        return true;
    }

    private void bindingLayoutShow() {
        setBarTitle("银行卡管理");
        tvReBunding.setText("解绑");
        etPhone.setText("");
        etMsgCode.setText("");
        if (timeCount != null) {
            timeCount.cancel();
        }
        bankLL.setVisibility(View.VISIBLE);
        noBankLL.setVisibility(View.GONE);
        reBankLL.setVisibility(View.GONE);
        isUnBundding = true;

        LoadingFragment loadingFragment = findLoadingFragment();
        if (loadingFragment != null) {
            removeLoadingFragment();
        }
    }

    private void unBundingLayoutShow() {
        setBarTitle("解绑银行卡");
        tvReBunding.setText("取消");
        bankLL.setVisibility(View.GONE);
        noBankLL.setVisibility(View.GONE);
        reBankLL.setVisibility(View.VISIBLE);
        etPhone.setText("");
        etMsgCode.setText("");
        isUnBundding = false;

        LoadingFragment loadingFragment = findLoadingFragment();
        if (loadingFragment != null) {
            removeLoadingFragment();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (etPhone.getText().toString().length() > 0 && etMsgCode.getText().toString().length() > 0) {
            goToReBindBtn.setBackground(getResources().getDrawable(R.drawable.bg_btn_home_pro));
            goToReBindBtn.setEnabled(true);
        } else {
            goToReBindBtn.setBackground(getResources().getDrawable(R.drawable.bg_btn_home_pro2));
            goToReBindBtn.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private void getUserBankInfo() {
        bankLL.setVisibility(View.VISIBLE);
        noBankLL.setVisibility(View.GONE);
        reBankLL.setVisibility(View.GONE);
        menuLayout.setVisibility(View.VISIBLE);

        subscrip2 = UserBusinessHelper.myBankCard(new MyBankCardRequest())
                .subscribe(new Action1<MyBankCardResponse>() {
                               @Override
                               public void call(MyBankCardResponse myBankCardResponse) {

                                   LoadingFragment loadingFragment = findLoadingFragment();
                                   if (loadingFragment != null) {
                                       removeLoadingFragment();
                                   }

                                   if (myBankCardResponse.bankList != null) {
                                       DataCache.instance.saveCacheData("haili", "MyBankCardResponse", myBankCardResponse);
                                       analyseData(myBankCardResponse);
                                   }

                               }
                           }, new Action1<Throwable>() {
                               @Override
                               public void call(Throwable throwable) {
                                   if (DataCache.instance.getCacheData("haili", "MyBankCardResponse") != null) {
                                       LoadingFragment loadingFragment = findLoadingFragment();
                                       if (loadingFragment != null) {
                                           removeLoadingFragment();
                                       }

                                       if (throwable instanceof RequestErrorThrowable) {
                                           RequestErrorThrowable requestErrorThrowable = (RequestErrorThrowable) throwable;
                                           ViewHelper.showToast(BankCardActivity.this, requestErrorThrowable.getMessage());
                                       }
                                   } else {
                                       LoadingFragment loadingFragment = findLoadingFragment();
                                       if (loadingFragment != null) {
                                           loadingFragment.showLoadingFailView();
                                       }
                                   }
                               }
                           }
                );
    }

    private void analyseData(MyBankCardResponse myBankCardResponse) {
        if (myBankCardResponse.bankList == null || myBankCardResponse.bankList.size() <= 0) {
            return;
        }
        LoginResponse cacheData = DataCache.instance.getCacheData("haili", "MyInfoResponse");
        dataModel = myBankCardResponse.bankList.get(0);
        //bankBindIcon bankUnbindIcon 银行卡icon --> loginModel.bankIcon
        Glide.with(this)
                .load(dataModel.bankIcon)
                .error(R.mipmap.img_loadings)
                .placeholder(R.mipmap.img_loadings)
                .into(bankUnbindIcon);
        Glide.with(this)
                .load(dataModel.bankIcon)
                .error(R.mipmap.img_loadings)
                .placeholder(R.mipmap.img_loadings)
                .into(bankBindIcon);
        if ("1".equals(dataModel.bankCardType)) {
            bankBindtype.setText("借记卡");
            bankRebindType.setText("借记卡");
        }
        bankBindCardNum.setText("尾号" + dataModel.bankCardNo.substring(dataModel.bankCardNo.length() - 4));
        String money = "单笔<font color=red>" + dataModel.singleLimit / 10000 + "</font>万,单日<font color=red>" + dataModel.dayLimit / 10000 + "</font>万,单月<font color=red>" + dataModel.monthLimit / 10000 + "</font>万";
        bankBindCardMsg.setText(Html.fromHtml(money));
        bankBindCardMsgNews.setText(dataModel.remark);
        bankRebindCardNum.setText(dataModel.bankCardNo.substring(0, 4) + " **** **** **** " + dataModel.bankCardNo.substring(dataModel.bankCardNo.length() - 3));//卡号
        String textString;
        if (cacheData.loginModel.ruleName.length() <= 2) {
            textString = cacheData.loginModel.ruleName.substring(0, 1) + "*";
        } else {
            textString = cacheData.loginModel.ruleName.substring(0, 1);
            for (int i = 0; i < cacheData.loginModel.ruleName.length() - 2; i++) {
                textString += "*";
            }
            textString += cacheData.loginModel.ruleName.substring(cacheData.loginModel.ruleName.length() - 1);
        }
        bankRebindCardName.setText(textString);//真实姓名
        bankRebindCardIdNum.setText(cacheData.loginModel.IDCard.substring(0, 3) + "****" + cacheData.loginModel.IDCard.substring(cacheData.loginModel.IDCard.length() - 1));//身份证号
        this.isCanRemoveBankCard = dataModel.isCanUnbundlingBankCard;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (subscrip != null) {
            subscrip.unsubscribe();
        }
        if (subscrip2 != null) {
            subscrip2.unsubscribe();
        }
        if (subscrip3 != null) {
            subscrip3.unsubscribe();
        }
    }
}
