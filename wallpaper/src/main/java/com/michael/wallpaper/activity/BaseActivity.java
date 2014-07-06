package com.michael.wallpaper.activity;

import android.app.Activity;
import cn.domob.android.ads.DomobInterstitialAd;
import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.google.ads.InterstitialAd;
import com.michael.wallpaper.AppConfig;

/**
 * Created by zhangdi on 14-3-5.
 */
public class BaseActivity extends Activity {

    protected AdView mAdView;

    protected InterstitialAd interstitial;

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
        if (mAdView != null) {
            mAdView.destroy();
        }
    }

    protected void initSplashAd() {
        if (AppConfig.GOOLE_AD_ENABLE) {
            // 制作插页式广告。
            interstitial = new InterstitialAd(this, "a15368dc3248e7e");
            // 创建广告请求。
            AdRequest adRequest = new AdRequest();
            // 开始加载插页式广告。
            interstitial.loadAd(adRequest);
        } else if (AppConfig.DOMOD_AD_ENABLE) {
            mDomobInterstitialAd = new DomobInterstitialAd(this, "56OJwdKYuNB/ECRykc", "16TLuqyaApjJ1NUEzQfGknUs", DomobInterstitialAd.INTERSITIAL_SIZE_FULL_SCREEN);
            mDomobInterstitialAd.loadInterstitialAd();
        }
    }

    protected void tryToShwoSplashAd() {
        if (AppConfig.GOOLE_AD_ENABLE) {
            if (interstitial != null && interstitial.isReady()) {
                interstitial.show();
            }
        } else if (AppConfig.DOMOD_AD_ENABLE) {
            if (mDomobInterstitialAd != null && mDomobInterstitialAd.isInterstitialAdReady()) {
                mDomobInterstitialAd.showInterstitialAd(this);
            }
        }
    }

}
