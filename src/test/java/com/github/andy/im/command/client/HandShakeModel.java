package com.github.andy.im.command.client;

import java.util.List;

/**
 * Created by yan.s.g on 18/2/11.
 */
public class HandShakeModel {

    private String sid;

    private List<String> upgrades;

    private int pingInterval;

    private int pingTimeout;

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public List<String> getUpgrades() {
        return upgrades;
    }

    public void setUpgrades(List<String> upgrades) {
        this.upgrades = upgrades;
    }

    public int getPingInterval() {
        return pingInterval;
    }

    public void setPingInterval(int pingInterval) {
        this.pingInterval = pingInterval;
    }

    public int getPingTimeout() {
        return pingTimeout;
    }

    public void setPingTimeout(int pingTimeout) {
        this.pingTimeout = pingTimeout;
    }
}
