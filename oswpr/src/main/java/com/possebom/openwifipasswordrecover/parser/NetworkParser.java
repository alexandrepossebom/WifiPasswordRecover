package com.possebom.openwifipasswordrecover.parser;

import com.possebom.openwifipasswordrecover.model.Network;

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
public final class NetworkParser {

    private NetworkParser(){

    }

    public static List<Network> getNetworksList() {
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

            if (matcherWpa.find() && matcherWpa.groupCount() == 2) {
                final Network network = new Network(matcherWpa.group(1), matcherWpa.group(2), "wpa");
                listNetworks.add(network);
            } else if (matcherWep.find() && matcherWep.groupCount() == 2) {
                final Network network = new Network(matcherWep.group(1), matcherWep.group(2), "wep");
                listNetworks.add(network);
            }
        }

        Collections.sort(listNetworks);

        return listNetworks;
    }

    private static String readFile() {
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