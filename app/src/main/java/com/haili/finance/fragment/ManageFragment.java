package com.haili.finance.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.haili.finance.R;
import com.haili.finance.base.BaseFragment;
import com.umeng.analytics.MobclickAgent;

/*
 * Created by fuliang on 2017/3/1.
 */

public class ManageFragment extends BaseFragment {

    private ViewPager mViewPager;
    private View placeView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage, container, false);
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
        ManageViewPageAdapter adapter = new ManageViewPageAdapter(getActivity().getFragmentManager());
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(4);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabTextColors(0x88ffffff, 0xffffffff);
        tabLayout.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(0);
        placeView = view.findViewById(R.id.place_holder_view);
        placeViewControl();
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    MobclickAgent.onEvent(getActivity(),"List_regular_action");
                } else {
                    MobclickAgent.onEvent(getActivity(),"List_new_action");
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    private void placeViewControl() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
            getActivity().getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            placeView.setVisibility(View.VISIBLE);
        } else {
            placeView.setVisibility(View.GONE);
        }
    }

    private class ManageViewPageAdapter extends FragmentPagerAdapter {

        String[] mPagerTitleArray;

        public ManageViewPageAdapter(FragmentManager fm) {
            super(fm);
            mPagerTitleArray = getResources().getStringArray(
                    R.array.manage_title_array);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mPagerTitleArray[position];
        }

        @Override
        public Fragment getItem(int pos) {
            if (pos == 0) {
                return new TerminalManageFragment();
            } else {
                return new GreenHandsManageFragment();
            }
        }

        @Override
        public int getCount() {
            return mPagerTitleArray.length;
        }
    }


}
