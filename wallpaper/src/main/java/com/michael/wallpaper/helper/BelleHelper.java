package com.michael.wallpaper.helper;

import android.content.Context;
import android.text.TextUtils;
import com.jesson.android.internet.InternetUtils;
import com.jesson.android.internet.core.NetWorkException;
import com.michael.wallpaper.AppConfig;
import com.michael.wallpaper.api.baidu.BaiduListRequest;
import com.michael.wallpaper.api.baidu.BaiduListResponse;
import com.michael.wallpaper.api.belle.*;
import com.michael.wallpaper.dao.model.DaoSession;
import com.michael.wallpaper.dao.model.LocalBelle;
import com.michael.wallpaper.dao.model.LocalBelleDao;
import com.michael.wallpaper.dao.utils.DaoUtils;
import com.michael.wallpaper.event.GetBelleListEvent;
import com.michael.wallpaper.event.NetworkErrorEvent;
import com.michael.wallpaper.event.ServerErrorEvent;
import com.michael.wallpaper.utils.AppRuntime;
import de.greenrobot.event.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangdi on 14-3-7.
 */
public class BelleHelper {

    private Context mContext;

    private DaoSession mSession;

    public BelleHelper(Context context) {
        mContext = context.getApplicationContext();
        mSession = DaoUtils.getDaoSession(mContext);
    }

    public void getBelleListFromServer(final int type, final int id, final int count) {
        new Thread() {
            @Override
            public void run() {
                try {
                    GetBelleListRequest request = new GetBelleListRequest();
                    request.type = type;
                    request.id = id;
                    request.count = count;
                    GetBelleListResponse response = InternetUtils.request(mContext, request);
                    if (response != null) {
                        GetBelleListEvent event = new GetBelleListEvent();
                        event.belles = response.belles;
                        event.hasMore = response.hasMore;
                        if (id <= 0) {
                            event.type = GetBelleListEvent.TYPE_SERVER;
                            // update local database
                            LocalBelleDao dao = mSession.getLocalBelleDao();
                            // delete old
                            dao.queryBuilder().where(LocalBelleDao.Properties.Type.eq(type)).buildDelete().forCurrentThread().executeDeleteWithoutDetachingEntities();
                            // insert new
                            if (response.belles != null) {
                                List<LocalBelle> localBelles = new ArrayList<LocalBelle>();
                                for (Belle belle : response.belles) {
                                    LocalBelle localBelle = new LocalBelle(belle.id, belle.time, belle.type, belle.desc, belle.url, belle.rawUrl);
                                    localBelles.add(localBelle);
                                }
                                dao.insertOrReplaceInTx(localBelles);
                            }
                        } else {
                            event.type = GetBelleListEvent.TYPE_SERVER_MORE;
                        }
                        EventBus.getDefault().post(event);
                    } else {
                        EventBus.getDefault().post(new ServerErrorEvent());
                    }
                } catch (NetWorkException e) {
                    e.printStackTrace();
                    EventBus.getDefault().post(new NetworkErrorEvent(e));
                }
            }
        }.start();
    }

    /**
     * 随机从服务器获取
     *
     * @param type
     * @param count
     */
    public void randomGetBelleListFromServer(final int type, final int startNum, final int count, final String category, final String title, final String tag3) {
        new Thread() {
            @Override
            public void run() {
                if (AppRuntime.PACKAGE_NAME.endsWith(AppConfig.BAIDU_SOURCE_MM_PACKAGE_NAME)) {
                    getBaiduItems(startNum, count, category, title, tag3);
                } else if (AppRuntime.PACKAGE_NAME.endsWith(AppConfig.CAR_PACKAGE_NAME)) {
//                    getNoramlItems(type, count);
                    getBaiduItems(startNum, count, category, title, tag3);
                } else if (AppRuntime.PACKAGE_NAME.endsWith(AppConfig.MM_WALLPAPER_PACKAGE_NAMMME)) {
                    getNoramlItems(type, count);
                } else if (AppRuntime.PACKAGE_NAME.endsWith(AppConfig.BAIDU_WALLPAPER_PACKAGE_NAMMME)) {
                    getBaiduItems(startNum, count, category, title, tag3);
                } else if (AppRuntime.PACKAGE_NAME.endsWith(AppConfig.GAOXIAO_WALLPAPER_PACKAGE_NAMMME)) {
                    getBaiduItems(startNum, count, category, title, tag3);
                }
            }
        }.start();
    }

    private void getNoramlItems(int type, int count) {
        try {
            long startTime = System.currentTimeMillis();

            RandomGetBelleListRequest request = new RandomGetBelleListRequest();
            request.type = type;
            request.count = count;
            RandomGetBelleListResponse response = InternetUtils.request(mContext, request);
            if (response != null) {
                GetBelleListEvent event = new GetBelleListEvent();
                event.belles = response.belles;
                event.type = GetBelleListEvent.TYPE_SERVER_RANDOM;
                event.hasMore = false;
                // update local database
                LocalBelleDao dao = mSession.getLocalBelleDao();
                // delete old
                dao.queryBuilder().where(LocalBelleDao.Properties.Type.eq(type)).buildDelete().forCurrentThread().executeDeleteWithoutDetachingEntities();
                // insert new
                if (response.belles != null) {
                    List<LocalBelle> localBelles = new ArrayList<LocalBelle>();
                    for (Belle belle : response.belles) {
                        if (TextUtils.isEmpty(belle.rawUrl)) {
                            belle.rawUrl = belle.url;
                        }
                        LocalBelle localBelle = new LocalBelle(belle.id, belle.time, belle.type, belle.desc, belle.url, belle.rawUrl);
                        localBelles.add(localBelle);
                    }
                    dao.insertOrReplaceInTx(localBelles);
                }

                long endTime = System.currentTimeMillis();
                long delay = 1000 - (endTime - startTime);
                if (delay > 0) {
                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                EventBus.getDefault().post(event);
            } else {
                EventBus.getDefault().post(new ServerErrorEvent());
            }
        } catch (NetWorkException e) {
            e.printStackTrace();
            EventBus.getDefault().post(new NetworkErrorEvent(e));
        }
    }

    private void getBaiduItems(int pageNum, int pageCount, String category, String title, String tag3) {
        try {
            BaiduListRequest request = new BaiduListRequest(pageNum, pageCount, category, title, tag3);
            BaiduListResponse response = InternetUtils.request(mContext, request);
            if (response != null) {

//                Log.d("BellWallpaper", "data response = " + response.toString());

                long startTime = System.currentTimeMillis();

                List<Belle> belles = BaiduListResponse.makeBellesFromBaiduItem(response.baiduItems);
                GetBelleListEvent event = new GetBelleListEvent();
                event.belles = belles;
                event.hasMore = response.totalNum > (response.start_index + response.return_number);
                event.pageCount = response.return_number;
                event.startIndex = response.start_index;
                event.type = GetBelleListEvent.TYPE_SERVER_RANDOM;
                // update local database
                LocalBelleDao dao = mSession.getLocalBelleDao();
                // delete old
                dao.queryBuilder().where(LocalBelleDao.Properties.Type.eq(2000)).buildDelete().forCurrentThread().executeDeleteWithoutDetachingEntities();
                // insert new
                if (event.belles != null) {
                    List<LocalBelle> localBelles = new ArrayList<LocalBelle>();
                    for (Belle belle : event.belles) {
                        LocalBelle localBelle = new LocalBelle(belle.id, belle.time, title.hashCode(), belle.desc, belle.url, belle.rawUrl);
                        localBelles.add(localBelle);
                    }
                    dao.insertOrReplaceInTx(localBelles);
                }

                long endTime = System.currentTimeMillis();
                long delay = 1000 - (endTime - startTime);
                if (delay > 0) {
                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                EventBus.getDefault().post(event);
            } else {
                EventBus.getDefault().post(new ServerErrorEvent());
//                Log.d("BellWallpaper", "data response = null");
            }
        } catch (NetWorkException e) {
            e.printStackTrace();
            EventBus.getDefault().post(new NetworkErrorEvent(e));
        }
    }

    public void getBelleListFromLocal(final int type) {
        new Thread() {
            @Override
            public void run() {
                LocalBelleDao dao = mSession.getLocalBelleDao();
                List<LocalBelle> localList = dao.queryBuilder().where(LocalBelleDao.Properties.Type.eq(type))
                                                 .build().forCurrentThread().list();
                List<Belle> belles = null;
                if (localList != null) {
                    belles = new ArrayList<Belle>();
                    for (LocalBelle localBelle : localList) {
                        Belle belle = new Belle(localBelle.getId(), localBelle.getTime(), localBelle.getType(), localBelle.getDesc()
                                                   , localBelle.getUrl(), localBelle.getRawUrl());
                        belles.add(belle);
                    }
                }
                GetBelleListEvent event = new GetBelleListEvent();
                event.type = GetBelleListEvent.TYPE_LOCAL;
                event.belles = belles;
                event.hasMore = false;
                EventBus.getDefault().post(event);
            }
        }.start();
    }

}
