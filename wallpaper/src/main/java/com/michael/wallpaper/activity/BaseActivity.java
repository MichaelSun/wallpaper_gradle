package com.michael.wallpaper.activity;

import android.app.Activity;
import cn.domob.android.ads.DomobInterstitialAd;
import com.michael.wallpaper.AppConfig;
import com.michael.wallpaper.R;
import com.michael.wallpaper.utils.AppRuntime;

/**
 * Created by zhangdi on 14-3-5.
 */
public class BaseActivity extends Activity {

    protected DomobInterstitialAd mDomobInterstitialAd;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected int getIconResByPackageName() {
        if (AppRuntime.PACKAGE_NAME.endsWith(AppConfig.BAIDU_SOURCE_MM_PACKAGE_NAME)) {
            return R.drawable.ic_launcher;
        } else if (AppRuntime.PACKAGE_NAME.endsWith(AppConfig.CAR_PACKAGE_NAME)) {
            return R.drawable.ic_launcher;
        } else if (AppRuntime.PACKAGE_NAME.endsWith(AppConfig.MM_WALLPAPER_PACKAGE_NAMMME)) {
            return R.drawable.ic_wallpaper;
        }

        return R.drawable.ic_launcher;
    }

    protected void initSplashAd() {
        if (AppConfig.GOOLE_AD_ENABLE) {
        } else if (AppConfig.DOMOD_AD_ENABLE) {
            mDomobInterstitialAd = new DomobInterstitialAd(this, AppConfig.DOMOD_PUBLISH_KEY, AppConfig.DOMOD_PLACEMENT_KEY, DomobInterstitialAd.INTERSITIAL_SIZE_FULL_SCREEN);
            mDomobInterstitialAd.loadInterstitialAd();
        }
    }

    protected void tryToShwoSplashAd() {
        if (AppConfig.GOOLE_AD_ENABLE) {
        } else if (AppConfig.DOMOD_AD_ENABLE) {
//            if (mDomobInterstitialAd != null && mDomobInterstitialAd.isInterstitialAdReady()) {
//                mDomobInterstitialAd.showInterstitialAd(this);
//            }
        }
    }

}
