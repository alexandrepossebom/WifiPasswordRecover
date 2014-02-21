package com.possebom.openwifipasswordrecover;

import android.app.Application;
import android.content.Context;

import com.possebom.openwifipasswordrecover.dao.NetworkDao;
import com.possebom.openwifipasswordrecover.model.Network;

import java.util.List;

/**
 * Created by alexandre on 19/02/14.
 */
public class MyApplication extends Application {
    private static List<Network> list;
    private static Network mNetwork;
    private static Context mContext;
    private static NetworkDao mNetworkDao;

    @Override
    public void onCreate() {
        super.onCreate();
        synchronized (MyApplication.class) {
            if (mContext == null) {
                mContext = getApplicationContext();
            }
            if (mNetworkDao == null) {
                mNetworkDao = new NetworkDao();
            }
        }
        getList();
    }

    public static List<Network> getList() {
        synchronized (MyApplication.class) {
            if (list == null) {
                list = mNetworkDao.getListWithCurrentFirst(getCurrentNetwork());
            }
            return list;
        }
    }

    public static Network getCurrentNetwork() {
        synchronized (MyApplication.class) {
            if (mNetwork == null) {
                mNetwork = mNetworkDao.getCurrentNetwork(mContext);
            }
            return mNetwork;
        }
    }
}
