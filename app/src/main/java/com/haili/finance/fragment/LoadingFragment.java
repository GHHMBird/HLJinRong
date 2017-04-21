package com.haili.finance.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.haili.finance.R;
import com.mcxiaoke.bus.Bus;

/*
 *
 * Created by lfu on 2017/2/24.
 */

public class LoadingFragment extends Fragment {

    public static final String TAG = "LoadingFragment";

    private static final String KEY_EVENT = "EVENT";

    private String event;

    private ImageView imageView, loadingFailImage;

    private RelativeLayout loadingFailLayout;

    /*
     * 新建一个LoadingFragment对象
     *
     * @param event RxBus广播的事件，字符串类型，不能为空，保证唯一。若为NULL或“”则不会发送事件广播
     * @return LoadingFragment 对象
     */
    public static LoadingFragment newInstance(@NonNull String event) {

        Bundle args = new Bundle();
        args.putString(KEY_EVENT, event);
        LoadingFragment fragment = new LoadingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loading, container, false);
        imageView = (ImageView) view.findViewById(R.id.loading_view);
        loadingFailLayout = (RelativeLayout) view.findViewById(R.id.loading_fail_layout);
        loadingFailImage = (ImageView) view.findViewById(R.id.loading_fail_image);
        loadingFailImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(event)) {
                    Bus.getDefault().post(event);
                }
                removeLoadingFailView();
                AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getDrawable();
                animationDrawable.start();
            }
        });
        AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getDrawable();
        animationDrawable.start();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String event = getArguments().getString(KEY_EVENT, "");
        if (!TextUtils.isEmpty(event)) {
            this.event = event;
            Bus.getDefault().post(event);
        }
    }

    public void stopAnimation() {
        AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getDrawable();
        if (animationDrawable != null) {
            animationDrawable.stop();
        }
    }

    public void removeSelf(final FragmentManager fragmentManager) {
        if (fragmentManager == null) {
            return;
        }
        stopAnimation();
        getFragmentManager().beginTransaction()
                .setCustomAnimations(0, 0)
                .remove(LoadingFragment.this).commitAllowingStateLoss();
    }

    public void showLoadingFailView() {
        stopAnimation();
        loadingFailLayout.setVisibility(View.VISIBLE);
    }

    private void removeLoadingFailView() {
        if (loadingFailLayout.getVisibility() == View.VISIBLE) {
            loadingFailLayout.setVisibility(View.GONE);
        }
    }
}
