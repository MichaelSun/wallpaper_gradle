package com.jesson.android.widget;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;
import com.github.johnpersano.supertoasts.SuperToast;

/**
 * Created by zhangdi on 14-2-12.
 * 管理系统Toast，避免Toast压栈
 */
public class Toaster {

    private static Toast toast;

    public static void showNative(Context context, String text) {
        if (toast == null) {
            synchronized (Toaster.class) {
                if (toast == null) {
                    toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
                }
            }
        } else {
            toast.setText(text);
        }

//        cancel();

        toast.show();
    }

    public static void cancelNative() {
        if (toast != null) {
            toast.cancel();
        }
    }

    public static void showNative(Context context, int resId) {
        showNative(context, context.getString(resId));
    }

    private static SuperToast superToast;

    public static void show(Activity activity, String text) {
        showNative(activity, text);

//        if (superToast == null) {
//            synchronized (SuperCardToast.class) {
//                if (superToast == null) {
//                    superToast = new SuperToast(activity.getApplicationContext());
//                    superToast.setAnimations(SuperToast.Animations.POPUP);
//                    superToast.setDuration(SuperToast.Duration.LONG);
//                    superToast.setTextSize(SuperToast.TextSize.SMALL);
//                    superToast.setBackground(SuperToast.Background.BLUE);
////                    superToast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
//                    superToast.setIcon(R.drawable.toast_48, SuperToast.IconPosition.LEFT);
//                    superToast.setText(text);
//                }
//            }
//        } else {
//            superToast.setText(text);
//        }
//
//        cancel();
//        superToast.show();
    }

    public static void cancel() {
        cancelNative();

//        if (superToast != null) {
//            superToast.dismiss();
//        }
    }

    public static void show(Activity activity, int resId) {
        show(activity, activity.getString(resId));
    }

}
