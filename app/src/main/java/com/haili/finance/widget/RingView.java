package com.haili.finance.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.haili.finance.R;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by fuliang on 2017/2/27.
 */

public class RingView extends View {

    private Paint paint;
    private Paint mPaint;
    private final Context context;
    private List<Integer> checkedPoint = new ArrayList<>();
    private int drawType = 0;

    public RingView(Context context) {
        this(context, null);
    }

    public RingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.paint = new Paint();
        this.paint.setAntiAlias(true); //消除锯齿
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int pointItemWidth = width / 9; // 每个点所占用方格的宽度
        int innerCircle = dip2px(context, 4); //设置内圆半径
        paint.setColor(getResources().getColor(R.color.white));
        mPaint.setColor(getResources().getColor(R.color.white));
        //------------------------------0----------------------------
        //绘制内圆
        paint.setStrokeWidth(2);
        mPaint.setStrokeWidth(2);

        if (checkedPoint.contains(0)){
            canvas.drawCircle(pointItemWidth*4,pointItemWidth/2,innerCircle,this.mPaint);
        }else {
            this.paint.setStyle(Paint.Style.STROKE); //绘制空心圆
            canvas.drawCircle(pointItemWidth*4,pointItemWidth/2, innerCircle, this.paint);
        }


        //------------------------------1----------------------------
        //绘制内圆
        if (checkedPoint.contains(1)){
            canvas.drawCircle(pointItemWidth*4.5f,pointItemWidth/2, innerCircle, this.mPaint);
        }else {
            this.paint.setStyle(Paint.Style.STROKE); //绘制空心圆
            canvas.drawCircle(pointItemWidth*4.5f,pointItemWidth/2, innerCircle, this.paint);
        }


        //------------------------------2----------------------------
        //绘制内圆
        if (checkedPoint.contains(2)){
            canvas.drawCircle(pointItemWidth*5,pointItemWidth/2, innerCircle, this.mPaint);
        }else {
            canvas.drawCircle(pointItemWidth*5,pointItemWidth/2, innerCircle, this.paint);
        }


        //------------------------------3----------------------------
        //绘制内圆
        if (checkedPoint.contains(3)){
            canvas.drawCircle(pointItemWidth*4,pointItemWidth, innerCircle, this.mPaint);
        }else {
            canvas.drawCircle(pointItemWidth*4,pointItemWidth, innerCircle, this.paint);
        }


        //------------------------------4----------------------------
        if (checkedPoint.contains(4)){
            canvas.drawCircle(pointItemWidth*4.5f,pointItemWidth, innerCircle, this.mPaint);
        }else {
            canvas.drawCircle(pointItemWidth*4.5f,pointItemWidth, innerCircle, this.paint);
        }

        //------------------------------5----------------------------
        if (checkedPoint.contains(5)){
            canvas.drawCircle(pointItemWidth*5,pointItemWidth, innerCircle, this.mPaint);
        }else {
            canvas.drawCircle(pointItemWidth*5,pointItemWidth, innerCircle, this.paint);
        }

        //------------------------------6----------------------------
        if (checkedPoint.contains(6)){
            canvas.drawCircle(pointItemWidth*4,pointItemWidth*1.5f, innerCircle, this.mPaint);
        }else {
            canvas.drawCircle(pointItemWidth*4,pointItemWidth*1.5f, innerCircle, this.paint);
        }


        //------------------------------7----------------------------
        if (checkedPoint.contains(7)){
            canvas.drawCircle(pointItemWidth*4.5f,pointItemWidth*1.5f, innerCircle, this.mPaint);
        }else {
            canvas.drawCircle(pointItemWidth*4.5f,pointItemWidth*1.5f, innerCircle, this.paint);
        }

        //------------------------------8----------------------------
        if (checkedPoint.contains(8)){
            canvas.drawCircle(pointItemWidth*5,pointItemWidth*1.5f, innerCircle, this.mPaint);
        }else {
            canvas.drawCircle(pointItemWidth*5,pointItemWidth*1.5f, innerCircle, this.paint);
        }



        super.onDraw(canvas);
    }

    public void getCheckedPoint(List<Integer> pointList){
        checkedPoint.clear();
        checkedPoint.addAll(pointList);

    }

    public void setType(int type){
        drawType = type;
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
