package com.michael.wallpaper.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import cn.domob.android.ads.DomobAdView;
import com.jesson.android.widget.Toaster;
import com.michael.wallpaper.AppConfig;
import com.michael.wallpaper.R;
import com.michael.wallpaper.dao.model.Series;
import com.michael.wallpaper.fragment.NavigationDrawerFragment;
import com.michael.wallpaper.fragment.PhotoStreamFragment;
import com.michael.wallpaper.fragment.StaggerPhotoStreamFragment;
import com.michael.wallpaper.helper.SeriesHelper;
import com.michael.wallpaper.setting.Setting;
import com.michael.wallpaper.utils.AppRuntime;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

public class MainActivity extends BaseActivity
    implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private int mRefreshTime = 0;

    private Series mSeries;

    private boolean mAdViewShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobclickAgent.updateOnlineConfig(this.getApplicationContext());

        initBannerAd();
        initInterstitialAd();
        initAppWall();

        MobclickAgent.updateOnlineConfig(getApplicationContext());

        mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));

        String data = MobclickAgent.getConfigParams(this.getApplicationContext(), "show_banner");
        if (!TextUtils.isEmpty(data) && data.equals("true")) {
            AppRuntime.SHOW_BANNER = true;
        } else {
            AppRuntime.SHOW_BANNER = false;
        }

//        AppRuntime.SHOW_BANNER = true;
    }

    @Override
    public void onResume() {
        super.onResume();
//        tryToShwoSplashAd();
        String data = MobclickAgent.getConfigParams(this.getApplicationContext(), "show_banner");
        if (!TextUtils.isEmpty(data) && data.equals("true")) {
            AppRuntime.SHOW_BANNER = true;
        } else {
            AppRuntime.SHOW_BANNER = false;
        }

        if (!mAdViewShow) {
            initBannerAd();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        OffersManager.getInstance(getApplicationContext()).onAppExit();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        List<Series> seriesList = SeriesHelper.getInstance().getSeriesList();
        if (position < 0 || position > seriesList.size() - 1) {
            return;
        }
        mSeries = seriesList.get(position);
        if (mSeries.getType() == -3) {
//            OffersManager.getInstance(this).showOffersWall();
//            Ads.showAppWall(MainActivity.this, AppConfig.WANDOUJIA_APP_WALL);
        } else if (mSeries.getType() == -2) {
            if (Setting.getInstace().getMode() != AppConfig.SERIES_MODE) {
                Toaster.show(getApplicationContext(), "已经开启");
                return;
            }
//            int point = PointsManager.getInstance(this).queryPoints();
//            if (point < 60) {
//                showWallInfoDialog(point);
//            } else {
                Setting.getInstace().setMode(1);
                Toaster.show(getApplicationContext(), "开启隐藏成功，请退出应用重新进入");
//            }
        } else {
            //检查是否积分已购
//            if (mOpenWall && mSeries.getProperty() == 0) {
//                隐藏属性
//                int point = PointsManager.getInstance(this).queryPoints();
//                if (point < 60) {
//                    showWallInfoDialog(point);
//                    return;
//                }
//            }

            // update the main content by replacing fragments
            FragmentManager fragmentManager = getFragmentManager();
            String tag = String.valueOf(mSeries.getType());
            Fragment fragment = fragmentManager.findFragmentByTag(tag);
            if (fragment == null) {
                fragment = AppRuntime.useStaggerGridView()
                               ? StaggerPhotoStreamFragment.newInstance(mSeries)
                               : PhotoStreamFragment.newInstance(mSeries);
            }
            fragmentManager.beginTransaction().replace(R.id.container, fragment, tag).commit();
        }
    }

    public void onSectionAttached(Series series) {
        if (!TextUtils.isEmpty(series.getTag3())) {
            mTitle = series.getTitle() + "-" + series.getTag3();
        } else {
            mTitle = series.getTitle();
        }
    }



    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
        actionBar.setIcon(getIconResByPackageName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.main, menu);
            MenuItem item = menu.findItem(R.id.action_refresh);
            if (mSeries != null && mSeries.getType() < 0) {
                item.setVisible(false);
            } else {
                item.setVisible(true);
            }
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            startActivity(new Intent(this, SettingActivity.class));
            return true;
        } else if (item.getItemId() == R.id.action_feedback) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initBannerAd() {
        if (!AppRuntime.SHOW_BANNER) {
            return;
        }

        if (AppConfig.GOOLE_AD_ENABLE) {
        } else if (AppConfig.DOMOD_AD_ENABLE) {
            DomobAdView adview = new DomobAdView(this, AppConfig.DOMOD_PUBLISH_KEY, AppConfig.DOMOD_PLACEMENT_KEY);
            RelativeLayout layout = (RelativeLayout) findViewById(R.id.ad_content);
            layout.addView(adview, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                                                                      RelativeLayout.LayoutParams.WRAP_CONTENT));
            mAdViewShow = true;
        }
    }

    private void initInterstitialAd() {
        initSplashAd();
    }

    private void initAppWall() {
//        GdtAppwall.init(this, AppConfig.GDT_AD_APPID, AppConfig.GDT_AD_APPWALL_POSID, AppConfig.DEBUG);
    }

    public void onRefresh() {
        mRefreshTime += 1;
        if (mRefreshTime % 5 == 0) {
            tryToShwoSplashAd();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            tryToShwoSplashAd();
        }

        if (!mAdViewShow) {
            initBannerAd();
        }
    }

//    private void showWallInfoDialog(int currentUserPoint) {
//        String tips = String.format(getString(R.string.offer_info_detail), 60, currentUserPoint);
//        View view = this.getLayoutInflater().inflate(R.layout.offer_tips_view, null);
//        TextView tv = (TextView) view.findViewById(R.id.tips);
//        tv.setText(tips);
//        AlertDialog dialog = new AlertDialog.Builder(this).setTitle(R.string.tips_title).setView(view)
//                                 .setPositiveButton(R.string.download_offer, new DialogInterface.OnClickListener() {
//
//                                     @Override
//                                     public void onClick(DialogInterface dialog, int which) {
//                                         OffersManager.getInstance(MainActivity.this).showOffersWall();
//                                     }
//                                 })
//                                 .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//
//                                     @Override
//                                     public void onClick(DialogInterface dialog, int which) {
//                                     }
//                                 }).create();
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.show();
//    }

}