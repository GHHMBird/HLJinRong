package com.haili.finance.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Monkey on 2017/1/14.
 */

public class MyListView extends ListView {
    private Context context;

    public MyListView(Context context) {
        super(context);
        this.context = context;

    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public MyListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

//        @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                setParentScrollAble(false);//当手指触到listview的时候，让父ScrollView交出ontouch权限，也就是让父scrollview停住不能滚动
//                Log.d("text1", "onInterceptTouchEventdown");
//
//            case MotionEvent.ACTION_MOVE:
//                Log.d("text1", "onInterceptTouchEvent move");
//                break;
//            case MotionEvent.ACTION_UP:
//                Log.d("text1", "onInterceptTouchEvent up");
//            case MotionEvent.ACTION_CANCEL:
//                Log.d("text1", "onInterceptTouchEvent cancel");
//                setParentScrollAble(true);//当手指松开时，让父ScrollView重新拿到onTouch权限
//                break;
//            default:
//                Log.d("text1", "onInterceptTouchEvent ev");
//                break;
//
//        }
//        return super.onInterceptTouchEvent(ev);

//    }
    /**
     * 是否把滚动事件交给父scrollview
     *
     * @param flag
     */
//    private void setParentScrollAble(boolean flag) {
//        Activity activity = (Activity) context;
//        MyScrollView myScrollView = (MyScrollView) activity.findViewById(R.id.srollview_2);
//        myScrollView.requestDisallowInterceptTouchEvent(!flag);//这里的parentScrollView就是listview外面的那个scrollview
//    }
}
