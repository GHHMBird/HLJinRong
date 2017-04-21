package com.haili.finance.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.haili.finance.R;
import com.haili.finance.base.BaseActivity;
import com.haili.finance.business.user.BankInfo;
import com.haili.finance.business.user.BankListRequest;
import com.haili.finance.business.user.BankListResponse;
import com.haili.finance.fragment.LoadingFragment;
import com.haili.finance.helper.UserBusinessHelper;
import com.haili.finance.helper.ViewHelper;
import com.haili.finance.rx.RequestErrorThrowable;
import com.haili.finance.stroage.DataCache;
import com.haili.finance.user.adapter.BankListAdapter;
import com.mcxiaoke.bus.annotation.BusReceiver;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by Monkey on 2017/1/20.
 * 选择开户行
 */

public class SelectOpeanBankActivity extends BaseActivity {
    @Bind(R.id.listview)
    ListView listView;
    private ArrayList<BankInfo> bankDataList;
    private Subscription subscrip;
    private String bankNO = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_opeanbank);
        ButterKnife.bind(this);
        setUpToolbar();
        getSupportActionBar().setTitle("");
        setBarTitle("选择开户行");
        initView();
        init();
    }

    private void initView() {
        Intent intent = getIntent();
        bankNO = intent.getStringExtra("bankNO");
    }

    private void init() {
        BankListResponse cacheData = DataCache.instance.getCacheData("haili", "BankListResponse");
        if (cacheData != null) {
            bankDataList = cacheData.bankDataList;
            BankListAdapter bankListAdapter = new BankListAdapter(SelectOpeanBankActivity.this, bankDataList, bankNO);
            listView.setAdapter(bankListAdapter);
            initListener();
        } else {
            addLoadingFragment(R.id.select_openbank_ll, "SelectOpeanBankActivity");
        }
    }

    @BusReceiver
    public void StringEvent(String event) {
        if (event.equals("SelectOpeanBankActivity")) {
            initListView();
        }
    }

    private void initListView() {
        subscrip = UserBusinessHelper.bankOpenList(new BankListRequest())
                .subscribe(new Action1<BankListResponse>() {
                    @Override
                    public void call(BankListResponse bankListResponse) {

                        LoadingFragment loadingFragment = findLoadingFragment();
                        if (loadingFragment != null) {
                            removeLoadingFragment();
                        }

                        if (bankListResponse.bankDataList != null) {
                            DataCache.instance.saveCacheData("haili", "BankListResponse", bankListResponse);
                            bankDataList = bankListResponse.bankDataList;
                            BankListAdapter bankListAdapter = new BankListAdapter(SelectOpeanBankActivity.this, bankDataList, bankNO);
                            listView.setAdapter(bankListAdapter);
                            initListener();
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
                            ViewHelper.showToast(SelectOpeanBankActivity.this, requestErrorThrowable.getMessage());
                        }
                    }
                });
    }

    private void initListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("bank", bankDataList.get(position).backName);
                intent.putExtra("CBDNo", bankDataList.get(position).backCBDNO);
                setResult(67890, intent);
                finish();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (subscrip != null) {
            subscrip.unsubscribe();
        }
    }
}
