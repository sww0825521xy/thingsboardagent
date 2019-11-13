package com.honeywell.sdk.connected.connector;

import android.content.Context;

import com.honeywell.sdk.connected.callback.ConnectStatusCallback;
import com.honeywell.sdk.connected.callback.MessageCallback;
import com.honeywell.sdk.connected.callback.MessageStatusCallback;
import com.honeywell.sdk.connected.entity.ConnectOptions;


/**
 * Created by h179730 on 12/17/2018.
 */

public interface Connector {

    void setCallback(MessageCallback messageCallback);

    void connect(Context context, ConnectOptions connectOptions, ConnectStatusCallback connectStatusCallback) throws Exception;

    void send(String message, MessageStatusCallback messageStatusCallback, boolean isRetry);

    void disconnect();

    boolean isConnected();
}
