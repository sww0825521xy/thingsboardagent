/**
 * 
 */
package org.thingsboard.agent.pc;

import java.util.UUID;

import org.thingsboard.sdk.connected.ConnectManager;
import org.thingsboard.sdk.connected.callback.ConnectStatusCallback;
import org.thingsboard.sdk.connected.callback.MessageCallback;
import org.thingsboard.sdk.connected.callback.MessageStatusCallback;
import org.thingsboard.sdk.connected.connector.Connector;
import org.thingsboard.sdk.connected.entity.ConnectMqttOptions;
import org.thingsboard.sdk.connected.entity.ConnectType;

/**
 * @author H179730
 *
 */
public class Test {

	private static Connector connector;

	private static MessageStatusCallback messageStatusCallback;

	public static boolean isBroadcastRegisterd = false;

	private static MessageCallback messageCallback;

	public static String DEFAULT_MQTT_SERVER_IP = "tcp://106.12.186.57:1883";

	public static String ACCESS_TOKEN = "XL5Uk0z68mtEetht6Dft";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (connector == null) {
			ConnectManager connectManager = ConnectManager.getInstance();
			connector = connectManager.init(ConnectType.MQTT);
		}

		if (connector != null) {
			if (!connector.isConnected()) {
				System.out.println("connect to Mqtt server");
				messageCallback = new MessageCallback() {
					@Override
					public void onMessage(String message) {
						System.out.println("MessageCallback onMessage:" + message);
						try {
//                            dealMqttMessage(message);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				};

				ConnectStatusCallback connectStatusCallback = new ConnectStatusCallback() {
					@Override
					public void onSuccess() {
						System.out.println("ConnectStatusCallback onSuccess");
//                        enrollDevice();
						//publish message
						publish("{\"device\":\"Device B\"}");
					}

					@Override
					public void onFailure() {
						System.out.println("ConnectStatusCallback onFailure");
					}

					@Override
					public void connectionLost() {
						System.out.println("ConnectStatusCallback connectionLost");
						disconnectServer();
					}
				};

				connector.setCallback(messageCallback);

				System.out.println("Connectivity is ok, connect to mqtt server");
				String clientId = UUID.randomUUID().toString();
				ConnectMqttOptions connectOptions = new ConnectMqttOptions();
				connectOptions.setUrl(DEFAULT_MQTT_SERVER_IP);
				connectOptions.setClientId(clientId);
				connectOptions.setUserName(ACCESS_TOKEN);
				connectOptions.setSubscribeTopic("v1/gateway/rpc");
				connectOptions.setPublishTopic("v1/gateway/connect");

				connectOptions.setCleanSession(true);
				connectOptions.setConnectionTimeout(100);
				connectOptions.setKeepAliveInterval(200);

				try {
					connector.connect(connectOptions, connectStatusCallback);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("already connect to Mqtt server");
//                enrollDevice();
			}
		}
	}

	private static void disconnectServer() {
		if (connector != null) {
			connector.disconnect();
			connector = null;
		}
	}
	
	public static boolean publish(String msg) {
        try {
            if (connector != null) {
            	System.out.println("publish:" + msg + " connector status:" + connector.isConnected());
            	System.out.println("publish:" + msg + " connector status:" + connector.isConnected());
                messageStatusCallback = new MessageStatusCallback() {
                    @Override
                    public void deliveryComplete() {
                    	System.out.println("deliveryComplete");
                    }
                };
                connector.send(msg, messageStatusCallback, false);
                return true;
            } else {
            	System.out.println("publish:" + msg + " connector is null");
            	System.out.println("publish:" + msg + " connector is null");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
