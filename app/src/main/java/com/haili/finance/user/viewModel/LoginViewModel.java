package com.haili.finance.user.viewModel;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Build;
import android.view.View;

import com.haili.finance.helper.BitmapHelper;
import com.haili.finance.utils.StringUtils;

/**
 * Created by lfu on 2017/4/14.
 */

public class LoginViewModel {

    private Activity activity;
    public int index = 0;
    public String phoneText;

    public LoginViewModel(Activity activity){
        this.activity = activity;
    }

    public boolean placeViewControl() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            return true;

        } else {
            return false;
        }
    }

    public Bitmap changeBitmap(String imageCode){
        Bitmap bitmap = BitmapHelper.getImageFromStr(imageCode.substring(imageCode.indexOf(","), imageCode.length()));
        Matrix matrix = new Matrix();
        matrix.postScale(4f, 5f); //长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizeBmp;
    }

    public boolean changeString(String contents,int arg1,int arg2){
        if (StringUtils.isEmpty(contents))
            return false;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < contents.length(); i++) {
            if (i != 3 && i != 8 && contents.charAt(i) == ' ') {
                continue;
            } else {
                sb.append(contents.charAt(i));
                if ((sb.length() == 4 || sb.length() == 9)
                        && sb.charAt(sb.length() - 1) != ' ') {
                    sb.insert(sb.length() - 1, ' ');
                }
            }

        }
        if (!sb.toString().equals(contents)) {
            int index = arg1 + 1;
            if (sb.charAt(arg1) == ' ') {
                if (arg2 == 0) {
                    index++;
                } else {
                    index--;
                }
            } else {
                if (arg2 == 1) {
                    index--;
                }
            }
            this.index = index;
            this.phoneText = sb.toString();
            return true;
        }
        return false;
    }
}
