package org.thingsboard.sdk.connected.connector;

import org.thingsboard.sdk.connected.callback.ConnectStatusCallback;
import org.thingsboard.sdk.connected.callback.MessageCallback;
import org.thingsboard.sdk.connected.callback.MessageStatusCallback;
import org.thingsboard.sdk.connected.entity.ConnectOptions;


/**
 * Created by h179730 on 12/17/2018.
 */

public interface Connector {

    void setCallback(MessageCallback messageCallback);

    void connect(ConnectOptions connectOptions, ConnectStatusCallback connectStatusCallback) throws Exception;

    void send(String message, MessageStatusCallback messageStatusCallback, boolean isRetry);

    void disconnect();

    boolean isConnected();
}
