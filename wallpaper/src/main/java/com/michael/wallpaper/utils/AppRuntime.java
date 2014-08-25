package com.michael.wallpaper.utils;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import com.jesson.android.Jess;
import com.jesson.android.internet.InternetUtils;
import com.jesson.android.utils.CustomThreadPool;
import com.jesson.android.utils.UtilsRuntime;
import com.michael.wallpaper.AppConfig;
import com.michael.wallpaper.api.stat.StatRequest;
import com.michael.wallpaper.api.stat.StatResponse;

/**
 * Created by michael on 14-5-13.
 */
public class AppRuntime {

    public static String RAW_URL_CACHE_DIR = "/sdcard/";

    public static String PACKAGE_NAME = "";

    public static boolean SHOW_BANNER = true;

    public static boolean useStaggerGridView() {
        if (AppRuntime.PACKAGE_NAME.endsWith(AppConfig.GAOXIAO_WALLPAPER_PACKAGE_NAMMME)
            || AppRuntime.PACKAGE_NAME.endsWith(AppConfig.CAR_PACKAGE_NAME)
            || AppRuntime.PACKAGE_NAME.equals(AppConfig.BAIDU_SOURCE_MM_PACKAGE_NAME)) {
            return true;
        }

        return false;
    }

    public static String makeRawUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }

        int pos = url.indexOf("_tn");
        if (pos != -1) {
            return url.replace("_tn", "");
        }

        return null;
    }

    public static void updateStat(final Context context) {
        if (context != null) {
            CustomThreadPool.asyncWork(new Runnable() {
                @Override
                public void run() {
                    try {
                        StatRequest request = new StatRequest("百度",
                                                                 "5",
                                                                 UtilsRuntime.getVersionName(context),
                                                                 "android",
                                                                 UtilsRuntime.getIMEI(context),
                                                                 UtilsRuntime.getIMSI(context),
                                                                 UtilsRuntime.getCurrentPhoneNumber(context),
                                                                 UtilsRuntime.getLocalMacAddress(context),
                                                                 "0",
                                                                 Build.MODEL,
                                                                 UtilsRuntime.getIMEI(context),
                                                                 UtilsRuntime.getIMEI(context));
                        StatResponse response = InternetUtils.request(context, request);
                        if (response != null) {
                            Jess.LOGD("[[updateStat]] response : " + response);
                        } else {
                            Jess.LOGD("[[updateStat]] response null");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    }

}
