package com.michael.wallpaper.activity;

import android.support.v4.app.FragmentActivity;
import cn.domob.android.ads.DomobAdManager;
import cn.domob.android.ads.DomobInterstitialAd;
import cn.domob.android.ads.DomobInterstitialAdListener;
import com.michael.wallpaper.AppConfig;
import com.michael.wallpaper.R;
import com.michael.wallpaper.utils.AppRuntime;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by zhangdi on 14-3-5.
 */
public class BaseActivity extends FragmentActivity {

    protected DomobInterstitialAd mDomobInterstitialAd;

    @Override
    protected void onResume() {
        super.onResume();

        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected int getIconResByPackageName() {
        if (AppRuntime.PACKAGE_NAME.endsWith(AppConfig.BAIDU_SOURCE_MM_PACKAGE_NAME)) {
            return R.drawable.ic_launcher;
        } else if (AppRuntime.PACKAGE_NAME.endsWith(AppConfig.CAR_PACKAGE_NAME)) {
            return R.drawable.ic_lanucher_car;
        } else if (AppRuntime.PACKAGE_NAME.endsWith(AppConfig.MM_WALLPAPER_PACKAGE_NAMMME)) {
            return R.drawable.ic_wallpaper;
        } else if (AppRuntime.PACKAGE_NAME.endsWith(AppConfig.BAIDU_WALLPAPER_PACKAGE_NAMMME)) {
            return R.drawable.ic_baidu_wallpaper;
        } else if (AppRuntime.PACKAGE_NAME.endsWith(AppConfig.GAOXIAO_WALLPAPER_PACKAGE_NAMMME)) {
            return R.drawable.icon_gaoxiao;
        }

        return R.drawable.ic_launcher;
    }

    protected void initSplashAd() {
        if (AppConfig.GOOLE_AD_ENABLE) {
        } else if (AppConfig.DOMOD_INSTER_ENABLE) {
            mDomobInterstitialAd = new DomobInterstitialAd(this, AppConfig.DOMOD_PUBLISH_KEY, AppConfig.DOMOD_INSTER_KEY, DomobInterstitialAd.INTERSITIAL_SIZE_FULL_SCREEN);
            mDomobInterstitialAd.setInterstitialAdListener(new DomobInterstitialAdListener() {
                @Override
                public void onInterstitialAdReady() {

                }

                @Override
                public void onInterstitialAdFailed(DomobAdManager.ErrorCode errorCode) {

                }

                @Override
                public void onInterstitialAdPresent() {

                }

                @Override
                public void onInterstitialAdDismiss() {
                    mDomobInterstitialAd.loadInterstitialAd();
                }

                @Override
                public void onLandingPageOpen() {

                }

                @Override
                public void onLandingPageClose() {

                }

                @Override
                public void onInterstitialAdLeaveApplication() {

                }

                @Override
                public void onInterstitialAdClicked(DomobInterstitialAd domobInterstitialAd) {

                }
            });

            mDomobInterstitialAd.loadInterstitialAd();
        }
    }

    protected void tryToShwoSplashAd() {
        if (AppConfig.GOOLE_AD_ENABLE) {
        } else if (AppConfig.DOMOD_INSTER_ENABLE) {
            if (mDomobInterstitialAd != null && mDomobInterstitialAd.isInterstitialAdReady()) {
                mDomobInterstitialAd.showInterstitialAd(this);
            }
        }
// else if (AppConfig.WANDOUJIA_INSTER_ENABLE) {
//            Ads.showAppWidget(this, null, AppConfig.WANDOUJIA_INSTER_KEY, Ads.ShowMode.FULL_SCREEN);
//        }
    }

}
