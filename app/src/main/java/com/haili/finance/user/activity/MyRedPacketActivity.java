package com.haili.finance.user.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.haili.finance.R;
import com.haili.finance.base.BaseActivity;
import com.haili.finance.user.fragment.RedPacketCanUseFragment;
import com.haili.finance.user.fragment.RedPacketOutDateFragment;
import com.haili.finance.user.fragment.RedPacketUsedFragment;

import java.util.ArrayList;


/**
 * Created by Monkey on 2017/1/16.
 * 我的红包界面
 */

public class MyRedPacketActivity extends BaseActivity{

    private ArrayList<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_redbackpage);
        setUpToolbar();
        getSupportActionBar().setTitle("");
        setBarTitle("我的红包");
        ViewPager mViewPager = (ViewPager) findViewById(R.id.view_pager);
        initFragments();
        RedPacketViewPageAdapter adapter = new RedPacketViewPageAdapter(getFragmentManager(), fragments);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabTextColors(0x88ffffff, 0xffffffff);
        tabLayout.setupWithViewPager(mViewPager);
    }

    private void initFragments() {
        RedPacketCanUseFragment redPacketCanUseFragment = new RedPacketCanUseFragment();
        RedPacketUsedFragment redPacketUsedFragment = new RedPacketUsedFragment();
        RedPacketOutDateFragment redPacketOutDateFragment = new RedPacketOutDateFragment();
        fragments = new ArrayList<>();
        fragments.add(redPacketCanUseFragment);
        fragments.add(redPacketUsedFragment);
        fragments.add(redPacketOutDateFragment);
    }

    private class RedPacketViewPageAdapter extends FragmentPagerAdapter {

        private String[] mPagerTitleArray;
        private ArrayList<Fragment> fragments;

        RedPacketViewPageAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
            super(fm);
            mPagerTitleArray = getResources().getStringArray(R.array.packet_title_array);
            this.fragments = fragments;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mPagerTitleArray[position];
        }

        @Override
        public Fragment getItem(int pos) {
            return fragments.get(pos);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

    }
}
