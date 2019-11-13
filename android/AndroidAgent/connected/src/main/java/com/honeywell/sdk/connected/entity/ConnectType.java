package com.honeywell.sdk.connected.entity;

/**
 * Created by h179730 on 12/17/2018.
 */

public enum ConnectType {
    WEBSOCKET("websocket"), MQTT("mqtt"), SENTIENCE("sentience");

    private String type;

    private ConnectType(String type) {
        this.type = type;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return getType();
    }

}
