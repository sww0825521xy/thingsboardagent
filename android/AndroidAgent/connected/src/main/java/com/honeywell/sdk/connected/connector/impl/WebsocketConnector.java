package com.honeywell.sdk.connected.connector.impl;

import android.content.Context;
import android.util.Log;

import com.honeywell.sdk.connected.callback.ConnectStatusCallback;
import com.honeywell.sdk.connected.callback.MessageCallback;
import com.honeywell.sdk.connected.callback.MessageStatusCallback;
import com.honeywell.sdk.connected.connector.Connector;
import com.honeywell.sdk.connected.entity.ConnectOptions;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/**
 * Created by h179730 on 12/17/2018.
 */

public class WebsocketConnector implements Connector {

    private static final String TAG = "WebsocketConnector";

    private String uri;

    private MessageCallback messageCallback;

    private ConnectStatusCallback connectStatusCallback;

    private MessageStatusCallback messageStatusCallback;

    public boolean isServerConnected;

    private WebSocketClient wc;

    private static final int CONNECTION_TIME_OUT = 0;

    @Override
    public void setCallback(MessageCallback messageCallback) {
        this.messageCallback = messageCallback;
    }

    @Override
    public void connect(Context context, ConnectOptions connectOptions, ConnectStatusCallback connectStatusCallback) throws Exception {
        this.messageStatusCallback = messageStatusCallback;
        connectServer(context, connectOptions);
    }

    @Override
    public void send(String message, MessageStatusCallback messageStatusCallback, boolean isRetry) {
        this.messageStatusCallback = messageStatusCallback;
        try {
            wc.send(message);
            messageStatusCallback.deliveryComplete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void disconnect() {
        Log.i(TAG, "disconnect");
        isServerConnected = false;
        if (wc != null) {
            wc.close();
            wc = null;
        }
    }

    @Override
    public boolean isConnected() {
        return isServerConnected;
    }

    private void connectServer(Context context, ConnectOptions connectOptions) throws Exception {
        synchronized (this) {
            if (wc == null) {
                wc = new WebSocketClient(new URI(connectOptions.getUrl()), new Draft_6455()) {
                    @Override
                    public void onOpen(ServerHandshake handshakedata) {
                        Log.i(TAG, "onOpen:" + handshakedata.getHttpStatusMessage());
                        isServerConnected = true;
                        connectStatusCallback.onSuccess();
                    }

                    @Override
                    public void onWebsocketPing(WebSocket conn, Framedata f) {
                        Log.i(TAG, "onWebsocketPing :" + f);
                    }

                    @Override
                    public void onWebsocketPong(WebSocket conn, Framedata f) {
                        super.onWebsocketPong(conn, f);
                        Log.i(TAG, "onWebsocketPong :" + f);
                    }

                    @Override
                    public void onMessage(String message) {
                        Log.i(TAG, "onMessage: message" + message);
                        messageCallback.onMessage(message);
                    }

                    @Override
                    public void onError(Exception ex) {
                        ex.printStackTrace();
                        connectStatusCallback.onFailure();
                    }

                    @Override
                    public void onClose(int code, String reason, boolean remote) {
                        Log.i(TAG, "onClose code:" + code);
                        connectStatusCallback.connectionLost();
                        disconnect();
                    }
                };
                Log.d(TAG, "connectServer: uri=" + uri);
                wc.setConnectionLostTimeout(connectOptions.getConnectionTimeout());
                wc.connect();
            }
        }
    }
}
