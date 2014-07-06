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

        if (AppConfig.BAIDU_SOURCE_MM_PACKAGE_NAME.endsWith(AppRuntime.PACKAGE_NAME)) {
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
                            Series series = new Series(s.type, s.title, null, 1);
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
            list.add(new Series("小清新".hashCode(), "小清新", "美女", 1));
            list.add(new Series("甜素纯".hashCode(), "甜素纯", "美女", 1));
            list.add(new Series("清纯".hashCode(), "清纯", "美女", 1));
            list.add(new Series("校花".hashCode(), "校花", "美女", 1));
            list.add(new Series("唯美".hashCode(), "唯美", "美女", 1));
            list.add(new Series("气质".hashCode(), "气质", "美女", 1));
            list.add(new Series("足球宝贝".hashCode(), "足球宝贝", "美女", 0));
            list.add(new Series("嫩萝莉".hashCode(), "嫩萝莉", "美女", 0));
            list.add(new Series("长发".hashCode(), "长发", "美女", 0));
            list.add(new Series("可爱".hashCode(), "可爱", "美女", 0));
            list.add(new Series("素颜".hashCode(), "素颜", "美女", 0));
            list.add(new Series("高雅大气很有范".hashCode(), "高雅大气很有范", "美女", 0));
        } else if (AppConfig.CAR_PACKAGE_NAME.endsWith(AppRuntime.PACKAGE_NAME)) {
            Series series11 = new Series(1001, "兰博基尼", null, 1);
            list.add(series11);
            Series series12 = new Series(1002, "宝马", null, 1);
            list.add(series12);
            Series series13 = new Series(1003, "玛莎拉蒂", null, 1);
            list.add(series13);
            Series series14 = new Series(1004, "奔驰", null, 1);
            list.add(series14);
            Series series15 = new Series(1005, "法拉利", null, 1);
            list.add(series15);
            Series series16 = new Series(1006, "迈凯轮", null, 1);
            list.add(series16);
        }

//        if (AppConfig.SERIES_MODE == 1) {
//            Series series1 = new Series(1, "性感美女");
//            list.add(series1);
//            Series series2 = new Series(2, "岛国女友");
//            list.add(series2);
//            Series series3 = new Series(3, "丝袜美腿");
//            list.add(series3);
//            Series series4 = new Series(4, "有沟必火");
//            list.add(series4);
//            Series series5 = new Series(5, "有沟必火");
//            list.add(series5);
//            Series series11 = new Series(11, "明星美女");
//            list.add(series11);
//            Series series12 = new Series(12, "甜素纯");
//            list.add(series12);
//            Series series13 = new Series(13, "校花");
//            list.add(series13);
//        } else {
//            Series series11 = new Series(1001, "兰博基尼");
//            list.add(series11);
//            Series series12 = new Series(1002, "宝马");
//            list.add(series12);
//            Series series13 = new Series(1003, "玛莎拉蒂");
//            list.add(series13);
//            Series series14 = new Series(1004, "奔驰");
//            list.add(series14);
//            Series series15 = new Series(1005, "法拉利");
//            list.add(series15);
//            Series series16 = new Series(1006, "迈凯轮");
//            list.add(series16);
//        }

        return list;
    }

    private List<Series> localSeries() {
        List<Series> list = new ArrayList<Series>();
        Series collect = new Series(-1, "我的收藏", "本地", 1);
        list.add(collect);
        Series seriesApp = new Series(-3, "更多应用", "本地", 0);
        list.add(seriesApp);

        return list;
    }

    public static SeriesHelper getInstance() {
        return mInstance;
    }

    public List<Series> getSeriesList() {
        return mSeriesList;
    }
}
