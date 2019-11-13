package com.honeywell.sdk.connected;

import android.util.Log;

import com.honeywell.sdk.connected.connector.Connector;
import com.honeywell.sdk.connected.connector.impl.MQTTConnector;
import com.honeywell.sdk.connected.connector.impl.SentienceConnector;
import com.honeywell.sdk.connected.connector.impl.WebsocketConnector;
import com.honeywell.sdk.connected.entity.ConnectType;


/**
 * Created by h179730 on 12/17/2018.
 */

public class ConnectManager {

    private static final String TAG = ConnectManager.class.getSimpleName();

    private static ConnectManager mInstance;

    public static ConnectManager getInstance() {
        if (mInstance == null) {
            mInstance = new ConnectManager();
        }
        return mInstance;
    }

    public Connector init(ConnectType connectType) {
        Log.i(TAG, "init connector:" + connectType);
        switch (connectType) {
            case WEBSOCKET:
                return new WebsocketConnector();
            case MQTT:
                return new MQTTConnector();
            case SENTIENCE:
                return new SentienceConnector();
            default:
                return new WebsocketConnector();

        }
    }

}
