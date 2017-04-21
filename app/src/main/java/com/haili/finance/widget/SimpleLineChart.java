package com.haili.finance.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.haili.finance.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author wing
 * @url http://blog.csdn.net/wingichoy
 * <p/>
 * 这是一个简约的折线图  适合展示一个趋势 而并非精确数据
 * <p/>
 * Created by Administrator on 2015/12/30.
 */
public class SimpleLineChart extends View {

    private int point = 0;
    private Context context;
    private boolean isTouch = false;
    int message = 1;
    float drawPadding = 17.5f;
    private Paint linePaint;
    private Paint hintPaint;
    private Paint textPaint;
    private Paint mPointPaint;
    //触摸点坐标
    float touchX;
    float touchY;

    //View 的宽和高
    private int mWidth, mHeight;

    //Y轴字体的大小
    private float mYAxisFontSize = 24;

    //线的颜色
    private int mLineColor = Color.parseColor("#ffffff");
    private int color = Color.parseColor("#00ffffff");

    //线条的宽度
    private float mStrokeWidth = 4.0f;

    //点的集合
    private HashMap<Integer, Integer> mPointMap;
    private ArrayList<Float> mPointList;

    //点的半径
    private float mPointRadius = 10;

    //没有数据的时候的内容
    private String mNoDataMsg = "no data";

    //X轴的文字
    private String[] mXAxis = {};

    //Y轴的文字
    private String[] mYAxis = {};

    //点的最终绘制Y值
    private float yPointsfinish[];

    //x轴的刻度集合
    private int[] xPoints;

    public SimpleLineChart(Context context) {
        this(context, null);
        init();
        this.context = context;
    }

    public SimpleLineChart(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init();
        this.context = context;
    }

    public SimpleLineChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        this.context = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            throw new IllegalArgumentException("width must be EXACTLY,you should set like android:width=\"200dp\"");
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize;
        } else if (widthMeasureSpec == MeasureSpec.AT_MOST) {

            throw new IllegalArgumentException("height must be EXACTLY,you should set like android:height=\"200dp\"");
        }

        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mPointList == null || mPointList.size() == 0){
            return;
        }
        if (mXAxis.length == 0 || mYAxis.length == 0) {
            throw new IllegalArgumentException("X or Y items is null");
        }
        //画坐标线的轴
        Paint axisPaint = new Paint();
        axisPaint.setAntiAlias(true);
        axisPaint.setTextSize(mYAxisFontSize);
        axisPaint.setColor(Color.parseColor("#fec5a3"));

        if (mPointMap == null || mPointMap.size() == 0) {
            int textLength = (int) axisPaint.measureText(mNoDataMsg);
            canvas.drawText(mNoDataMsg, mWidth / 2 - textLength / 2, mHeight / 2, axisPaint);
        } else {
            //画 Y 轴


            //存放每个Y轴的坐标
            float[] yPoints = new float[mYAxis.length];


            //计算Y轴 每个刻度的间距
            int yInterval = (int) (mHeight / (mYAxis.length + 1));

            //测量Y轴文字的高度 用来画第一个数
            Paint.FontMetrics fm = axisPaint.getFontMetrics();
            int yItemHeight = (int) Math.ceil(fm.descent - fm.ascent);

            for (int i = 1; i <= mYAxis.length; i++) {
                canvas.drawText(mYAxis[i - 1], 0, i * yInterval, axisPaint);
                if (i - 1 < mPointList.size()) {
                    yPoints[i - 1] = (float) mHeight - (mPointList.get(i - 1) * mHeight) + mYAxisFontSize / 4;
                }
            }


            //画 X 轴

            xPoints = new int[mXAxis.length];

            //计算Y轴开始的原点坐标
            int xItemX = (int) axisPaint.measureText(mYAxis[1]);

            //X轴偏移量
            int xOffset = 0;
            //计算x轴 刻度间距
            int xInterval = ((mWidth - (xItemX * 2)) / ((mXAxis.length) - 1)) - 20;
            //获取X轴刻度Y坐标
            int xItemY = (int) ((mYAxis.length + 1) * yInterval);

            for (int i = 0; i < mXAxis.length; i++) {
                if (i == mXAxis.length - 1) {
                    axisPaint.setColor(Color.parseColor("#ffffff"));
                } else {
                    axisPaint.setColor(Color.parseColor("#fec5a3"));
                }
                canvas.drawText(mXAxis[i], i * xInterval + xItemX + xOffset, xItemY, axisPaint);
                xPoints[i] = (int) (i * xInterval + xItemX + axisPaint.measureText(mXAxis[i]) / 2 + xOffset);
            }


            //画点
            Paint pointPaint = new Paint();
            pointPaint.setAntiAlias(true);
            pointPaint.setColor(mLineColor);
            pointPaint.setStyle(Paint.Style.FILL);

            linePaint = new Paint();
            linePaint.setAntiAlias(true);
            linePaint.setColor(mLineColor);
            //设置线条宽度
            linePaint.setStrokeWidth(mStrokeWidth);


            Paint mLinePaint = new Paint();
            mLinePaint.setAntiAlias(true);
            mLinePaint.setColor(mLineColor);
            //设置线条宽度
            mLinePaint.setStrokeWidth(2.0f);
            //新建一个线性渐变，前两个参数是渐变开始的点坐标，第三四个参数是渐变结束的点的坐标。连接这2个点就拉出一条渐变线了，玩过PS的都懂。
            // 然后那个数组是渐变的颜色。下一个参数是渐变颜色的分布，如果为空，每个颜色就是均匀分布的。最后是模式，这里设置的是循环渐变
            Shader mShader = new LinearGradient(0, 0, 0, 150, new int[]{mLineColor, color}, null, Shader.TileMode.REPEAT);
            mLinePaint.setShader(mShader);
            yPointsfinish = new float[mYAxis.length];
            for (int i = 0; i < message; i++) {
                if (mPointMap.get(i) == -1) {
                    throw new IllegalArgumentException("PointMap has incomplete data!");
                }

                if (i > 0) {
                    canvas.drawLine(xPoints[i - 1], yPoints[mPointMap.get(i - 1)] - (yItemHeight) / 3, xPoints[i], yPoints[mPointMap.get(i)] - (yItemHeight) / 3, linePaint);
                }

                //画点
                if (i == mXAxis.length - 1) {
                    pointPaint.setMaskFilter(new BlurMaskFilter(50, BlurMaskFilter.Blur.SOLID));
                    canvas.drawBitmap(BitmapFactory.decodeResource(this.getResources(), R.mipmap.line_image), xPoints[i] - dp2px(context, drawPadding),
                            (yPoints[mPointMap.get(i)] - (yItemHeight) / 3) - dp2px(context, drawPadding), pointPaint);
                    yPointsfinish[i] = yPoints[mPointMap.get(i)] - (yItemHeight) / 3;
                    if (!isTouch) {
                        point = 2;
                        drawFloatTextBox(canvas, xPoints[2], yPointsfinish[2] - 10, mPointList.get(2) + "元");
                    }

                } else {
                    canvas.drawCircle(xPoints[i], yPoints[mPointMap.get(i)] - (yItemHeight) / 3, 2.0f, pointPaint);
                    yPointsfinish[i] = yPoints[mPointMap.get(i)] - (yItemHeight) / 3;
                }

            }
            if (isTouch) {
                drawFloatTextBox(canvas, xPoints[point], yPointsfinish[point] - 10, mPointList.get(point) + "元");
            }

        }

    }

    private void init() {
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        mPointPaint = new Paint();
        mPointPaint.setAntiAlias(true);
        mPointPaint.setColor(mLineColor);
        mPointPaint.setStyle(Paint.Style.FILL);
        mPointPaint.setMaskFilter(new BlurMaskFilter(6f, BlurMaskFilter.Blur.SOLID));
    }

    /**
     * 绘制显示Y值的浮动框
     *
     * @param canvas
     * @param x
     * @param y
     * @param text
     */
    private void drawFloatTextBox(Canvas canvas, float x, float y, String text) {

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.mipmap.hint_image, opts);
        opts.inSampleSize = 1;
        opts.inJustDecodeBounds = false;
        Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.hint_image, opts);
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.parseColor("#eb5c05"));
        textPaint.setTextSize(30);
        textPaint.setStyle(Paint.Style.FILL);
        float length = textPaint.measureText(text);
        mBitmap = stretchBitmap(mBitmap, length);
        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();
        if (point == 0) {
            Matrix m = new Matrix();
            m.setScale(-1, 1);
            m.postTranslate(x + width, y - height / 8 * 3); //向右平移两个图片宽度的位置
            canvas.drawBitmap(mBitmap, m, hintPaint);
            canvas.drawText(text + "", x + width / 4, y + dp2px(context, 5f), textPaint);
        } else {
            canvas.drawBitmap(mBitmap, x - width, y - height / 2, hintPaint);
            canvas.drawText(text + "", x - width + width / 6, y + dp2px(context, 1.5f), textPaint);
        }
    }

    private Bitmap stretchBitmap(Bitmap src, float textSize) {

        if (src == null) {
            return null;
        }
        int w = src.getWidth();//源文件的大小
        int h = src.getHeight();
        float scaleWidth;
        // calculate the scale - in this case = 0.4f
        if (textSize > w) {
            scaleWidth = ((float) textSize + dp2px(context, 5f)) / w;
        } else {
            scaleWidth = ((float) w) / w;
        }
        Matrix m = new Matrix();//矩阵
        m.postScale(scaleWidth, 0.6f);//设置矩阵比例
        Bitmap resizedBitmap = Bitmap.createBitmap(src, 0, 0, w, h, m, true);//直接按照矩阵的比例把源文件画入进行
        return resizedBitmap;
    }

    /**
     * 根据线程传递的消息绘制点和线
     */
    public void drawPointAndLine(int message) {
        this.message = message;
        invalidate();
    }


    /**
     * 设置map数据
     *
     * @param data
     */
    public void setData(HashMap<Integer, Integer> data, ArrayList<Float> myData) {
        mPointMap = data;
        mPointList = myData;
        invalidate();
    }

    /**
     * 设置Y轴文字
     *
     * @param yItem
     */
    public void setYItem(String[] yItem) {
        mYAxis = yItem;
    }

    /**
     * 设置X轴文字
     *
     * @param xItem
     */
    public void setXItem(String[] xItem) {
        mXAxis = xItem;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchX = event.getRawX();
                touchY = event.getRawY();
                for (int i = 0; i < mXAxis.length; i++) {
                    if ((touchX < xPoints[i] + 50 && touchX > xPoints[i] - 50) && (touchY < yPointsfinish[i] + dp2px(context, 125f) && touchY > yPointsfinish[i] + 75f)) {
                        isTouch = true;
                        point = i;
                        invalidate();
                    }
                }
        }
        return true;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

}
