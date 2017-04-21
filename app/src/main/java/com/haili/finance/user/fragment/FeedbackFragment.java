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
import android.widget.ScrollView;

import com.haili.finance.R;
import com.haili.finance.base.BaseFragment;
import com.haili.finance.business.user.UserSendMsgBackReoponse;
import com.haili.finance.business.user.UserSendMsgBackRequest;
import com.haili.finance.fragment.LoadingFragment;
import com.haili.finance.helper.UserBusinessHelper;
import com.haili.finance.helper.ViewHelper;
import com.haili.finance.rx.RequestErrorThrowable;
import com.mcxiaoke.bus.Bus;
import com.mcxiaoke.bus.annotation.BusReceiver;

import rx.Subscription;
import rx.functions.Action1;

import static com.haili.finance.R.id.fragment_feedback_fl;

/**
 * Created by lfu on 2017/2/21.
 */

public class FeedbackFragment extends BaseFragment implements View.OnClickListener {

    private EditText editFeedback;
    private Button submitBtn;
    private Subscription subscrip;
    private ScrollView scrollView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bus.getDefault().register(this);
        View view = inflater.inflate(R.layout.fragment_feedback, container, false);
        editFeedback = (EditText) view.findViewById(R.id.edit_feedback);
        submitBtn = (Button) view.findViewById(R.id.btn_submit);
        scrollView = (ScrollView) view.findViewById(R.id.fragment_feedback_scroll);
        submitBtn.setOnClickListener(this);
        editTextListener();
        return view;
    }

    //监听输入框的变化，改变提交按钮的状态
    private void editTextListener() {
        editFeedback.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (editFeedback.getText().toString().length() > 0) {
                    submitBtn.setBackground(getResources().getDrawable(R.drawable.bg_btn_home_pro));
                    submitBtn.setEnabled(true);
                } else {
                    submitBtn.setBackground(getResources().getDrawable(R.drawable.bg_btn_home_pro2));
                    submitBtn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @BusReceiver
    public void StringEvent(String event) {
        if (event.equals("FeedbackFragment")) {
            commit();
        }
    }

    private void commit() {
        String text = editFeedback.getText().toString();
        UserSendMsgBackRequest request = new UserSendMsgBackRequest();
        request.content = text;
        subscrip=  UserBusinessHelper.userMessageBack(request)
                .subscribe(new Action1<UserSendMsgBackReoponse>() {
                    @Override
                    public void call(UserSendMsgBackReoponse userSendMsgBackReoponse) {

                        if (getActivity() != null) {
                            LoadingFragment loadingFragment = findLoadingFragment();
                            if (loadingFragment != null) {
                                removeLoadingFragment();
                            }

                            if (userSendMsgBackReoponse.dataModel != null) {
                                if ("0".equals(userSendMsgBackReoponse.dataModel.feedBackState)) {
                                    ViewHelper.showToast(getActivity(), "反馈成功");
                                } else if ("1".equals(userSendMsgBackReoponse.dataModel.feedBackState)) {
                                    ViewHelper.showToast(getActivity(), "反馈失败");
                                }
                            }
                        }

                        Bus.getDefault().post("feedBackIsFinish");

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                        if (getActivity() != null) {
                            LoadingFragment loadingFragment = findLoadingFragment();
                            if (loadingFragment != null) {
                                removeLoadingFragment();
                            }
                            if (throwable instanceof RequestErrorThrowable) {
                                RequestErrorThrowable requestErrorThrowable = (RequestErrorThrowable) throwable;
                                ViewHelper.showToast(getActivity(), requestErrorThrowable.getMessage());
                            }
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_submit) {
            addLoadingFragment(fragment_feedback_fl, "FeedbackFragment");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        scrollView = null;
        editFeedback = null;
        submitBtn = null;
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
