package com.haili.finance.user.fragment;
/*
 * Created by hhm on 2017/3/24.
 */

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.haili.finance.R;
import com.haili.finance.business.user.LoginResponse;
import com.haili.finance.stroage.DataCache;
import com.haili.finance.user.activity.IDInformationActivity;
import com.haili.finance.user.activity.MyRedPacketActivity;
import com.mcxiaoke.bus.Bus;
import com.umeng.analytics.MobclickAgent;

public class RegisterRedPackageFragment extends Fragment implements View.OnClickListener {

    private static final String KEY_EVENT = "KEY";
    public static final String TAG = "RegisterRedPackageFragment";

    /**
     * 新建一个RegisterRedPackageFragment对象
     *
     * @param event RxBus广播的事件，字符串类型，不能为空，保证唯一。若为NULL或“”则不会发送事件广播
     * @return RegisterRedPackageFragment 对象
     */
    public static RegisterRedPackageFragment newInstance(@NonNull String event) {
        Bundle args = new Bundle();
        args.putString(KEY_EVENT, event);
        RegisterRedPackageFragment fragment = new RegisterRedPackageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_red_package, container, false);
        TextView textInfo = (TextView) view.findViewById(R.id.register_red_package_id_info);
        TextView textRedPackage = (TextView) view.findViewById(R.id.tv_register_red_package);
        LoginResponse cacheData = DataCache.instance.getCacheData("haili", "RegisterResponse");
        if (cacheData != null) {
            textRedPackage.setText(cacheData.loginModel.registerRedBagMoney);
        }
        ImageView ivClose = (ImageView) view.findViewById(R.id.red_package_fragment_close);
        Button btnRedPackage = (Button) view.findViewById(R.id.register_red_package_btn);
        btnRedPackage.setOnClickListener(this);
        textInfo.setOnClickListener(this);
        ivClose.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String event = getArguments().getString(KEY_EVENT, "");
        if (!TextUtils.isEmpty(event)) {
            Bus.getDefault().post(event);
        }
    }

    public void removeSelf(final FragmentManager fragmentManager) {
        if (fragmentManager == null) {
            return;
        }
        getFragmentManager().beginTransaction()
                .setCustomAnimations(0, 0)
                .remove(RegisterRedPackageFragment.this).commitAllowingStateLoss();
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(getActivity());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_red_package_id_info://实名认证
                startActivity(new Intent(getActivity(), IDInformationActivity.class));
                break;
            case R.id.register_red_package_btn://查看红包
                startActivity(new Intent(getActivity(), MyRedPacketActivity.class));
                MobclickAgent.onEvent(getActivity(), "Reg_veiwredpacket_action");
                break;
        }
        removeSelf(getFragmentManager());
    }
}
