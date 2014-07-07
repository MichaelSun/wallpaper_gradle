package com.michael.wallpaper;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import com.jesson.android.Jess;
import com.jesson.android.internet.InternetUtils;
import com.jesson.android.utils.DeviceInfo;
import com.jesson.android.utils.UtilsRuntime;
import com.jesson.android.widget.Toaster;
import com.michael.wallpaper.setting.Setting;
import com.michael.wallpaper.utils.AppRuntime;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import net.youmi.android.AdManager;

import java.io.File;

/**
 * Created by zhangdi on 14-3-4.
 */
public class BelleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AppRuntime.RAW_URL_CACHE_DIR = "/sdcard/." + UtilsRuntime.getPackageName(getApplicationContext()) + "/rawCache/";
        File dirCheck = new File(AppRuntime.RAW_URL_CACHE_DIR);
        if (!dirCheck.exists()) {
            dirCheck.mkdirs();
        }

        AppRuntime.PACKAGE_NAME = getPackageName();
        if (AppRuntime.PACKAGE_NAME.endsWith(AppConfig.MM_WALLPAPER_PACKAGE_NAMMME)) {
            //美女壁纸
            AppConfig.SERIES_MODE = 2;
        }

        Jess.init(this);
        Jess.DEBUG = AppConfig.DEBUG;


        initImageLoader();

        registerInternetError();

        AppRuntime.updateStat(getApplicationContext());
        initYoumi();

        Setting.getInstace().init(getApplicationContext());
    }

    private void initYoumi() {
        AdManager.getInstance(getApplicationContext()).init(AppConfig.YOUMI_APIKEY, AppConfig.YOUMI_SECRETKEY, false);
    }

    private String getMetaData(String key) {
        try {
            Bundle metaData = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA).metaData;
            return metaData.getString(key);
        } catch (Exception e) {

        }
        return null;
    }

    private void initImageLoader() {
        File cache = new File("/sdcard/." + UtilsRuntime.getPackageName(getApplicationContext()) + "/imagecache");
        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(this)
                                                       .threadPoolSize(8)
                                                       .denyCacheImageMultipleSizesInMemory()
                                                       .memoryCacheSize(DeviceInfo.MEM_SIZE / 16 * 1024 * 1024)
                                                       .memoryCache(new WeakMemoryCache())
                                                       .diskCache(new UnlimitedDiscCache(cache))
                                                       .defaultDisplayImageOptions(new DisplayImageOptions.Builder()
                                                                                       .resetViewBeforeLoading(true)
                                                                                       .cacheInMemory(true)
                                                                                       .cacheOnDisk(true)
//                                                                                       .considerExifParams(true)
                                                                                       .imageScaleType(ImageScaleType.EXACTLY)
//                                                                                       .displayer(new RoundedBitmapDisplayer(8))
                                                                                       .build());
        if (AppConfig.DEBUG) {
            builder.writeDebugLogs();
        }
        ImageLoader.getInstance().init(builder.build());
    }

    private void registerInternetError() {
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(getApplicationContext());
        IntentFilter filter = new IntentFilter();
        filter.addAction(InternetUtils.ACTION_INTERNET_ERROR);
        filter.addAction(InternetUtils.ACTION_INTERNET_ERROR_LOCAL);
        lbm.registerReceiver(mInternetBRC, filter);
    }

    private BroadcastReceiver mInternetBRC = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null) return;

            if (InternetUtils.ACTION_INTERNET_ERROR.equals(intent.getAction())) {
                Toaster.show(context, R.string.api_server_error);
            } else if (InternetUtils.ACTION_INTERNET_ERROR_LOCAL.equals(intent.getAction())) {
                String msg = intent.getStringExtra("msg");
                Toaster.show(context, msg);
            }
        }
    };

}
