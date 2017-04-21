package com.haili.finance.user.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.haili.finance.R;
import com.haili.finance.base.BaseFragment;
import com.haili.finance.business.user.ChangeLoginPsdRequest;
import com.haili.finance.business.user.ChangeLoginPsdResponse;
import com.haili.finance.fragment.LoadingFragment;
import com.haili.finance.helper.UserBusinessHelper;
import com.haili.finance.helper.ViewHelper;
import com.haili.finance.rx.RequestErrorThrowable;
import com.haili.finance.stroage.DataCache;
import com.haili.finance.user.activity.FindLoginPassword;
import com.haili.finance.utils.StringUtils;
import com.mcxiaoke.bus.Bus;
import com.mcxiaoke.bus.annotation.BusReceiver;

import rx.Subscription;
import rx.functions.Action1;

/*
 * Created by lfu on 2017/2/22.
 */

public class UserChangeLoginPasswordFragment extends BaseFragment implements View.OnClickListener, TextWatcher {
    private Subscription subscrip;
    private EditText oldPassWord, newPassWord, newPassWord2;
    private Button change;

    public UserChangeLoginPasswordFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bus.getDefault().register(this);
        View view = inflater.inflate(R.layout.fragment_change_login_password, container, false);
        oldPassWord = (EditText) view.findViewById(R.id.edit_old_phone);
        newPassWord = (EditText) view.findViewById(R.id.edit_new_password);
        newPassWord2 = (EditText) view.findViewById(R.id.edit_new_password_second);
        change = (Button) view.findViewById(R.id.btn_change);
        change.setOnClickListener(this);
        oldPassWord.addTextChangedListener(this);
        newPassWord.addTextChangedListener(this);
        newPassWord2.addTextChangedListener(this);
        TextView forgetPassWord = (TextView) view.findViewById(R.id.change_login_forget_password);
        forgetPassWord.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        change = null;
        oldPassWord = null;
        newPassWord = null;
        newPassWord2 = null;
        Bus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.change_login_forget_password://忘记密码
                startActivityForResult(new Intent(getActivity(), FindLoginPassword.class), 12305);
                break;
            case R.id.btn_change:
                if (checkValue()) {
                    addLoadingFragment(R.id.loading_layout, "UserChangeLoginPasswordFragment");
                    if (getActivity() != null && getActivity().isFinishing()) {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(newPassWord.getWindowToken(), 0);
                    }
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 12305 && resultCode == 12306) {
            boolean result = data.getBooleanExtra("result", false);
            if (result) {
                DataCache.instance.clearCacheData("haili", "LoginResponse");
                DataCache.instance.clearCacheData("haili", "MyInfoResponse");
                getActivity().finish();
                Bus.getDefault().post("unLogin");
            }
        }
    }

    @BusReceiver
    public void StringEvent(String event) {
        if (event.equals("UserChangeLoginPasswordFragment")) {
            changeLoginPsd();
        }
    }

    public void changeLoginPsd() {
        ChangeLoginPsdRequest request = new ChangeLoginPsdRequest();
        request.newPwd = newPassWord.getText().toString();
        request.oldPwd = oldPassWord.getText().toString();
        subscrip = UserBusinessHelper.changeLoginPsd(request)
                .subscribe(new Action1<ChangeLoginPsdResponse>() {
                    @Override
                    public void call(ChangeLoginPsdResponse changeLoginPsdResponse) {

                        LoadingFragment loadingFragment = findLoadingFragment();
                        if (loadingFragment != null) {
                            removeLoadingFragment();
                        }

                        if (changeLoginPsdResponse.dataModel != null) {
                            if ("0".equals(changeLoginPsdResponse.dataModel.updateState)) {
                                ViewHelper.showToast(getActivity(), "更改登录密码成功");
                                DataCache.instance.clearCacheData("haili", "LoginResponse");
                                DataCache.instance.clearCacheData("haili", "MyInfoResponse");
                                getActivity().finish();
                                Bus.getDefault().post("unLogin");
                            }
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

    private boolean checkValue() {
        String oldpsd = oldPassWord.getText().toString();
        String newpsd1 = newPassWord.getText().toString();
        String newpsd2 = newPassWord2.getText().toString();
        if (isZZ(oldpsd) || oldpsd.length() < 6) {
            ViewHelper.showToast(getActivity(), "请输入正确的密码");
            return false;
        }
        if (newpsd1.length() < 6 || StringUtils.isZZ(newpsd1)) {
            ViewHelper.showToast(getActivity(), "请输入正确的密码，包含大小写和数字，长度为6-16位");
            return false;
        }
        if (!newpsd1.equals(newpsd2)) {
            ViewHelper.showToast(getActivity(), "两次密码输入不一致");
            return false;
        }
        return true;
    }

    private boolean isZZ(String psd) {//判断是否是包含大写或小写字母和数字的正则表达式
        String regex = ".*[a-zA-Z].*[0-9]|.*[0-9].*[a-zA-Z]";
        return !psd.matches(regex);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (oldPassWord.getText().toString().length() > 0 && newPassWord.getText().toString().length() > 0 && newPassWord2.getText().toString().length() > 0) {
            change.setEnabled(true);
            change.setBackground(getResources().getDrawable(R.drawable.bg_btn_home_pro));
        } else {
            change.setEnabled(false);
            change.setBackground(getResources().getDrawable(R.drawable.bg_btn_home_pro2));
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onStop() {
        super.onStop();
        if (subscrip != null) {
            subscrip.unsubscribe();
        }
    }

    public void removeSelf(){
        oldPassWord.setText("");
        newPassWord.setText("");
        newPassWord2.setText("");
        getFragmentManager().beginTransaction()
                .setCustomAnimations(0, R.animator.slide_out_right)
                .remove(UserChangeLoginPasswordFragment.this).commit();
    }

}
