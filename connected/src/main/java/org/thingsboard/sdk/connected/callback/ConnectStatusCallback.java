package org.thingsboard.sdk.connected.callback;

/**
 * Created by h179730 on 12/17/2018.
 */

public interface ConnectStatusCallback {

    void onSuccess();

    void onFailure();

    void connectionLost();
}
