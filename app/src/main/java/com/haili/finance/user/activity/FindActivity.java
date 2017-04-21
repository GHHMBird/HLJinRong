package com.haili.finance.user.activity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haili.finance.PagerSlidingTab;
import com.haili.finance.R;
import com.haili.finance.base.BaseActivity;
import com.haili.finance.user.adapter.FindAdapter;
import com.haili.finance.user.fragment.ActiveFragment;
import com.haili.finance.user.fragment.NewsFragment;
import com.haili.finance.user.fragment.NoticeFragment;

import java.util.ArrayList;

/**
 *
 * Created by Monkey on 2017/2/28.
 */

public class FindActivity extends BaseActivity {

    private ArrayList<Fragment> fragments = new ArrayList<>();
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);
        setUpToolbar();
        getSupportActionBar().setTitle("");
        setBarTitle("发现");
        initView();
        init();
    }

    private void init() {
        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        if ("1".equals(type)) {
            viewPager.setCurrentItem(2);
        }
    }

    private void initView() {
        String[] title = getResources().getStringArray(R.array.find_title_array);
        ActiveFragment findFragment1 = new ActiveFragment();
        NewsFragment findFragment2 = new NewsFragment();
        NoticeFragment findFragment3 = new NoticeFragment();
        fragments.add(findFragment1);
        fragments.add(findFragment2);
        fragments.add(findFragment3);
        viewPager = (ViewPager) findViewById(R.id.find_activity_vp);
        PagerSlidingTab mSlidingTabLayout = (PagerSlidingTab) findViewById(R.id.find_activity_tab);
        FindAdapter findAdapter = new FindAdapter(getFragmentManager(),fragments,title);
        viewPager.setAdapter(findAdapter);
        mSlidingTabLayout.setViewPager(viewPager);//把viewPager配置给页签（顶部指示器）
    }

    class SamplePagerAdapter extends PagerAdapter {
        String[] title = getResources().getStringArray(R.array.find_title_array);
        @Override
        public int getCount() {
            return title.length;
        }
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return object == view;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return title[position];
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TextView textView = new TextView(FindActivity.this);
            textView.setText("aaaaaaaaaaaaaaaaaaaaaaaaaa"+position);
            return textView;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }
}
