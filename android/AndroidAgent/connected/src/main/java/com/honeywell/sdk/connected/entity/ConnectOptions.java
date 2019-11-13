package com.honeywell.sdk.connected.entity;

import javax.net.SocketFactory;

/**
 * Created by h179730 on 12/19/2018.
 */

public class ConnectOptions {

    public static final int CONNECTION_TIMEOUT_DEFAULT = 30;

    private int connectionTimeout = CONNECTION_TIMEOUT_DEFAULT;

    private String userName;

    private char[] password;

    private SocketFactory socketFactory;

    private boolean automaticReconnect = false;

    private String url;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public char[] getPassword() {
        return password;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }

    public SocketFactory getSocketFactory() {
        return socketFactory;
    }

    public void setSocketFactory(SocketFactory socketFactory) {
        this.socketFactory = socketFactory;
    }

    public boolean isAutomaticReconnect() {
        return automaticReconnect;
    }

    public void setAutomaticReconnect(boolean automaticReconnect) {
        this.automaticReconnect = automaticReconnect;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }
}
