package com.cheguan.lgdpulltorefresh;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.Transformation;
import android.widget.AbsListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

public class PullToRefreshView extends ViewGroup {

    private static final int DRAG_MAX_DISTANCE = 120;
    private static final int PULL_VIEW_HEIGHT = 50;
    private static final int REFRESH_HEIGHT = 60;
    private static final int MY_HEIGHT = 36;
    private static final float DRAG_RATE = .5f;
    private static final float DECELERATE_INTERPOLATION_FACTOR = 2f;

    public static final int MAX_OFFSET_ANIMATION_DURATION = 700;
    //返回动画时间
    public static final int RESTORE_ANIMATION_DURATION = 650;

    private static final int INVALID_POINTER = -1;

    //静止状态
    private static final int NORMAL = 11;
    //用户正在下拉，但还没有到达刷新的位置
    private static final int BEGIN_DOWN = 12;
    //到达可以刷新的位置，但用户还没有松开手指
    private static final int CAN_REFRESH = 13;
    //正在刷新
    private static final int BEGIN_REFRESH = 14;
    //刷新完成
    private static final int REFRESH_COMPLATE = 15;

    //下拉动画类别  0 正常 1 产品详情页拉下回顶
    private int REFRESH_TYPE = 0;


    //当前刷新控件的状态
    private int mRefreshState;

    private Context mContext;
    private int mRefreshHeight;
    private int mPullViewHeight;
    //实体被刷新的数据内容区
    private View mTarget;
    //RefreshView实例
    private RelativeLayout mRefreshViewGroup;
    private RefreshView mRefreshView;
    private PullView mPullView;
    private boolean isShowPullView;
    //控制动画变化的类
    private Interpolator mDecelerateInterpolator;
    //可识别的最小滑动距离
    private int mTouchSlop;
    //刷新距离判断
    private int mRefreshingDistance;
    private float mCurrentDragPercent;
    //用户主动拖拽 数据视图距离顶部的高度，也就是 RefreshView当前的高度
    private int mCurrentOffsetTop;
    private boolean mRefreshing;
    private boolean mLoadMore;
    private int mActivePointerId;
    private boolean mIsBeingDragged;
    private float mInitialMotionY;
    //用户手指离开时，数据视图距离顶部的高度
    private int mFrom;
    //尼阻值
    private float mFromDragPercent;
    private boolean mNotify;
    private OnRefreshListener mListener;

    public boolean isShowPullView() {
        return isShowPullView;
    }

    public void setShowPullView(boolean showPullView) {
        isShowPullView = showPullView;
    }

    public int getTotalDragDistance() {
        return mRefreshingDistance;
    }

    public void setRefreshView(RefreshView mRefreshView) {
        if (mRefreshView == null){
            return;
        }
        this.mRefreshView = mRefreshView;
        initRefreshView();
    }

    public PullToRefreshView(Context context,int type) {
        this(context, null);
    }

    public PullToRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PullToRefreshView);
        if (typedArray != null){
            REFRESH_TYPE = typedArray.getInt(R.styleable.PullToRefreshView_type,0);
            typedArray.recycle();
        }
         init();
    }

    private void init(){
        mDecelerateInterpolator = new DecelerateInterpolator(DECELERATE_INTERPOLATION_FACTOR);
        mTouchSlop = ViewConfiguration.get(mContext).getScaledTouchSlop();
        //获取屏幕像素密度
        float density = mContext.getResources().getDisplayMetrics().density;
        mRefreshingDistance = Math.round((float) DRAG_MAX_DISTANCE * density);
        if (REFRESH_TYPE == 0){
            mRefreshHeight = Math.round((float)REFRESH_HEIGHT * density);
        }else {
            mRefreshHeight = Math.round((float)MY_HEIGHT * density);
        }
        mPullViewHeight = Math.round((float)PULL_VIEW_HEIGHT * density);

        initRefreshViewGroup();
        initPullView();
        //解决onDraw方法不被执行
        setWillNotDraw(false);
        //改变子视图的绘制顺序
        ViewCompat.setChildrenDrawingOrderEnabled(this, true);
    }

    private void initRefreshViewGroup(){
        mRefreshViewGroup = new RelativeLayout(mContext);
        mRefreshViewGroup.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL);
        initRefreshView();
        addView(mRefreshViewGroup);
    }

    private void initRefreshView(){
        if (mRefreshViewGroup.getChildCount() == 0){
            if (REFRESH_TYPE == 0){
                mRefreshView  = new DefaultRefeshView(mContext);
            }else {
                mRefreshView = new ProductDetailRefesh(mContext);
            }

        }else{
            mRefreshViewGroup.removeAllViews();
        }

        mRefreshView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,mRefreshHeight));
        mRefreshView.normal();
        mRefreshState = NORMAL;
        mRefreshViewGroup.addView(mRefreshView);
    }

    private void initPullView(){
        mPullView = new PullView(mContext);
        isShowPullView = true;
        addView(mPullView);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        ensureTarget();
        if (mTarget == null)
            return;
        //支持padding的设置，计算控件的大小
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth() - getPaddingRight() - getPaddingLeft(), MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), MeasureSpec.EXACTLY);

        mTarget.measure(widthMeasureSpec, heightMeasureSpec);
        mRefreshViewGroup.measure(widthMeasureSpec, heightMeasureSpec);
        mPullView.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(mPullViewHeight, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        ensureTarget();
        if (mTarget == null)
            return;

        int height = getMeasuredHeight();
        int width = getMeasuredWidth();
        int left = getPaddingLeft();
        int top = getPaddingTop();
        int right = getPaddingRight();
        int bottom = getPaddingBottom();

//        mTarget.layout(left, top + mCurrentOffsetTop, left + width - right, top + height - bottom + mCurrentOffsetTop - mPullViewHeight);
        mTarget.layout(left, top + mCurrentOffsetTop, left + width - right, top + height - bottom + mCurrentOffsetTop );
        mRefreshViewGroup.layout(left, top, left + width - right, top + height - bottom);
//        mPullView.layout(left,top + height - bottom + mCurrentOffsetTop - mPullViewHeight,left + width - right,top + height - bottom + mCurrentOffsetTop);
        mPullView.layout(left,top + height - bottom + mCurrentOffsetTop  - mPullViewHeight,left + width - right,top + height - bottom + mCurrentOffsetTop);
    }

    private void ensureTarget() {
        if (mTarget != null)
            return;
        if (getChildCount() > 0) {
            //PullToRefreshView 只可包含一个被刷新的控件  一般为ListView  GrldView  RecyclerView
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (child != mRefreshView && child != mPullView){
                    mTarget = child;
                    if (mTarget.getBackground() == null){
                        mTarget.setBackgroundColor(Color.WHITE);
                    }
                }

            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull MotionEvent ev) {
        if (!isEnabled() || (!isChildScrollToTop() && !isChildScrollToBottom()) || mRefreshing) {
            return false;
        }

        final int action = MotionEventCompat.getActionMasked(ev);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                setTargetOffsetTop(0, true);
                mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                mIsBeingDragged = false;
                final float initialMotionY = getMotionEventY(ev, mActivePointerId);
                if (initialMotionY == -1) {
                    return false;
                }
                mInitialMotionY = initialMotionY;
                break;
            case MotionEvent.ACTION_MOVE:

                if (isChildScrollToBottom()){
                    Log.d("TAG","isChildScrollToBottom");
                }

                if (mActivePointerId == INVALID_POINTER) {
                    return false;
                }
                final float y = getMotionEventY(ev, mActivePointerId);

                if (y == -1) {
                    return false;
                }
                final float yDiff = y - mInitialMotionY;
                if (yDiff > mTouchSlop && !mIsBeingDragged && isChildScrollToTop()) {
                    mIsBeingDragged = true;
                }

//                if (!mLoadMore){
//                    if (isChildScrollToBottom()){
//                        mTarget.offsetTopAndBottom(-mPullViewHeight);
//                        mLoadMore = true;
//                    }
//                }


                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mIsBeingDragged = false;
                mActivePointerId = INVALID_POINTER;
                break;
            case MotionEventCompat.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                break;
        }

        return mIsBeingDragged;
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent ev) {
        if (!mIsBeingDragged) {
            return super.onTouchEvent(ev);
        }

        final int action = MotionEventCompat.getActionMasked(ev);

        switch (action) {
            case MotionEvent.ACTION_MOVE: {
                final int pointerIndex = MotionEventCompat.findPointerIndex(ev, mActivePointerId);
                if (pointerIndex < 0) {
                    return false;
                }

                final float y = MotionEventCompat.getY(ev, pointerIndex);
                final float yDiff = y - mInitialMotionY;
                final float scrollTop = yDiff * DRAG_RATE;
                mCurrentDragPercent = scrollTop / mRefreshingDistance;
                if (mCurrentDragPercent < 0) {
                    return false;
                }
                float boundedDragPercent = Math.min(1f, Math.abs(mCurrentDragPercent));
                float extraOS = Math.abs(scrollTop) - mRefreshingDistance;
                float slingshotDist = mRefreshingDistance;
                float tensionSlingshotPercent = Math.max(0,
                        Math.min(extraOS, slingshotDist * 2) / slingshotDist);
                float tensionPercent = (float) ((tensionSlingshotPercent / 4) - Math.pow(
                        (tensionSlingshotPercent / 4), 2)) * 2f;
                float extraMove = (slingshotDist) * tensionPercent / 2;
                int targetY = (int) ((slingshotDist * boundedDragPercent) + extraMove);
                setTargetOffsetTop(targetY - mCurrentOffsetTop, true);
                break;
            }
            case MotionEventCompat.ACTION_POINTER_DOWN:
                final int index = MotionEventCompat.getActionIndex(ev);
                mActivePointerId = MotionEventCompat.getPointerId(ev, index);
                break;
            case MotionEventCompat.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                if (mActivePointerId == INVALID_POINTER) {
                    return false;
                }
                final int pointerIndex = MotionEventCompat.findPointerIndex(ev, mActivePointerId);
                final float y = MotionEventCompat.getY(ev, pointerIndex);
                final float overScrollTop = (y - mInitialMotionY) * DRAG_RATE;
                mIsBeingDragged = false;
                if (overScrollTop > mRefreshHeight) {
                    setRefreshing(true, true);
                } else {
                    mRefreshing = false;
                    animateOffsetToPosition(mAnimateToStartPosition);
                }
                mActivePointerId = INVALID_POINTER;
                return false;
            }
        }

        return true;
    }

    /**
     * 刷新动画
     * @param animation
     */
    private void animateOffsetToPosition(Animation animation) {
        mFrom = mCurrentOffsetTop;
        mFromDragPercent = mCurrentDragPercent;
        long animationDuration = (long) Math.abs(MAX_OFFSET_ANIMATION_DURATION * mFromDragPercent);

        animation.reset();
        animation.setDuration(animationDuration);
        animation.setInterpolator(mDecelerateInterpolator);
        animation.setAnimationListener(mToStartListener);
        mRefreshViewGroup.clearAnimation();
        mRefreshViewGroup.startAnimation(animation);
    }

    /**
     * 根据用户手指滑动的展示动画
     */
    private void animateOffsetToCorrectPosition() {
        mFrom = mCurrentOffsetTop;
        mFromDragPercent = mCurrentDragPercent;

        mAnimateToCorrectPosition.reset();
        mAnimateToCorrectPosition.setDuration(RESTORE_ANIMATION_DURATION);
        mAnimateToCorrectPosition.setInterpolator(mDecelerateInterpolator);
        mRefreshViewGroup.clearAnimation();
        mRefreshViewGroup.startAnimation(mAnimateToCorrectPosition);

        if (mRefreshing) {
            if (mNotify) {
                if (mListener != null) {
                    mListener.onRefresh();
                }
            }
        } else {
            animateOffsetToPosition(mAnimateToStartPosition);
        }
        mCurrentOffsetTop = mTarget.getTop();
    }

    private final Animation mAnimateToStartPosition = new Animation() {
        @Override
        public void applyTransformation(float interpolatedTime, @NonNull Transformation t) {
            moveToStart(interpolatedTime);
        }
    };

    private Animation mAnimateToEndPosition = new Animation() {
        @Override
        public void applyTransformation(float interpolatedTime, @NonNull Transformation t) {
            moveToEnd(interpolatedTime);
        }
    };

    private final Animation mAnimateToCorrectPosition = new Animation() {
        @Override
        public void applyTransformation(float interpolatedTime, @NonNull Transformation t) {
            int targetTop;
            int endTarget = mRefreshHeight;
            targetTop = (mFrom + (int) ((endTarget - mFrom) * interpolatedTime));
            int offset = targetTop - mTarget.getTop();

            mCurrentDragPercent = mFromDragPercent - (mFromDragPercent - 1.0f) * interpolatedTime;

            setTargetOffsetTop(offset, false /* requires update */);
        }

    };

    private void moveToStart(float interpolatedTime) {
        int targetTop = mFrom - (int) (mFrom * interpolatedTime);
        float targetPercent = mFromDragPercent * (1.0f - interpolatedTime);
        int offset = targetTop - mTarget.getTop();

        mCurrentDragPercent = targetPercent;
        setTargetOffsetTop(offset, false);
    }

    private void moveToEnd(float interpolatedTime) {
        int targetTop = mFrom - (int) (mFrom * interpolatedTime);
        float targetPercent = mFromDragPercent * (1.0f + interpolatedTime);
        int offset = targetTop - mTarget.getTop();

        mCurrentDragPercent = targetPercent;
        setTargetOffsetTop(offset, false);
        if (targetTop == 0 && mRefreshState != NORMAL){
            mRefreshState = NORMAL;
            mRefreshView.normal();
        }
    }

    public void setRefreshing(boolean refreshing) {
        if (mRefreshing != refreshing) {
            setRefreshing(refreshing, false /* notify */);
        }
    }

    private void setRefreshing(boolean refreshing, final boolean notify) {
        if (mRefreshing != refreshing) {
            mNotify = notify;
            ensureTarget();
            mRefreshing = refreshing;
            //刷新状态
            if (mRefreshing) {
                if (mRefreshState != BEGIN_REFRESH){
                    mRefreshState = BEGIN_REFRESH;
                    mRefreshView.beginRefresh();
                }

                animateOffsetToCorrectPosition();
            } else {
                if (mRefreshState != REFRESH_COMPLATE){
                    mRefreshState = REFRESH_COMPLATE;
                    mRefreshView.refreshComplete();
                }

                animateOffsetToPosition(mAnimateToEndPosition);

            }
        }
    }

    private Animation.AnimationListener mToStartListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            mCurrentOffsetTop = mTarget.getTop();
        }
    };

    private void onSecondaryPointerUp(MotionEvent ev) {
        final int pointerIndex = MotionEventCompat.getActionIndex(ev);
        final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);

        if (pointerId == mActivePointerId) {
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
        }
    }

    private float getMotionEventY(MotionEvent ev, int activePointerId) {
        final int index = MotionEventCompat.findPointerIndex(ev, activePointerId);
        if (index < 0) {
            return -1;
        }
        return MotionEventCompat.getY(ev, index);
    }

    private void setTargetOffsetTop(int offset, boolean requiresUpdate) {
        mTarget.offsetTopAndBottom(offset);
        mCurrentOffsetTop = mTarget.getTop();

        mRefreshViewGroup.setPadding(0, mCurrentOffsetTop - mRefreshHeight,0,0);
        //刷新状态
        if (mRefreshState != REFRESH_COMPLATE && mRefreshState != BEGIN_REFRESH){
            if (mCurrentOffsetTop < mRefreshHeight){
                mRefreshState = BEGIN_DOWN;
                mRefreshView.beginDown((float) mCurrentOffsetTop / mRefreshHeight);
            }else if (mCurrentOffsetTop >= mRefreshHeight){
                if (mRefreshState != CAN_REFRESH){
                    mRefreshState = CAN_REFRESH;
                    mRefreshView.canRefresh();
                }

            }
        }


        if (requiresUpdate && android.os.Build.VERSION.SDK_INT < 11) {
            invalidate();
        }
    }

    /**
     * 是否滑动到顶部
     * @return
     */
    private boolean isChildScrollToTop() {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (mTarget instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) mTarget;
                return !(absListView.getChildCount() > 0
                        && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
                        .getTop() < absListView.getPaddingTop()));
            } else {
                return ! (mTarget.getScrollY() > 0);
            }
        } else {
            return !ViewCompat.canScrollVertically(mTarget, -1);
        }

    }

    /**
     * 是否滑动到底部
     * @return
     */
    public boolean isChildScrollToBottom() {
        if (mTarget instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) mTarget;
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (recyclerView.getAdapter()==null) {
                return false;
            }
            int count = recyclerView.getAdapter().getItemCount();
            if (layoutManager instanceof LinearLayoutManager && count > 0) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == count - 1) {
                    return true;
                }
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                int[] lastItems = new int[2];
                staggeredGridLayoutManager
                        .findLastCompletelyVisibleItemPositions(lastItems);
                int lastItem = Math.max(lastItems[0], lastItems[1]);
                if (lastItem == count - 1) {
                    return true;
                }
            }
            return false;
        } else
        if (mTarget instanceof AbsListView) {
            final AbsListView absListView = (AbsListView) mTarget;
            int count = absListView.getAdapter().getCount();
            int fristPos = absListView.getFirstVisiblePosition();
            if (fristPos == 0
                    && absListView.getChildAt(0).getTop() >= absListView
                    .getPaddingTop()) {
                return false;
            }
            int lastPos = absListView.getLastVisiblePosition();
            if (lastPos > 0 && count > 0 && lastPos == count - 1) {
                return true;
            }
            return false;
        } else if (mTarget instanceof ScrollView) {
            ScrollView scrollView = (ScrollView) mTarget;
            View view = (View) scrollView
                    .getChildAt(scrollView.getChildCount() - 1);
            if (view != null) {
                int diff = (view.getBottom() - (scrollView.getHeight() + scrollView
                        .getScrollY()));
                if (diff == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private void asd(){
        Log.d("TAG","mTarget.getScrollY()="+mTarget.getScrollY());
    }

    public void setOnRefreshListener(OnRefreshListener listener) {
        mListener = listener;
    }


    public interface OnRefreshListener {
        void onRefresh();
    }

    public void refreshType(int type){
        REFRESH_TYPE = type;
    }

}
