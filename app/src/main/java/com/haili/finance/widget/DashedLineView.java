package com.haili.finance.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.View;

import com.haili.finance.R;
import com.haili.finance.utils.ScreenUtils;

/**
 * Created by Monkey on 2017/1/9.
 * 虚线
 */

public class DashedLineView extends View {
    private Context context;
    public DashedLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(context.getResources().getColor(R.color.line_grey));
        Path path = new Path();
        path.moveTo(0, 10);
        path.lineTo(ScreenUtils.getScreenWidth(context),10);
        PathEffect effects = new DashPathEffect(new float[]{10,10,10,10},1);
        paint.setPathEffect(effects);
        canvas.drawPath(path, paint);
    }
}