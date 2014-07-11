package com.michael.wallpaper.helper;

import android.content.Context;
import com.jesson.android.internet.InternetUtils;
import com.jesson.android.internet.core.NetWorkException;
import com.michael.wallpaper.AppConfig;
import com.michael.wallpaper.api.series.GetSeriesListRequest;
import com.michael.wallpaper.api.series.GetSeriesListResponse;
import com.michael.wallpaper.dao.model.DaoSession;
import com.michael.wallpaper.dao.model.Series;
import com.michael.wallpaper.dao.model.SeriesDao;
import com.michael.wallpaper.dao.utils.DaoUtils;
import com.michael.wallpaper.event.SeriesUpdatedEvent;
import com.michael.wallpaper.utils.AppRuntime;
import de.greenrobot.event.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangdi on 14-3-11.
 */
public class SeriesHelper {

    private static SeriesHelper mInstance = new SeriesHelper();

    private List<Series> mSeriesList = new ArrayList<Series>();

    private SeriesHelper() {

    }

    public void syncSeries(final Context context) {
        DaoSession session = DaoUtils.getDaoSession(context);
        final SeriesDao dao = session.getSeriesDao();
        // load from local database
        mSeriesList = dao.loadAll();
        if (mSeriesList == null || mSeriesList.size() == 0) {
            // 加载默认
            mSeriesList = defaultSeries();
            dao.insertInTx(mSeriesList);
        }
        mSeriesList.addAll(localSeries());

        if (AppConfig.BAIDU_SOURCE_MM_PACKAGE_NAME.endsWith(AppRuntime.PACKAGE_NAME)
            || AppConfig.CAR_PACKAGE_NAME.endsWith(AppRuntime.PACKAGE_NAME)) {
            EventBus.getDefault().post(new SeriesUpdatedEvent());
            return;
        }

        new Thread() {
            @Override
            public void run() {
                // load from server
                GetSeriesListRequest request = new GetSeriesListRequest();
                try {
                    GetSeriesListResponse response = InternetUtils.request(context, request);
                    if (response != null && response.seriesList != null && response.seriesList.size() > 0) {
                        List<Series> list = new ArrayList<Series>();
                        for (com.michael.wallpaper.api.series.Series s : response.seriesList) {
                            Series series = new Series(s.type, s.title, null, null, 1);
                            list.add(series);
                        }
                        // delete old
                        dao.deleteAll();
                        mSeriesList.clear();
                        // insert new
                        dao.insertInTx(list);
                        mSeriesList.addAll(list);
                        mSeriesList.addAll(localSeries());
                        // post event
                        EventBus.getDefault().post(new SeriesUpdatedEvent());
                    }
                } catch (NetWorkException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private List<Series> defaultSeries() {
        List<Series> list = new ArrayList<Series>();

        if (AppConfig.BAIDU_SOURCE_MM_PACKAGE_NAME.endsWith(AppRuntime.PACKAGE_NAME)) {
            list.add(new Series("小清新".hashCode(), "小清新", "美女", null, 1));
            list.add(new Series("甜素纯".hashCode(), "甜素纯", "美女", null, 1));
            list.add(new Series("清纯".hashCode(), "清纯", "美女", null, 1));
            list.add(new Series("校花".hashCode(), "校花", "美女", null, 1));
            list.add(new Series("唯美".hashCode(), "唯美", "美女", null, 1));
            list.add(new Series("气质".hashCode(), "气质", "美女", null, 1));
            list.add(new Series("足球宝贝".hashCode(), "足球宝贝", "美女", null, 0));
            list.add(new Series("嫩萝莉".hashCode(), "嫩萝莉", "美女", null, 0));
            list.add(new Series("长发".hashCode(), "长发", "美女", null, 0));
            list.add(new Series("可爱".hashCode(), "可爱", "美女", null, 0));
            list.add(new Series("素颜".hashCode(), "素颜", "美女", null, 0));
            list.add(new Series("高雅大气很有范".hashCode(), "高雅大气很有范", "美女", null, 0));
        } else if (AppConfig.CAR_PACKAGE_NAME.endsWith(AppRuntime.PACKAGE_NAME)) {
            list.add(new Series("名车".hashCode(), "名车", "汽车", null, 1));
            list.add(new Series("汽车图解".hashCode(), "汽车图解", "汽车", null, 1));
            list.add(new Series("高清壁纸".hashCode(), "高清壁纸", "汽车", null, 1));
            list.add(new Series("跑车".hashCode(), "跑车", "汽车", null, 1));
            list.add(new Series("法拉利".hashCode(), "法拉利", "汽车", null, 1));
            list.add(new Series("玛莎拉蒂".hashCode(), "玛莎拉蒂", "汽车", null, 1));
            list.add(new Series("兰博基尼".hashCode(), "兰博基尼", "汽车", null, 1));
            list.add(new Series("保时捷".hashCode(), "保时捷", "汽车", null, 0));
            list.add(new Series("宝马".hashCode(), "宝马", "汽车", null, 0));
            list.add(new Series("概念车".hashCode(), "概念车", "汽车", null, 0));
            list.add(new Series("奔驰".hashCode(), "奔驰", "汽车", null, 0));
            list.add(new Series("阿斯顿.马丁".hashCode(), "阿斯顿.马丁", "汽车", null, 0));
            list.add(new Series("老爷车".hashCode(), "老爷车", "汽车", null, 0));
            list.add(new Series("SUV越野车".hashCode(), "SUV越野车", "汽车", null, 0));
            list.add(new Series("北京车展".hashCode(), "北京车展", "汽车", null, 0));
        } else if (AppConfig.MM_WALLPAPER_PACKAGE_NAMMME.endsWith(AppRuntime.PACKAGE_NAME)) {
            list.add(new Series(1, "性感美女", null, null, 1));
            list.add(new Series(2, "岛国女友", null, null, 1));
            list.add(new Series(3, "丝袜美腿", null, null, 1));
            list.add(new Series(4, "有沟必火", null, null, 1));
            list.add(new Series(5, "有沟必火", null, null, 1));
            list.add(new Series(11, "明星美女", null, null, 1));
            list.add(new Series(12, "甜素纯", null, null, 1));
            list.add(new Series(13, "校花", null, null, 1));
        }  else if (AppConfig.BAIDU_WALLPAPER_PACKAGE_NAMMME.endsWith(AppRuntime.PACKAGE_NAME)) {
            list.add(new Series("编辑推荐".hashCode(), "编辑推荐", "壁纸", null, 1));
            list.add(new Series("世界杯".hashCode(), "世界杯", "壁纸", null, 1));
            list.add(new Series("影视".hashCode(), "影视", "壁纸", "小时代", 1));
            list.add(new Series("高清壁纸".hashCode(), "高清壁纸", "壁纸", null, 1));
            list.add(new Series("lomo风格壁纸".hashCode(), "lomo风格壁纸", "壁纸", null, 1));
            list.add(new Series("创意".hashCode(), "创意", "壁纸", "极简", 1));
            list.add(new Series("风景".hashCode(), "风景", "壁纸", "唯美意境", 1));
            list.add(new Series("夏季清凉".hashCode(), "夏季清凉", "壁纸", "唯美意境", 1));
            list.add(new Series("美女".hashCode(), "美女", "壁纸", "可爱", 1));
            list.add(new Series("美女+日韩写真".hashCode(), "美女", "壁纸", "日韩写真", 0));
            list.add(new Series("美女+清新".hashCode(), "美女", "壁纸", "清新", 0));
            list.add(new Series("美女+动漫美女".hashCode(), "美女", "壁纸", "动漫美女", 0));
            list.add(new Series("创意+广告创意".hashCode(), "创意", "壁纸", "广告创意", 0));
            list.add(new Series("创意+治愈系".hashCode(), "创意", "壁纸", "治愈系", 0));
            list.add(new Series("创意+三维立体".hashCode(), "创意", "壁纸", "三维立体", 0));
        }

        return list;
    }

    private List<Series> localSeries() {
        List<Series> list = new ArrayList<Series>();
        list.add(new Series(-1, "我的收藏", "本地", null, 1));
        if (AppConfig.MM_WALLPAPER_PACKAGE_NAMMME.endsWith(AppRuntime.PACKAGE_NAME)) {
            list.add(new Series(-2, "隐藏美女", "本地", null, 1));
        }
        list.add(new Series(-3, "更多应用", "本地", null, 0));

        return list;
    }

    public static SeriesHelper getInstance() {
        return mInstance;
    }

    public List<Series> getSeriesList() {
        return mSeriesList;
    }
}
