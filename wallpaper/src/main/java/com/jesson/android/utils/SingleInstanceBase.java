/**
 * SingleInstanceBase.java
 */
package com.jesson.android.utils;

import android.content.Context;
import android.util.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Guoqing Sun Nov 5, 20129:09:50 PM
 */
public abstract class SingleInstanceBase {

    private static final boolean DEBUG = false;

    public synchronized static final <T> T getInstance(Class<T> classType) {
        if (classType == null) {
            throw new IllegalArgumentException("argument is null");
        }

        SingleInstanceBase ret = SingleInstanceManager.getInstance().getInstance(classType);
        if (ret == null) {
            ret = SingleInstanceManager.getInstance().makeNewInstance(classType);
            if (ret != null) {
                SingleInstanceManager.getInstance().putInstace(classType, ret);
            }
        }

        if (ret == null) {
            throw new IllegalArgumentException("create new instance for " + classType + " error");
        }

        return (T) ret;
    }

    protected SingleInstanceBase() {
    }

    abstract protected void init(Context context);

    /**
     * manager
     * 
     * @author Guoqing Sun Nov 6, 20122:05:39 PM
     */
    public static class SingleInstanceManager {

        private static final String TAG = "SingleInstanceManager";

        private static SingleInstanceManager gSingleInstanceManager;

        private static Object obj = new Object();

        private Context mContext;

        private HashMap<Class, SingleInstanceBase> mSingleInstanceCache;

        public static final SingleInstanceManager getInstance() {
            if (gSingleInstanceManager == null) {
                synchronized (obj) {
                    if (gSingleInstanceManager == null) {
                        gSingleInstanceManager = new SingleInstanceManager();
                    }
                }
            }

            return gSingleInstanceManager;
        }

        public void init(Context context) {
            mContext = context.getApplicationContext();
        }

        private SingleInstanceManager() {
            mSingleInstanceCache = new HashMap<Class, SingleInstanceBase>();
        }

        public SingleInstanceBase getInstance(Class classType) {
            if (mSingleInstanceCache.containsKey(classType)) {
                return mSingleInstanceCache.get(classType);
            }

            return null;
        }

        public void putInstace(Class classType, SingleInstanceBase obj) {
            if (classType != null && obj != null) {
                mSingleInstanceCache.put(classType, obj);
            }
        }

        public void clearInstance(Class classType) {
            if (classType != null) {
                mSingleInstanceCache.remove(classType);
            }
        }

        public void clearAllInstance() {
            mSingleInstanceCache.clear();
        }

        public ArrayList<SingleInstanceBase> getAllInstance() {
            ArrayList<SingleInstanceBase> ret = new ArrayList<SingleInstanceBase>();
            ret.addAll(mSingleInstanceCache.values());

            return ret;
        }

        public SingleInstanceBase makeNewInstance(Class classType) {
            if (classType == null) {
                return null;
            }

            try {
                if (DEBUG) {
                    Log.d(TAG, "[[makeNewInstance]] class name = " + classType.getName());
                }

                // Class classObj = Class.forName(classType.getName());
                Constructor<?> c = classType.getDeclaredConstructor();

                if (DEBUG) {
                    Log.d(TAG, "[[makeNewInstance]] Constructor = " + c.toGenericString() + " accessible = " + c.isAccessible());
                }

                c.setAccessible(true);

                if (DEBUG) {
                    Log.d(TAG, "[[makeNewInstance]] Constructor changed = " + c.isAccessible());
                }

                Object obj = c.newInstance();
                if (DEBUG) {
                    Log.d(TAG, "[[makeNewInstance]] create obj = " + obj + " SingleInstanceBase is " + (obj instanceof SingleInstanceBase));
                }

                if ((obj != null) && (obj instanceof SingleInstanceBase)) {
                    Method m = classType.getDeclaredMethod("init", Context.class);
                    if (m != null) {
                        m.setAccessible(true);
                        m.invoke(obj, mContext);
                    } else {
                        throw new IllegalArgumentException("can't find init(Context context) method");
                    }

                    return (SingleInstanceBase) obj;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        public void dump() {
            if (DEBUG) {
                Log.d(TAG, "============= info for SingleInstanceManager ===========");
                for (Class c : mSingleInstanceCache.keySet()) {
                    Log.d(TAG, "    key : " + c.getName() + " value = " + mSingleInstanceCache.get(c).toString());
                }
                Log.d(TAG, "<<<<<<<<<<<<< info for SingleInstanceManager >>>>>>>>>>>");
            }
        }
    }

}
