package com.haili.finance.user.fragment;

import android.app.Fragment;
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
import com.haili.finance.helper.ViewHelper;

/**
 * Created by Monkey on 2017/3/1.
 */

public class ChangeAccountPwdFragment extends Fragment implements View.OnClickListener, TextWatcher {

    private EditText oldPassWord, newPassWord, newPassWord2;
    private Button change;
    private TextView forgetPassWord;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_account_password, container, false);
        oldPassWord = (EditText) view.findViewById(R.id.edit_old_phone);
        newPassWord = (EditText) view.findViewById(R.id.edit_new_password);
        newPassWord2 = (EditText) view.findViewById(R.id.edit_new_password_second);
        oldPassWord.addTextChangedListener(this);
        newPassWord.addTextChangedListener(this);
        newPassWord2.addTextChangedListener(this);
        forgetPassWord = (TextView) view.findViewById(R.id.change_login_forget_password);
        change = (Button) view.findViewById(R.id.btn_change);
        forgetPassWord.setOnClickListener(this);
        change.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.change_login_forget_password://忘记密码

                break;
            case R.id.btn_change:
                if (checkValue()) {

                }
                break;
        }
    }

    private boolean checkValue() {
        String oldpsd = oldPassWord.getText().toString();
        String newpsd1 = newPassWord.getText().toString();
        String newpsd2 = newPassWord2.getText().toString();
        if (isZZ(oldpsd) || oldpsd.length() < 6) {
            ViewHelper.showToast(getActivity(),"请输入正确的密码");
            return false;
        }
        if (isZZ(newpsd1) || newpsd1.length() < 6) {
            ViewHelper.showToast(getActivity(),"请输入正确的密码");
            return false;
        }
        if (!newpsd1.equals(newpsd2)) {
            ViewHelper.showToast(getActivity(),"两次密码输入不一致");
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
            change.setBackground(getResources().getDrawable(R.drawable.bg_btn_home_pro));
            change.setEnabled(true);
        } else {
            change.setBackground(getResources().getDrawable(R.drawable.bg_btn_home_pro2));
            change.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}
