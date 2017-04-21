package com.haili.finance.user.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 *
 * Created by Monkey on 2017/2/28.
 */

public class FindAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> al;
   private String[] title;

    public FindAdapter(FragmentManager fm, ArrayList<Fragment> al, String[] title) {
        super(fm);
        this.al = al;
       this.title = title;
    }

    @Override
    public Fragment getItem(int position) {
        return al.get(position);
    }

    @Override
    public int getCount() {
        return al.size();
    }

    //此方法用来显示tab上的名字
    @Override
    public CharSequence
    getPageTitle(int position) {
        return title[position];
    }


}
