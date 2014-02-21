package com.possebom.openwifipasswordrecover.dao;

import android.content.Context;

import com.possebom.openwifipasswordrecover.model.Network;
import com.possebom.openwifipasswordrecover.parser.NetworkParser;
import com.possebom.openwifipasswordrecover.util.Utils;

import java.util.List;

/**
 * Created by alexandre on 17/02/14.
 */
public class NetworkDao {

    private static List<Network> list;

    public List<Network> getList() {
        synchronized (NetworkParser.class) {
            if (list == null) {
                list = NetworkParser.getNetworksList();
            }
            return list;
        }
    }

    public List<Network> getListWithCurrentFirst(final String ssid) {
        final List<Network> mList = getList();
        Network currNet = getNetworkBySsid(ssid);
        if (currNet != null) {
            mList.remove(currNet);
            mList.add(0, currNet);
        }
        return mList;
    }

    public List<Network> getListWithCurrentFirst(final Network network) {
        if(network == null){
            return getList();
        }
        return getListWithCurrentFirst(network.getSsid());
    }


    public Network getCurrentNetwork(final Context context){
        final String ssid = Utils.getCurrentSsid(context);
        return getNetworkBySsid(ssid);
    }

    public Network getNetworkBySsid(final String ssid) {
        Network network = null;
        if (ssid != null && ssid.length() > 0) {
            for (Network n : getList()) {
                if (n.getSsid().equals(ssid)) {
                    network = n;
                    break;
                }
            }
        }
        return network;
    }

}
