package org.thingsboard.sdk.connected.connector.impl;

import android.content.Context;
import android.util.Log;

import org.thingsboard.sdk.connected.callback.ConnectStatusCallback;
import org.thingsboard.sdk.connected.callback.MessageCallback;
import org.thingsboard.sdk.connected.callback.MessageStatusCallback;
import org.thingsboard.sdk.connected.connector.Connector;
import org.thingsboard.sdk.connected.entity.ConnectMqttOptions;
import org.thingsboard.sdk.connected.entity.ConnectOptions;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Created by h179730 on 12/17/2018.
 */

public class MQTTConnector implements Connector {

    private static final String TAG = "MQTTConnector";

    private ConnectMqttOptions connectOptions;

    private MessageCallback messageCallback;

    private ConnectStatusCallback connectStatusCallback;

    private MessageStatusCallback messageStatusCallback;

    private static MqttAndroidClient client;

    private MqttConnectOptions conOpt;

    private IMqttActionListener iMqttActionListener;


    @Override
    public void setCallback(MessageCallback messageCallback) {
        this.messageCallback = messageCallback;
    }


    @Override
    public void connect(Context context, ConnectOptions connectOptions, ConnectStatusCallback connectStatusCallback) throws MqttException {
        this.connectStatusCallback = connectStatusCallback;
        this.connectOptions = (ConnectMqttOptions) connectOptions;
        connectServer(context);
    }

    @Override
    public void send(String message, MessageStatusCallback messageStatusCallback, boolean isRetry) {
        this.messageStatusCallback = messageStatusCallback;
        try {
            String topic = connectOptions.getPublishTopic();
            client.publish(topic, message.getBytes(), connectOptions.getQos(), connectOptions.isRetained());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void disconnect() {
        if (client != null) {
            try {
                client.disconnect();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean isConnected() {
        return client == null ? false : client.isConnected();
    }

    private void connectServer(Context context) throws MqttException {
        client = new MqttAndroidClient(context, connectOptions.getUrl(), connectOptions.getClientId());

        MqttCallback mqttCallback = new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                Log.i(TAG, "connectionLost");
                connectStatusCallback.connectionLost();
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Log.i(TAG, "messageArrived:" + new String(message.getPayload()));
                messageCallback.onMessage(new String(message.getPayload()));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                try {
                    Log.i(TAG, "deliveryComplete:" + token.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                messageStatusCallback.deliveryComplete();
            }
        };
        client.setCallback(mqttCallback);

        conOpt = new MqttConnectOptions();
        conOpt.setCleanSession(connectOptions.isCleanSession());
        conOpt.setConnectionTimeout(connectOptions.getConnectionTimeout());
        conOpt.setKeepAliveInterval(connectOptions.getKeepAliveInterval());
        conOpt.setUserName("XL5Uk0z68mtEetht6Dft");

        iMqttActionListener = new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                try {
                    client.subscribe(connectOptions.getSubscribeTopic(), connectOptions.getQos());
                    connectStatusCallback.onSuccess();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                connectStatusCallback.onFailure();
            }
        };

        doClientConnection();

    }

    private void doClientConnection() throws MqttException {
        if (!client.isConnected()) {
            Log.i(TAG, "doClientConnection");
            client.connect(conOpt, null, iMqttActionListener);
        }
    }
}
