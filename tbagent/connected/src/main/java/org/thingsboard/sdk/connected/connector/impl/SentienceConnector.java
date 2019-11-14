package org.thingsboard.sdk.connected.connector.impl;

import org.thingsboard.sdk.connected.callback.ConnectStatusCallback;
import org.thingsboard.sdk.connected.callback.MessageCallback;
import org.thingsboard.sdk.connected.callback.MessageStatusCallback;
import org.thingsboard.sdk.connected.connector.Connector;
import org.thingsboard.sdk.connected.entity.ConnectOptions;

/**
 * Created by h179730 on 12/17/2018.
 */

public class SentienceConnector implements Connector {

    private static final String TAG = "SentienceConnector";

    private String uri;

    private MessageCallback messageCallback;

    private ConnectStatusCallback connectStatusCallback;

    private MessageStatusCallback messageStatusCallback;

    public boolean isServerConnected;

    @Override
    public void setCallback(MessageCallback messageCallback) {

    }

    @Override
    public void connect(ConnectOptions connectOptions, ConnectStatusCallback connectStatusCallback) {

    }

    @Override
    public void send(String message, MessageStatusCallback messageStatusCallback, boolean isRetry) {

    }

    @Override
    public void disconnect() {

    }

    @Override
    public boolean isConnected() {
        return isServerConnected;
    }
}
