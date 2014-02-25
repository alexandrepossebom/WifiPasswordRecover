/*
 * Copyright Alexandre Possebom
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.possebom.openwifipasswordrecover.parser;

import android.content.Context;
import android.os.AsyncTask;

import com.possebom.openwifipasswordrecover.interfaces.NetworkListener;
import com.possebom.openwifipasswordrecover.model.Network;
import com.possebom.openwifipasswordrecover.util.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by alexandre on 17/02/14.
 */
public class NetworkParser extends AsyncTask <Void, Void, List<Network> >{

    private final Context context;
    private final NetworkListener listener;

    public NetworkParser(final Context context,final NetworkListener listener) {
        this.listener = listener;
        this.context = context;
    }

    @Override
    protected void onPostExecute(List<Network> networkList) {
        super.onPostExecute(networkList);
        listener.onParserDone(networkList);
    }

    @Override
    protected List<Network> doInBackground(Void... params) {

        final String currNetwork = Utils.getCurrentSsid(context);
        final List<Network> listNetworks = new ArrayList<Network>();
        final String content = readFile();

        final Pattern patternNetworks = Pattern.compile("network=\\{(.*?)\\}", Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
        final Pattern patternWpa = Pattern.compile("ssid=\"(.*?)\".*psk=\"(.*?)\"", Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
        final Pattern patternWep = Pattern.compile("ssid=\"(.*?)\".*wep_key.=\"(.*?)\"", Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);

        Matcher matcherNetworks = patternNetworks.matcher(content);
        Matcher matcherWpa;
        Matcher matcherWep;

        while (matcherNetworks.find()) {
            final String stringNetwork = matcherNetworks.group(1);
            matcherWpa = patternWpa.matcher(stringNetwork);
            matcherWep = patternWep.matcher(stringNetwork);

            boolean connected = false;
            if (matcherWpa.find() && matcherWpa.groupCount() == 2) {
                if(currNetwork != null && currNetwork.equals(matcherWpa.group(1))){
                    connected = true;
                }
                final Network network = new Network(matcherWpa.group(1), matcherWpa.group(2),connected, "wpa");
                listNetworks.add(network);
            } else if (matcherWep.find() && matcherWep.groupCount() == 2) {
                if(currNetwork != null && currNetwork.equals(matcherWep.group(1))){
                    connected = true;
                }
                final Network network = new Network(matcherWep.group(1), matcherWep.group(2),connected, "wep");
                listNetworks.add(network);
            }
        }

        Collections.sort(listNetworks);

        return listNetworks;
    }

    private String readFile() {
        String ret;
        try {
            Process process = Runtime.getRuntime().exec("su -c cat /data/misc/wifi/wpa_supplicant.conf");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            int read;
            char[] buffer = new char[4096];
            final StringBuffer output = new StringBuffer();
            while ((read = reader.read(buffer)) > 0) {
                output.append(buffer, 0, read);
            }
            reader.close();
            process.waitFor();
            ret = output.toString();
        } catch (IOException e) {
            ret = "";
        } catch (InterruptedException e) {
            ret = "";
        }
        return ret;
    }

}