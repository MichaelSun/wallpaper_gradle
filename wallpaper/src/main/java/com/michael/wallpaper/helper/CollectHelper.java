package com.michael.wallpaper.helper;

import android.content.Context;
import com.michael.wallpaper.api.belle.Belle;
import com.michael.wallpaper.dao.model.CollectedBelle;
import com.michael.wallpaper.dao.model.CollectedBelleDao;
import com.michael.wallpaper.dao.model.DaoSession;
import com.michael.wallpaper.dao.utils.DaoUtils;

import java.util.Hashtable;
import java.util.List;

/**
 * Created by zhangdi on 14-3-9.
 */
public class CollectHelper {

    private Context mContext;

    private DaoSession mSession;

    private Hashtable<String, Boolean> mCollectHashtable = new Hashtable<String, Boolean>();

    public CollectHelper(Context context) {
        mContext = context.getApplicationContext();

        mSession = DaoUtils.getDaoSession(mContext);
        List<CollectedBelle> belles = loadAll();
        if (belles != null) {
            for (CollectedBelle belle : belles) {
                mCollectHashtable.put(belle.getUrl(), true);
            }
        }
    }

    public void collectBelle(Belle belleC) {
        DaoSession session = DaoUtils.getDaoSession(mContext);
        CollectedBelleDao dao = session.getCollectedBelleDao();
        CollectedBelle belle = new CollectedBelle(belleC.url, System.currentTimeMillis(), belleC.rawUrl, belleC.thumb_large_width, belleC.thumb_large_height);
        dao.insertOrReplace(belle);
        mCollectHashtable.put(belleC.url, true);
    }

    public void cancelCollectBelle(String url) {
        DaoSession session = DaoUtils.getDaoSession(mContext);
        CollectedBelleDao dao = session.getCollectedBelleDao();
        dao.deleteByKey(url);
        if (mCollectHashtable.containsKey(url)) {
            mCollectHashtable.put(url, false);
        }
    }

    public boolean isCollected(String url) {
        if (mCollectHashtable.containsKey(url) && mCollectHashtable.get(url).booleanValue()) {
            return true;
        }
        return false;
    }

    public List<CollectedBelle> loadAll() {
        CollectedBelleDao dao = mSession.getCollectedBelleDao();
        return dao.loadAll();
    }
}
