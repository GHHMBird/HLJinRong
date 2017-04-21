package com.cheguan.lgdpulltorefresh;

import android.content.Context;
import android.widget.FrameLayout;

/**
 * Created by Sunday on 16/2/1.
 * 默认的刷新头样式
 */
public abstract class RefreshView extends FrameLayout {
    private Context mContext;

    public RefreshView(Context context) {
        super(context);
        this.mContext = context;
    }

    /**
     * 静止状态
     */
    public abstract void normal();

    /**
     * 用户正在下拉，但还没有到达刷新的位置
     * @param downRate 下拉与指定刷新位置的比例 最大为1（为1时已经入canRefresh方法中）  可根据此比例自定义刷新动画
     */
    public abstract void beginDown(float downRate);

    /**
     * 到达可以刷新的位置，但用户还没有松开手指
     */
    public abstract void canRefresh();

    /**
     * 正在刷新
     */
    public abstract void beginRefresh();

    /**
     * 刷新完成
     */
    public abstract void refreshComplete();

}
