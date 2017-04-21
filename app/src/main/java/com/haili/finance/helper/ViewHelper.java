package com.haili.finance.helper;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.haili.finance.R;
import com.haili.finance.widget.EToast;
import com.haili.finance.widget.Toast;

/*
 * Created by lfu on 2017/2/21.
 */

public class ViewHelper {

    public interface OnPositiveBtnClickedListener {
        void onPositiveBtnClicked(MaterialDialog dialog);
    }

    public interface OnNegativeBtnClickedListener {
        void onNegativeBtnClicked(MaterialDialog dialog);
    }


    public static void showToast(Context context, String content) {
        Toast.makeText(context, content, Toast.LENGTH_LONG).show();
    }

    public static void toastStop() {
        EToast.finish();
    }

    public static MaterialDialog buildDialog(Context context, String title, String content, final OnPositiveBtnClickedListener listener) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(context);
        builder.title(title);
        builder.content(content);
        if (listener != null) {
            builder.positiveText(R.string.ok);
            builder.negativeText(R.string.cancel_dialog);
            builder.positiveColorRes(R.color.colorAccent);
            builder.negativeColorRes(R.color.colorAccent);
            builder.autoDismiss(false);
            builder.onNegative(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    dialog.dismiss();
                }
            });
            builder.onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    listener.onPositiveBtnClicked(dialog);
                }
            });
        }
        return builder.build();
    }

    public static MaterialDialog buildNoTitleTextDialog(Context context, String content, final OnPositiveBtnClickedListener listener) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(context);
        builder.positiveText(R.string.ok);
        builder.negativeText(R.string.cancel);
        builder.positiveColorRes(R.color.colorAccent);
        builder.negativeColorRes(R.color.colorAccent);
        builder.content(content);
        builder.onNegative(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
            }
        });
        builder.onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                listener.onPositiveBtnClicked(dialog);
            }
        });
        return builder.build();
    }

    public static MaterialDialog buildNoTitleTextDialog(Activity activity, String content) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(activity);
        builder.negativeText(R.string.ok);
        builder.content(content);
        builder.positiveColorRes(R.color.colorAccent);
        builder.negativeColorRes(R.color.colorAccent);
        builder.onNegative(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
            }
        });
        return builder.build();
    }

    public static MaterialDialog buildNoTitleTextDialog2(Activity activity, String content, final OnPositiveBtnClickedListener listener) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(activity);
        builder.negativeText(R.string.ok);
        builder.content(content);
        builder.cancelable(false);
        builder.positiveColorRes(R.color.colorAccent);
        builder.negativeColorRes(R.color.colorAccent);
        builder.onNegative(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                listener.onPositiveBtnClicked(dialog);
            }
        });
        return builder.build();
    }

    public static MaterialDialog buildNoTitleTextDialog3(Context context, String content, final OnPositiveBtnClickedListener listener, final OnNegativeBtnClickedListener myListener) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(context);
        builder.positiveText(R.string.ok);
        builder.negativeText(R.string.cancel);
        builder.positiveColorRes(R.color.colorAccent);
        builder.negativeColorRes(R.color.colorAccent);
        builder.content(content);
        builder.onNegative(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                myListener.onNegativeBtnClicked(dialog);
            }
        });
        builder.onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                listener.onPositiveBtnClicked(dialog);
            }
        });
        return builder.build();
    }

    public static MaterialDialog buildNoTitleTextDialog4(Context context, String content, final OnPositiveBtnClickedListener listener) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(context);
        builder.positiveText("好的");
        builder.negativeText("暂不设置");
        builder.positiveColorRes(R.color.colorAccent);
        builder.negativeColorRes(R.color.colorAccent);
        builder.content(content);
        builder.onNegative(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
            }
        });
        builder.onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                listener.onPositiveBtnClicked(dialog);
            }
        });
        return builder.build();
    }

}
