package com.haili.finance.property.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.haili.finance.R;
import com.haili.finance.adapter.FragmentAdapter;
import com.haili.finance.base.BaseActivity;
import com.haili.finance.manage.fragment.InvestmentRecordFragment;
import com.haili.finance.property.fragment.PaymentHistoryFragment;

import java.util.ArrayList;

/*
 * Created by Monkey on 2017/1/19.
 * 全部交易记录界面
 */

public class AllTradeActivity extends BaseActivity {
    private ViewPager viewPager;
    private String manageMoneyId;
//    private TextView tv_tab_one, tv_tab_two;

    protected void onCreate(Bundle savedInstanceState) {
//        applyTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_trade);
        setUpToolbar();
        getSupportActionBar().setTitle("");
        Intent intent = getIntent();
        manageMoneyId = intent.getStringExtra("manageMoneyId");
        if (!TextUtils.isEmpty(manageMoneyId)) {
            setBarTitle("交易记录");
        } else {
            setBarTitle("全部交易");
        }
        initView();
    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.viewpage);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabTextColors(0x88ffffff, 0xffffffff);
        tabLayout.setupWithViewPager(viewPager);
        AllTradeViewPageAdapter adapter = new AllTradeViewPageAdapter(getFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
//        tv_tab_one = (TextView) findViewById(R.id.tv_tab_one);
//        tv_tab_two = (TextView) findViewById(R.id.tv_tab_two);
    }


//    private void initViewPager() {
//        ArrayList<Fragment> mFragmentList = new ArrayList<>();
//        InvestmentRecordFragment f1 = new InvestmentRecordFragment( manageMoneyId);//投资记录
//        PaymentHistoryFragment f2 = new PaymentHistoryFragment( manageMoneyId);//还款记录
//        mFragmentList.add(f1);
//        mFragmentList.add(f2);
//        FragmentAdapter mFragmentAdapter = new FragmentAdapter(getFragmentManager(), mFragmentList);
//        viewPager.setAdapter(mFragmentAdapter);
//        viewPager.setOffscreenPageLimit(3);
//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                switch (position) {
//                    case 0:
//                        tv_tab_one.setTextColor(Color.WHITE);
//                        tv_tab_one.setBackgroundResource(R.drawable.btn_bg_org_3);
//                        tv_tab_two.setTextColor(getResources().getColor(R.color.text_grey));
//                        tv_tab_two.setBackgroundResource(R.drawable.btn_bg_greybo_3);
//                        break;
//                    case 1:
//                        tv_tab_two.setTextColor(Color.WHITE);
//                        tv_tab_two.setBackgroundResource(R.drawable.btn_bg_org_3);
//                        tv_tab_one.setTextColor(getResources().getColor(R.color.text_grey));
//                        tv_tab_one.setBackgroundResource(R.drawable.btn_bg_greybo_3);
//                        break;
//
//                }
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
//    }

    private class AllTradeViewPageAdapter extends FragmentPagerAdapter {

        String[] mPagerTitleArray;

        public AllTradeViewPageAdapter(FragmentManager fm) {
            super(fm);
            mPagerTitleArray = getResources().getStringArray(
                    R.array.all_trade_title_array);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mPagerTitleArray[position];
        }

        @Override
        public Fragment getItem(int pos) {
            if (pos == 0) {
                InvestmentRecordFragment f1 = new InvestmentRecordFragment( manageMoneyId);//投资记录
                return f1;

            } else {
                PaymentHistoryFragment f2 = new PaymentHistoryFragment( manageMoneyId);//还款记录
                return f2;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
