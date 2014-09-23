package com.jesson.android;

import android.content.Context;
import com.jesson.android.utils.DeviceInfo;
import com.jesson.android.utils.Logger;

/**
 * Created by zhangdi on 14-3-4.
 */
public class Jess {

    public static boolean DEBUG = false;

    public static void init(Context context) {
        DeviceInfo.init(context);
    }

    public static void LOGD(String msg) {
        Logger.d("com.michael", msg);
    }

}
