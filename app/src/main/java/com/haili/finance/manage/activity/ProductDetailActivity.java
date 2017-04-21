package com.haili.finance.manage.activity;

import android.app.Fragment;
import android.app.Service;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.haili.finance.R;
import com.haili.finance.ShareUtils;
import com.haili.finance.adapter.FragmentAdapter;
import com.haili.finance.base.BaseActivity;
import com.haili.finance.business.manage.ManageDetailRequest;
import com.haili.finance.business.manage.ManageDetailResponse;
import com.haili.finance.business.manage.ManageListModel;
import com.haili.finance.business.user.LoginResponse;
import com.haili.finance.fragment.LoadingFragment;
import com.haili.finance.helper.ManageBusinessHelper;
import com.haili.finance.helper.ViewHelper;
import com.haili.finance.manage.fragment.ProductDetail_OneFragment;
import com.haili.finance.manage.fragment.ProductDetail_ThreeFragment;
import com.haili.finance.manage.fragment.ProductDetail_TwoFragment;
import com.haili.finance.rx.RequestErrorThrowable;
import com.haili.finance.stroage.DataCache;
import com.haili.finance.user.activity.AddBankCardActivity;
import com.haili.finance.user.activity.IDInformationActivity;
import com.haili.finance.user.activity.LoginActivity;
import com.haili.finance.user.activity.PayPasswordActivity;
import com.haili.finance.utils.SoftKeyBoardListener;
import com.haili.finance.utils.StringUtils;
import com.haili.finance.widget.PullUpToLoadMore;
import com.mcxiaoke.bus.Bus;
import com.mcxiaoke.bus.annotation.BusReceiver;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.media.UMImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import rx.Subscription;
import rx.functions.Action1;


/*
 * Created by Monkey on 2017/1/13.
 * 理财产品详情页
 */

public class ProductDetailActivity extends BaseActivity implements View.OnClickListener {
    private PullUpToLoadMore ptlm;
    private ArrayList<Fragment> mFragmentList;
    private FragmentAdapter mFragmentAdapter;
    private ViewPager vg;
    private Subscription subscribe;
    private LinearLayout ll_tab_one, ll_tab_two, ll_tab_three;
    private TextView tv_tab_one, tv_tab_two, tv_tab_three, tv_cancle, tv_comit, shareBtn, hint_text;
    private Button btn_jisuanqi, btn_product;
    private PopupWindow popupWindow;
    private ManageListModel model;
    private String id;
    private ManageDetailResponse response;
    private ImageView type_icon, red_packet_view;
    private TextView tv_rate1, tv_rate2, deadline, interest_start_time, startInvestorBalance_view, tv_progress, rental_view, pay_type, residue_balance;
    private TextView invest_title, invest_date, interests_title, interests_date, product_title, product_date, remittance_title, remittance_date;
    private ProgressBar progress;
    private int progressInt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        setUpToolbar();
        getSupportActionBar().setTitle("");
        setBarTitle("");
        initView();
        init();
        addLoadingFragment(R.id.loading_layout, "ProductDetailActivity");
    }

    private void initView() {
        shareBtn = (TextView) findViewById(R.id.share_btn);
        ptlm = (PullUpToLoadMore) findViewById(R.id.ptlm);
        vg = (ViewPager) findViewById(R.id.id_page_vp);
        ll_tab_one = (LinearLayout) findViewById(R.id.ll_tab_one);
        ll_tab_two = (LinearLayout) findViewById(R.id.ll_tab_two);
        ll_tab_three = (LinearLayout) findViewById(R.id.ll_tab_three);
        tv_tab_one = (TextView) findViewById(R.id.tv_tab_one);
        tv_tab_two = (TextView) findViewById(R.id.tv_tab_two);
        tv_tab_three = (TextView) findViewById(R.id.tv_tab_three);
        btn_jisuanqi = (Button) findViewById(R.id.btn_jisuanqi);
        btn_product = (Button) findViewById(R.id.btn_product);
        type_icon = (ImageView) findViewById(R.id.type_icon);

        //fu
        tv_rate1 = (TextView) findViewById(R.id.tv_rate1);
        tv_rate2 = (TextView) findViewById(R.id.tv_rate2);
        red_packet_view = (ImageView) findViewById(R.id.red_packet_view);
        deadline = (TextView) findViewById(R.id.deadline);
        interest_start_time = (TextView) findViewById(R.id.interest_start_time);
        startInvestorBalance_view = (TextView) findViewById(R.id.startInvestorBalance_view);
        progress = (ProgressBar) findViewById(R.id.progress);
        tv_progress = (TextView) findViewById(R.id.tv_progress);
        pay_type = (TextView) findViewById(R.id.pay_type);
        residue_balance = (TextView) findViewById(R.id.residue_balance);
        invest_title = (TextView) findViewById(R.id.invest_title);
        invest_date = (TextView) findViewById(R.id.invest_date);
        interests_title = (TextView) findViewById(R.id.interests_title);
        interests_date = (TextView) findViewById(R.id.interests_date);
        product_title = (TextView) findViewById(R.id.product_title);
        product_date = (TextView) findViewById(R.id.product_date);
        remittance_title = (TextView) findViewById(R.id.remittance_title);
        remittance_date = (TextView) findViewById(R.id.remittance_date);
        rental_view = (TextView) findViewById(R.id.rental_view);
        hint_text = (TextView) findViewById(R.id.hint_text);
    }

    private void setViewData() {
        setBarTitle(response.data.productInfoVo.productName);
        NumberFormat nt = NumberFormat.getPercentInstance();
        //设置百分数精确度2即保留两位小数
        nt.setMinimumFractionDigits(2);
        String yieldRate = nt.format(response.data.productInfoVo.yieldRate);
        if (response.data.productInfoVo.type.equals("2")) {
            hint_text.setVisibility(View.VISIBLE);
            type_icon.setVisibility(View.VISIBLE);
        }
        if (response.data.productInfoVo.isUseRedPacket.equals("1")) {
            red_packet_view.setVisibility(View.VISIBLE);
        }
        if (response.data.productInfoVo.yieldRateAdd > 0) {
            String yieldRateAdd = nt.format(response.data.productInfoVo.yieldRateAdd);
            tv_rate2.setText("%+" + yieldRateAdd);
        } else {
            tv_rate2.setText("%");
        }
        tv_rate1.setText(yieldRate.substring(0, yieldRate.length() - 1));
        deadline.setText(response.data.productInfoVo.limitTime + "天");
        interest_start_time.setText(response.data.productInfoVo.carryInterest);
        startInvestorBalance_view.setText(response.data.productInfoVo.startInvestorBanalce + "元");
        tv_progress.setText(nt.format(response.data.productInfoVo.investmentProgress));
//        progress.setProgress((int) (response.data.productInfoVo.investmentProgress * 100));
        String totalAmountOfFinancing = "融资" + response.data.productInfoVo.totalAmountOfFinancing;
        rental_view.setText(totalAmountOfFinancing);
        pay_type.setText(response.data.productInfoVo.repaymentMode);
        residue_balance.setText("剩余" + response.data.productInfoVo.remainingAmount);
        invest_title.setText(response.data.deadlineList.get(0).stateDescribe);
        invest_date.setText(response.data.deadlineList.get(0).time.substring(0, 10));
        interests_title.setText(response.data.deadlineList.get(1).stateDescribe);
        interests_date.setText(response.data.deadlineList.get(1).time.substring(0, 10));
        product_title.setText(response.data.deadlineList.get(2).stateDescribe);
        product_date.setText(response.data.deadlineList.get(2).time.substring(0, 10));
        remittance_title.setText(response.data.deadlineList.get(3).stateDescribe);
        remittance_date.setText(response.data.deadlineList.get(3).time.substring(0, 10));
        new Thread() {
            @Override
            public void run() {
                while (progressInt <= response.data.productInfoVo.investmentProgress * 100) {
                    //发送消息
                    progressInt++;
                    try {
                        Thread.sleep(45);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mHandler.sendEmptyMessage(11111);
                }
            }
        }.start();
        if (response.data.productInfoVo.productState.equals("10")) {
            btn_product.setText("立即投资");
            if (response.data.productInfoVo.isCanInvest.equals("1")) {
                btn_product.setClickable(true);
                btn_product.setBackground(getResources().getDrawable(R.drawable.bg_btn_home_pro));
            } else {
                btn_product.setClickable(false);
                btn_product.setBackground(getResources().getDrawable(R.drawable.bg_btn_home_pro2));
            }
            return;
        }
        if (response.data.productInfoVo.productState.equals("20")) {
            btn_product.setText("募集结束");
            btn_product.setClickable(false);
            btn_product.setBackground(getResources().getDrawable(R.drawable.bg_btn_home_pro2));
            return;
        }
        if (response.data.productInfoVo.productState.equals("30")) {
            btn_product.setText("计息中");
            btn_product.setClickable(false);
            btn_product.setBackground(getResources().getDrawable(R.drawable.bg_btn_home_pro2));
            return;
        }
        if (response.data.productInfoVo.productState.equals("40")) {
            btn_product.setText("还款中");
            btn_product.setClickable(false);
            btn_product.setBackground(getResources().getDrawable(R.drawable.bg_btn_home_pro2));
            return;
        }
        if (response.data.productInfoVo.productState.equals("50")) {
            btn_product.setText("已还款");
            btn_product.setClickable(false);
            btn_product.setBackground(getResources().getDrawable(R.drawable.bg_btn_home_pro2));
            return;
        }
    }

    @BusReceiver
    public void StringEvent(String event) {
        if (event.equals("ProductDetailActivity")) {
            getDetail();
        }
    }

    private void init() {
        initKeyBoardView();
        initListener();
        Intent intent = getIntent();
        Bundle bundle = getIntent().getExtras();
        model = (ManageListModel) bundle.getSerializable("ManageListModel");
        String jsonString = getIntent().getStringExtra("params");
        if (jsonString != null) {
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                jsonString = jsonObject.getString("productId");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            id = jsonString;
        }
        if (!StringUtils.isEmpty(intent.getStringExtra("productId"))) {
            id = intent.getStringExtra("productId");
        }
        if (model != null) {
            setBarTitle(model.productName);
            id = model.productId;
        }
        response = DataCache.instance.getCacheData("haili", "ManageDetailResponse" + id);
        if (response != null) {
            setViewData();
            initViewPage();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    private void showPopupComment() {
        if (response == null) {
            return;
        }
        View view = LayoutInflater.from(ProductDetailActivity.this).inflate(
                R.layout.comment_popupwindow, null);
        final EditText inputComment = (EditText) view
                .findViewById(R.id.edit_comment);
        final TextView earnings = (TextView) view.findViewById(R.id.earnings);
        TextView balance_text = (TextView) view.findViewById(R.id.balance_text);
        TextView recharge_btn = (TextView) view.findViewById(R.id.recharge_btn);
        popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        popupWindow.setFocusable(true);
        // 设置点击窗口外边窗口消失
        popupWindow.setOutsideTouchable(true);
//        popupWindow.setBackgroundDrawable(getResources().getDrawable(
//                R.drawable.popuwindow_white_bg));

        // 设置弹出窗体需要软键盘
        popupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        // 再设置模式，和Activity的一样，覆盖，调整大小。
        popupWindow
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        ColorDrawable cd = new ColorDrawable(0x000000);
        popupWindow.setBackgroundDrawable(cd);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.4f;
        getWindow().setAttributes(params);
        // 设置popWindow的显示和消失动画
        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        popupWindow.update();
        popupInputMethodWindow();
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            // 在dismiss中恢复透明度
            public void onDismiss() {
                WindowManager.LayoutParams params = getWindow().getAttributes();
                params.alpha = 1f;
                getWindow().setAttributes(params);
            }
        });
        LoginResponse loginResponse = DataCache.instance.getCacheData("haili", "MyInfoResponse");
        if (loginResponse != null) {
            balance_text.setText(loginResponse.loginModel.balance + "元");
        }
        tv_cancle = (TextView) view.findViewById(R.id.tv_cancle);
        tv_comit = (TextView) view.findViewById(R.id.tv_comit);
        if (response != null) {
            if (response.data.productInfoVo.productState.equals("10") && response.data.productInfoVo.isCanInvest.equals("1")) {
                tv_comit.setVisibility(View.VISIBLE);
            } else {
                tv_comit.setVisibility(View.GONE);
            }
        }
        tv_comit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkState()) {
                    if (inputComment.getText().toString().length() == 0 || Double.valueOf(inputComment.getText().toString()) == 0) {
                        ViewHelper.showToast(ProductDetailActivity.this, "请输入投资金额");
                        return;
                    }
                    if (response.data.productInfoVo.startInvestorBanalce > Double.valueOf(inputComment.getText().toString())) {
                        ViewHelper.showToast(ProductDetailActivity.this, "起投金额为" + response.data.productInfoVo.startInvestorBanalce + "元");
                        return;
                    }
                    if (response.data.productInfoVo.remainingAmount < Double.valueOf(inputComment.getText().toString())) {
                        ViewHelper.showToast(ProductDetailActivity.this, "最大投资金额为" + response.data.productInfoVo.remainingAmount + "元");
                        return;
                    }
                    if (popupWindow != null) {
                        popupWindow.dismiss();
                    }
                    Intent intent = new Intent(ProductDetailActivity.this, BuyConfirmActivity.class);
                    intent.putExtra("id", response.data.productInfoVo.productId);
                    intent.putExtra("name", response.data.productInfoVo.productName);
                    intent.putExtra("money", inputComment.getText().toString());
                    intent.putExtra("earning", earnings.getText().toString());
                    startActivity(intent);
                }
            }
        });
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
            }
        });
        recharge_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkState()) {
                    startActivity(new Intent(ProductDetailActivity.this, RechargeActivity.class));
                }

            }
        });
        inputComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (inputComment.getText().toString().length() == 0) {
                    earnings.setText("0.00");
                } else {
                    int a = Integer.parseInt(inputComment.getText().toString());
                    int b = Integer.parseInt(response.data.productInfoVo.limitTime);
                    double c = a * (response.data.productInfoVo.yieldRate + response.data.productInfoVo.yieldRateAdd) / 365 * b;
                    DecimalFormat df = new DecimalFormat("0.00");
                    earnings.setText(df.format(c) + "");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /*
     * show soft input
     */
    private void popupInputMethodWindow() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                InputMethodManager imm = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }.start();
        //
    }

    private void initKeyBoardView() {
        //注册软键盘的监听
        SoftKeyBoardListener.setListener(ProductDetailActivity.this,
                new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
                    @Override
                    public void keyBoardShow(int height) {
                        Log.i("text", "键盘显示 高度" + height);
                    }

                    @Override
                    public void keyBoardHide(int height) {
                        Log.i("text", "键盘隐藏 高度" + height);
                        if (popupWindow != null) {
                            popupWindow.dismiss();
                        }
                    }
                });
    }

    private void initViewPage() {
        mFragmentList = new ArrayList<>();
        ProductDetail_OneFragment productDetail_oneFragment = new ProductDetail_OneFragment(response.data.productDescribe);
        ProductDetail_TwoFragment productDetail_twoFragment = new ProductDetail_TwoFragment(response.data.productInfoVo.safefyUrl,response.data.productInfoVo.productId);
        ProductDetail_ThreeFragment productDetail_threeFragment = new ProductDetail_ThreeFragment(response.data.investList);
        mFragmentList.add(productDetail_oneFragment);
        mFragmentList.add(productDetail_twoFragment);
        mFragmentList.add(productDetail_threeFragment);
        mFragmentAdapter = new FragmentAdapter(getFragmentManager(), mFragmentList);
        vg.setAdapter(mFragmentAdapter);
        vg.setOffscreenPageLimit(3);
        vg.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    resetTextView();
                    tv_tab_one.setTextColor(getResources().getColor(R.color.white));
                    tv_tab_one.setBackgroundResource(R.drawable.btn_bg_getmsg);
                    return;
                }
                if (position == 1) {
                    resetTextView();
                    tv_tab_two.setTextColor(getResources().getColor(R.color.white));
                    tv_tab_two.setBackgroundResource(R.drawable.btn_bg_getmsg);
                    return;
                }
                if (position == 2) {
                    resetTextView();
                    tv_tab_three.setTextColor(getResources().getColor(R.color.white));
                    tv_tab_three.setBackgroundResource(R.drawable.btn_bg_getmsg);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });
        productDetail_oneFragment.setOnRefreshViewShow(new ProductDetail_OneFragment.OnRefreshViewShow() {
            @Override
            public void onViewShow() {
                ptlm.scrollToTop();
            }
        });
        productDetail_threeFragment.setOnRefreshViewShow(new ProductDetail_ThreeFragment.OnRefreshViewShow() {
            @Override
            public void onViewShow() {
                ptlm.scrollToTop();
            }
        });
        productDetail_twoFragment.setOnRefreshViewShow(new ProductDetail_TwoFragment.OnRefreshViewShow() {
            @Override
            public void onViewShow() {
                ptlm.scrollToTop();
            }
        });
    }

    private void resetTextView() {
        tv_tab_one.setTextColor(getResources().getColor(R.color.text_grey));
        tv_tab_one.setBackgroundResource(R.drawable.bg_grey_bo);
        tv_tab_two.setTextColor(getResources().getColor(R.color.text_grey));
        tv_tab_two.setBackgroundResource(R.drawable.bg_grey_bo);
        tv_tab_three.setTextColor(getResources().getColor(R.color.text_grey));
        tv_tab_three.setBackgroundResource(R.drawable.bg_grey_bo);
    }

    private void initListener() {
        ptlm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ptlm.scrollToTop();
            }
        });
        ll_tab_one.setOnClickListener(this);
        ll_tab_two.setOnClickListener(this);
        ll_tab_three.setOnClickListener(this);
        btn_jisuanqi.setOnClickListener(this);
        btn_product.setOnClickListener(this);
        shareBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share_btn://分享
                if (response != null) {
                    UMImage umImage = new UMImage(ProductDetailActivity.this, R.mipmap.logo_center);
                    umImage.setThumb(umImage);
                    String shareurl = response.data.productInfoVo.Shareurl + "?productId=" + response.data.productInfoVo.productId;
                    ShareUtils.share(ProductDetailActivity.this, response.data.productInfoVo.productName, umImage, shareurl, "");
                }
                break;
            case R.id.ll_tab_one:
                vg.setCurrentItem(0);
                resetTextView();
                tv_tab_one.setTextColor(getResources().getColor(R.color.white));
                tv_tab_one.setBackgroundResource(R.drawable.btn_bg_getmsg);
                break;
            case R.id.ll_tab_two:
                vg.setCurrentItem(1);
                resetTextView();
                tv_tab_two.setTextColor(getResources().getColor(R.color.white));
                tv_tab_two.setBackgroundResource(R.drawable.btn_bg_getmsg);
                break;
            case R.id.ll_tab_three:
                vg.setCurrentItem(2);
                resetTextView();
                tv_tab_three.setTextColor(getResources().getColor(R.color.white));
                tv_tab_three.setBackgroundResource(R.drawable.btn_bg_getmsg);
                break;
            case R.id.btn_jisuanqi:
                if (checkState()) {
                    showPopupComment();
                }
                break;
            case R.id.btn_product:
                if (checkState()) {
                    Intent intent = new Intent(ProductDetailActivity.this, BuyProductActivity.class);
                    if (response != null) {
                        intent.putExtra("model",response.data);
//                        intent.putExtra("yieldRate", response.data.productInfoVo.yieldRate + response.data.productInfoVo.yieldRateAdd);
//                        intent.putExtra("limitTime", response.data.productInfoVo.limitTime);
//                        intent.putExtra("id", response.data.productInfoVo.productId);
//                        intent.putExtra("name", response.data.productInfoVo.productName);
//                        intent.putExtra("min", response.data.productInfoVo.startInvestorBanalce);
//                        intent.putExtra("max", response.data.productInfoVo.remainingAmount);
                    }
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        if (popupWindow != null) {
            popupWindow.dismiss();
        }
    }

    private void getDetail() {
        ManageDetailRequest request = new ManageDetailRequest();
        request.productId = id;
        request.type = "1";
        subscribe = ManageBusinessHelper.getManageDetail(request).subscribe(new Action1<ManageDetailResponse>() {
            @Override
            public void call(ManageDetailResponse manageDetailResponse) {
                LoadingFragment loadingFragment = findLoadingFragment();
                if (loadingFragment != null) {
                    removeLoadingFragment();
                }
                DataCache.instance.saveCacheData("haili", "ManageDetailResponse" + manageDetailResponse.data.productInfoVo.productId, manageDetailResponse);
                response = manageDetailResponse;
                setViewData();
                initViewPage();
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                LoadingFragment loadingFragment = findLoadingFragment();
                if (DataCache.instance.getCacheData("haili", "ManageDetailResponse" + id) == null) {
                    if (loadingFragment != null) {
                        loadingFragment.showLoadingFailView();
                    }
                } else {
                    if (loadingFragment != null) {
                        removeLoadingFragment();
                    }
                    if (throwable instanceof RequestErrorThrowable) {
                        RequestErrorThrowable requestErrorThrowable = (RequestErrorThrowable) throwable;
                        ViewHelper.showToast(ProductDetailActivity.this, requestErrorThrowable.getMessage());
                    }
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Bus.getDefault().unregister(this);
    }

    private boolean checkState() {
        LoginResponse loginResponse = DataCache.instance.getCacheData("haili", "MyInfoResponse");
        if (loginResponse != null) {
            if (loginResponse.loginModel.certificationState.equals("1")) {
                startActivity(new Intent(ProductDetailActivity.this, IDInformationActivity.class));
                return false;
            }
            if (loginResponse.loginModel.certificationState.equals("2") || loginResponse.loginModel.certificationState.equals("5")) {
                startActivity(new Intent(ProductDetailActivity.this, AddBankCardActivity.class));
                return false;
            }
            if (loginResponse.loginModel.certificationState.equals("3")) {
                Intent intent = new Intent(ProductDetailActivity.this, PayPasswordActivity.class);
                intent.putExtra("title", "设置支付密码");
                startActivity(intent);
                return false;
            }
        } else {
            startActivity(new Intent(ProductDetailActivity.this, LoginActivity.class));
            return false;
        }
        return true;
    }

    //创建一个负责更新进度的hander
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            progress.setProgress(progressInt);
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        if (subscribe!=null) {
            subscribe.unsubscribe();
        }
    }
}
