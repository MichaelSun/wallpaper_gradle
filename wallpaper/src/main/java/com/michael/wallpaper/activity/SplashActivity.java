package com.michael.wallpaper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import com.michael.wallpaper.R;
import com.michael.wallpaper.helper.SeriesHelper;

/**
 * Created by zhangdi on 14-3-8.
 */
public class SplashActivity extends BaseActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();

    private long mStartTime = 0;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initSplashAd();

        mStartTime = System.currentTimeMillis();

        SeriesHelper.getInstance().syncSeries(this);

        enterMain();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void enterMain() {
        long endTime = System.currentTimeMillis();
        long delay = 500 - (endTime - mStartTime);
        if (delay <= 0) {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        } else {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }
            }, delay);
        }
    }
}
