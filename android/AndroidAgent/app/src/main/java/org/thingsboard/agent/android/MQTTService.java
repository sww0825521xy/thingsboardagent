package org.thingsboard.agent.android;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import org.thingsboard.sdk.connected.ConnectManager;
import org.thingsboard.sdk.connected.callback.ConnectStatusCallback;
import org.thingsboard.sdk.connected.callback.MessageCallback;
import org.thingsboard.sdk.connected.callback.MessageStatusCallback;
import org.thingsboard.sdk.connected.connector.Connector;
import org.thingsboard.sdk.connected.entity.ConnectMqttOptions;
import org.thingsboard.sdk.connected.entity.ConnectType;
import com.orhanobut.logger.Logger;

public class MQTTService extends Service {

    private static final String TAG = "MQTTService";

    public static String DEFAULT_MQTT_SERVER_IP = "tcp://106.12.186.57:1883";

    private static Connector connector;

    private static MessageStatusCallback messageStatusCallback;

    public static boolean isBroadcastRegisterd = false;

    private static MessageCallback messageCallback;

    private IBinder mBinder = new LocalBinder();

    public MQTTService() {
    }

    public class LocalBinder extends Binder {
        public MQTTService getServerInstance() {
            return MQTTService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            Log.d(TAG, "onStartCommand: Action=" + action);
            Logger.d("onStartCommand: Action=" + action);
            if (Const.ACTION_CONNECT_SERVER.equalsIgnoreCase(action)) {
                connectServer();
            } else if (Const.ACTION_DISCONNECT_SERVER.equalsIgnoreCase(action)) {
                disconnectServer();
            }
        }
        return START_STICKY;
    }

    private void registerBroadcast() {
        Log.i(TAG, "registerBroadcast :" + isBroadcastRegisterd);
        if (!isBroadcastRegisterd) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_TIME_TICK);
            filter.addAction(Intent.ACTION_TIME_CHANGED);
            filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
            registerReceiver(mIntentReceiver, filter, null, null);
            isBroadcastRegisterd = true;
        }
    }

    private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(intent.ACTION_TIME_TICK.equals(action)){
                Log.i(TAG, "onReceive action=" + action);
                //TODO
                boolean enrollFlag = false;
                if (enrollFlag) {
                    connectServer();
                }
            }
        }
    };

    private void connectServer() {
        registerBroadcast();
        synchronized (this) {
            Log.i(TAG, "connectServer");
            if (connector == null) {
                Log.i(TAG, "init connector");
                ConnectManager connectManager = ConnectManager.getInstance();
                connector = connectManager.init(ConnectType.MQTT);
            }

            if (connector != null) {
                if (!connector.isConnected()) {
                    Log.i(TAG, "connect to Mqtt server");
                    messageCallback = new MessageCallback() {
                        @Override
                        public void onMessage(String message) {
                            Log.i(TAG, "MessageCallback onMessage:" + message);
                            try {
                                dealMqttMessage(message);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    ConnectStatusCallback connectStatusCallback = new ConnectStatusCallback() {
                        @Override
                        public void onSuccess() {
                            Log.i(TAG, "ConnectStatusCallback onSuccess");
//                            enrollDevice();

                            sendOutConnectStatus(getApplicationContext(), "Connected");
                        }

                        @Override
                        public void onFailure() {
                            Log.i(TAG, "ConnectStatusCallback onFailure");
                            sendOutConnectStatus(getApplicationContext(), "Connect failure");
                        }

                        @Override
                        public void connectionLost() {
                            Log.i(TAG, "ConnectStatusCallback connectionLost");
                            disconnectServer();
                            sendOutConnectStatus(getApplicationContext(), "Connection lost");
                        }
                    };

                    connector.setCallback(messageCallback);

                    if (Utils.isWifiConnected(getApplicationContext()) || Utils.isEthernetConnected(getApplicationContext())) {
                        Log.i(TAG, "Connectivity is ok, connect to mqtt server");
                        String clientId = android.os.Build.SERIAL + "-" + android.os.Build.MODEL.replaceAll(" ", "_") + "-" + Const.MODULE_NAME;
                        ConnectMqttOptions connectOptions = new ConnectMqttOptions();
                        connectOptions.setUrl(DEFAULT_MQTT_SERVER_IP);
                        connectOptions.setClientId(clientId);
                        connectOptions.setUserName("XL5Uk0z68mtEetht6Dft");
                        connectOptions.setSubscribeTopic("toDevice/" + clientId);
                        connectOptions.setPublishTopic("toCloud/" + clientId);

                        connectOptions.setCleanSession(true);
                        connectOptions.setConnectionTimeout(100);
                        connectOptions.setKeepAliveInterval(200);

                        try {
                            connector.connect(getApplicationContext(), connectOptions, connectStatusCallback);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.d(TAG, "WI-FI or Ethernet is not available");
                    }
                } else {
                    Log.i(TAG, "already connect to Mqtt server");
//                    enrollDevice();
                }
            }
        }
    }

    private void disconnectServer() {
        Log.d(TAG, "disconnectServer");
        Logger.d("disconnectServer");
        if (connector != null) {
            connector.disconnect();
            connector = null;
        }
    }

    public void dealMqttMessage(String mqttMessage) {
        Log.i(TAG,"dealMqttMessage:" + mqttMessage);
        Logger.d("dealMqttMessage:" + mqttMessage);
    }

    private void sendOutConnectStatus(Context context, String status) {
//        Intent intent = new Intent(Const.ACTION_CONNECT_STATUS_CHANGE);
//        intent.putExtra(Const.ID_NETWORK_STATUS, status);
//        context.sendBroadcast(intent);
    }

    public static boolean publish(String msg) {
        try {
            if (connector != null) {
                Log.i(TAG, "publish:" + msg + " connector status:" + connector.isConnected());
                Logger.d("publish:" + msg + " connector status:" + connector.isConnected());
                messageStatusCallback = new MessageStatusCallback() {
                    @Override
                    public void deliveryComplete() {
                        Log.d(TAG, "deliveryComplete");
                    }
                };
                connector.send(msg, messageStatusCallback, false);
                return true;
            } else {
                Log.i(TAG, "publish:" + msg + " connector is null");
                Logger.d("publish:" + msg + " connector is null");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
