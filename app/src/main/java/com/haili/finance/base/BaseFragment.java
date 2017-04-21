package com.haili.finance.base;

import android.app.Fragment;
import android.os.Bundle;

import com.haili.finance.fragment.LoadingFragment;
import com.haili.finance.helper.ViewHelper;
import com.squareup.leakcanary.RefWatcher;
import com.umeng.analytics.MobclickAgent;



/*
 * Created by Monkey on 2017/1/7.
 */

public class BaseFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBaseFragment();
    }

    private void initBaseFragment() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = BaseApplication.getRefWatcher(getActivity());
        refWatcher.watch(this);
        ViewHelper.toastStop();
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

    public void addLoadingFragment(int viewId,String event){
        LoadingFragment mLoadingFragment = LoadingFragment.newInstance(event);
        getFragmentManager()
                .beginTransaction()
                .replace(viewId, mLoadingFragment,LoadingFragment.TAG)
                .commitAllowingStateLoss();
    }

    public void removeLoadingFragment(){
        LoadingFragment mLoadingFragment = findLoadingFragment();
        if(mLoadingFragment != null){
            mLoadingFragment.removeSelf(getFragmentManager());
        }
    }


    public LoadingFragment findLoadingFragment(){
        Fragment fragment = getFragmentManager().findFragmentByTag(LoadingFragment.TAG);
        if(fragment != null){
            return (LoadingFragment)fragment;
        }
        return null;
    }

    public void showLoadingFailLayout(){
        LoadingFragment loadingFragment = findLoadingFragment();
        if (loadingFragment != null){
            loadingFragment.showLoadingFailView();
        }
    }

}
