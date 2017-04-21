package com.haili.finance.user.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.haili.finance.R;
import com.haili.finance.base.BaseFragment;
import com.haili.finance.business.user.ChangePhoneNumRequest;
import com.haili.finance.business.user.ChangePhoneNumResponse;
import com.haili.finance.business.user.RegisterMsgCodeRequest;
import com.haili.finance.business.user.RegisterMsgCodeResponse;
import com.haili.finance.fragment.LoadingFragment;
import com.haili.finance.helper.UserBusinessHelper;
import com.haili.finance.helper.ViewHelper;
import com.haili.finance.rx.RequestErrorThrowable;
import com.haili.finance.utils.StringUtils;
import com.haili.finance.utils.TimeCount;
import com.mcxiaoke.bus.Bus;
import com.mcxiaoke.bus.annotation.BusReceiver;

import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by lfu on 2017/2/22.
 */

public class ChangePhoneNumberFragment extends BaseFragment implements View.OnClickListener, TextWatcher {

    EditText editOldPhone, editIdCard, editNewPhoneNumber, editMsg;
    TextView getMsgBtn;
    Button changeBtn;
    private Subscription subscrip,subscrip2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bus.getDefault().register(this);
        View view = inflater.inflate(R.layout.fragment_change_phone_number, container, false);
        editOldPhone = (EditText) view.findViewById(R.id.edit_old_phone);
        editIdCard = (EditText) view.findViewById(R.id.edit_id_card);
        editNewPhoneNumber = (EditText) view.findViewById(R.id.edit_new_phone_number);
        editMsg = (EditText) view.findViewById(R.id.edit_msg);
        editOldPhone.addTextChangedListener(this);
        editIdCard.addTextChangedListener(this);
        editNewPhoneNumber.addTextChangedListener(this);
        editMsg.addTextChangedListener(this);
        getMsgBtn = (TextView) view.findViewById(R.id.tv_getmsg);
        changeBtn = (Button) view.findViewById(R.id.btn_change);
        getMsgBtn.setOnClickListener(this);
        changeBtn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_getmsg:
                if (getMsgCheck()) {
                    sendMsg();//发送验证码
                }
                break;
            case R.id.btn_change:
                if (checkValue()) {
                    addLoadingFragment(R.id.change_phone_number_fl, "ChangePhoneNumberFragment");
                }
                break;
            default:
                break;
        }
    }

    @BusReceiver
    public void StringEvent(String event) {
        if (event.equals("ChangePhoneNumberFragment")) {
            changePhoneNum();
        }
    }

    private void changePhoneNum() {
        ChangePhoneNumRequest request = new ChangePhoneNumRequest();
        request.idCard = editIdCard.getText().toString();
        request.phoneCode = editMsg.getText().toString();
        request.phone = editOldPhone.getText().toString();
        subscrip=  UserBusinessHelper.changePhoneNum(request)
                .subscribe(new Action1<ChangePhoneNumResponse>() {
                    @Override
                    public void call(ChangePhoneNumResponse changePhoneNumResponse) {

                        LoadingFragment loadingFragment = findLoadingFragment();
                        if (loadingFragment != null) {
                            removeLoadingFragment();
                        }

                        if (changePhoneNumResponse.dataModel != null) {
                            if ("1".equals(changePhoneNumResponse.dataModel.state)) {
                                Bus.getDefault().post("UserAccountSecurityActivityCloseFragment");
                                ViewHelper.showToast(getActivity(), "手机号修改完成");
                            } else {

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
                           if(getActivity()!=null){ViewHelper.showToast(getActivity(), requestErrorThrowable.getMessage());}
                        }
                    }
                });
    }

    private void sendMsg() {
        TimeCount timeCount = new TimeCount(60000, 1000, getMsgBtn, getActivity(), 1);
        timeCount.start();

        RegisterMsgCodeRequest request = new RegisterMsgCodeRequest();
        request.phone = editNewPhoneNumber.getText().toString();
        request.type = 4;
        subscrip2=  UserBusinessHelper.getMsgCode(request)
                .subscribe(new Action1<RegisterMsgCodeResponse>() {
                    @Override
                    public void call(RegisterMsgCodeResponse registerMsgCodeResponse) {
                        //todo 获取短信验证码
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (throwable instanceof RequestErrorThrowable) {
                            RequestErrorThrowable requestErrorThrowable = (RequestErrorThrowable) throwable;
                           if(getActivity()!=null){ViewHelper.showToast(getActivity(), requestErrorThrowable.getMessage());}
                        }
                    }
                });


    }

    private boolean checkValue() {
        if (editOldPhone.getText().toString().length() < 11 || !StringUtils.isPhone(editOldPhone.getText().toString())) {
            ViewHelper.showToast(getActivity(),"请输入正确的手机号码");
            return false;
        }
        if (editIdCard.getText().toString().length() < 18) {
            ViewHelper.showToast(getActivity(),"请输入正确的身份证号码");
            return false;
        }
        if (editNewPhoneNumber.getText().toString().length() < 11) {
            ViewHelper.showToast(getActivity(),"请输入正确的手机号码");
            return false;
        }
        if (editMsg.getText().toString().length() < 6) {
            ViewHelper.showToast(getActivity(),"请输入正确的验证码");
            return false;
        }
        return true;
    }

    public boolean getMsgCheck() {
        if (editNewPhoneNumber.getText().toString().length() < 11 && !StringUtils.isPhone(editNewPhoneNumber.getText().toString())) {
            ViewHelper.showToast(getActivity(),"请输入正确的手机号码");
            return false;
        }
        return true;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (editOldPhone.getText().toString().length() > 0 && editIdCard.getText().toString().length() > 0 && editNewPhoneNumber.getText().toString().length() > 0 && editMsg.getText().toString().length() > 0) {
            changeBtn.setBackground(getResources().getDrawable(R.drawable.bg_btn_home_pro));
            changeBtn.setEnabled(true);
        } else {
            changeBtn.setBackground(getResources().getDrawable(R.drawable.bg_btn_home_pro2));
            changeBtn.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

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
        }  if (subscrip2!=null) {
            subscrip2.unsubscribe();
        }
    }

    public void removeSelf() {

        getFragmentManager().beginTransaction()
                .setCustomAnimations(0, R.animator.slide_out_right)
                .remove(ChangePhoneNumberFragment.this).commitAllowingStateLoss();
    }
}
