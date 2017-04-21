package com.haili.finance.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.haili.finance.R;

/**
 * Created by lfu on 2017/2/24.
 */

public class CustomToast {

    public static void showToast(Context context, String message) {
//        //加载Toast布局
//        View toastRoot = LayoutInflater.from(context).inflate(R.layout.toast_layout, null);
//        //初始化布局控件
//        TextView mTextView = (TextView) toastRoot.findViewById(R.id.toast_text);
//        //为控件设置属性
//        mTextView.setText(message);
//        //Toast的初始化
//        Toast toastStart = new Toast(context);
//        //获取屏幕高度
//        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        int height = wm.getDefaultDisplay().getHeight();
//        //Toast的Y坐标是屏幕高度的1/3，不会出现不适配的问题
//        toastStart.setGravity(Gravity.TOP, 0, height / 2);
//        toastStart.setDuration(Toast.LENGTH_LONG);
//        toastStart.setView(toastRoot);
//        toastStart.show();
    }
}
