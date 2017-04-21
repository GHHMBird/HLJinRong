package com.haili.finance.helper;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.Base64;

public class BitmapHelper {

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            roundPx = width / 2;
            left = 0;
            top = 0;
            right = width;
            bottom = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }
        Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
//        final RectF rectF = new RectF(dst);  

        paint.setAntiAlias(true);// 设置画笔无锯齿  

        canvas.drawARGB(0, 0, 0, 0); // 填充整个Canvas  
        paint.setColor(color);

        // 以下有两种方法画圆,drawRounRect和drawCircle  
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);// 画圆角矩形，第一个参数为图形显示区域，第二个参数和第三个参数分别是水平圆角半径和垂直圆角半径。  
        canvas.drawCircle(roundPx, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));// 设置两张图片相交时的模式,参考http://trylovecatch.iteye.com/blog/1189452
        canvas.drawBitmap(bitmap, src, dst, paint); //以Mode.SRC_IN模式合并bitmap和已经draw了的Circle  

        return output;
    }

    public static Bitmap getImageFromStr(String imgStr) { // 对字节数组字符串进行Base64解码并生成图片
        if (imgStr == null) // 图像数据为空
            return null;
        try {
            // Base64解码
            byte[] b = Base64.decode(imgStr, Base64.DEFAULT);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {// 调整异常数据
                    b[i] += 256;
                }
            }
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static Bitmap getBitmap(Bitmap bitmap) {
        Bitmap mSrc;
        int mWidth = bitmap.getWidth();
        int mHeight = bitmap.getHeight();
        int min = Math.min(mWidth, mHeight);

        /*
         * 长度如果不一致，按小的值进行压缩
         */
        mSrc = Bitmap.createScaledBitmap(bitmap, min, min, false);
        Bitmap output = Bitmap.createBitmap(mSrc.getWidth(), mSrc.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        canvas.drawBitmap(createCircleImage(mSrc, min), 0, 0, null);
        return output;
    }

    /*
     * 根据原图和变长绘制圆形图片
     *
     * @param source
     * @param min
     * @return
     */
    public static Bitmap createCircleImage(Bitmap source, int min) {
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap target = Bitmap.createBitmap(min, min, Config.ARGB_8888);
        /*
         * 产生一个同样大小的画布
         */
        Canvas canvas = new Canvas(target);
        /*
         * 首先绘制圆形
         */
        canvas.drawCircle(min / 2, min / 2, min / 2, paint);
        /*
         * 使用SRC_IN，参考上面的说明
         */
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        /*
         * 绘制图片
         */
        canvas.drawBitmap(source, 0, 0, paint);
        return target;
    }
}
