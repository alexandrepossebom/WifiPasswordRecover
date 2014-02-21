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

package com.possebom.openwifipasswordrecover.model;

/**
 * Created by alexandre on 17/02/14.
 */

public class Network implements Comparable<Network> {
    private String ssid = "";
    private String password = "";
    private String type = "";

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Network(final String ssid, final String password, final String type) {
        this.ssid = ssid;
        this.password = password;
        this.type = type;
    }

    @Override
    public int compareTo(Network o) {
        if(o ==null ) {return -1;}
        return ssid.compareToIgnoreCase(o.ssid);
    }

    @Override
    public String toString() {
        return String.format("ssid : %s\npass : %s\ntype : %s", ssid, password, type);
    }
}
