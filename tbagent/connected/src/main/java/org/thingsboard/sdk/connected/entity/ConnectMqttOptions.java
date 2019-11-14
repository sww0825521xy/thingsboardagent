package org.thingsboard.sdk.connected.entity;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Created by h179730 on 12/19/2018.
 */

public class ConnectMqttOptions extends ConnectOptions {

    public static final int MQTT_VERSION_DEFAULT = 0;
    public static final int MQTT_VERSION_3_1 = 3;
    public static final int MQTT_VERSION_3_1_1 = 4;

    private int keepAliveInterval = MqttConnectOptions.KEEP_ALIVE_INTERVAL_DEFAULT;
    private int maxInflight = MqttConnectOptions.MAX_INFLIGHT_DEFAULT;
    private String willDestination = null;
    private MqttMessage willMessage = null;
    private boolean cleanSession = MqttConnectOptions.CLEAN_SESSION_DEFAULT;
    private String[] serverURIs = null;
    private int MqttVersion = MQTT_VERSION_DEFAULT;


    private String clientId;
    private String subscribeTopic;
    private String publishTopic;
    private int qos;
    private boolean retained;

    public int getKeepAliveInterval() {
        return keepAliveInterval;
    }

    public void setKeepAliveInterval(int keepAliveInterval) {
        this.keepAliveInterval = keepAliveInterval;
    }

    public int getMaxInflight() {
        return maxInflight;
    }

    public void setMaxInflight(int maxInflight) {
        this.maxInflight = maxInflight;
    }

    public String getWillDestination() {
        return willDestination;
    }

    public void setWillDestination(String willDestination) {
        this.willDestination = willDestination;
    }

    public MqttMessage getWillMessage() {
        return willMessage;
    }

    public void setWillMessage(MqttMessage willMessage) {
        this.willMessage = willMessage;
    }

    public boolean isCleanSession() {
        return cleanSession;
    }

    public void setCleanSession(boolean cleanSession) {
        this.cleanSession = cleanSession;
    }

    public String[] getServerURIs() {
        return serverURIs;
    }

    public void setServerURIs(String[] serverURIs) {
        this.serverURIs = serverURIs;
    }

    public int getMqttVersion() {
        return MqttVersion;
    }

    public void setMqttVersion(int MqttVersion)throws IllegalArgumentException {
        if (MqttVersion != MQTT_VERSION_DEFAULT &&
                MqttVersion != MQTT_VERSION_3_1 &&
                MqttVersion != MQTT_VERSION_3_1_1) {
            throw new IllegalArgumentException();
        }
        this.MqttVersion = MqttVersion;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getSubscribeTopic() {
        return subscribeTopic;
    }

    public void setSubscribeTopic(String subscribeTopic) {
        this.subscribeTopic = subscribeTopic;
    }

    public String getPublishTopic() {
        return publishTopic;
    }

    public void setPublishTopic(String publishTopic) {
        this.publishTopic = publishTopic;
    }

    public int getQos() {
        return qos;
    }

    public void setQos(int qos) {
        this.qos = qos;
    }

    public boolean isRetained() {
        return retained;
    }

    public void setRetained(boolean retained) {
        this.retained = retained;
    }
}
