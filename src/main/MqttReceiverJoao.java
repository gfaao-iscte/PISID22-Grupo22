import java.util.ArrayDeque;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.MqttTopic;

public class MqttReceiver implements MqttCallback {
	public static String CLOUDSERVER = "tcp://broker.mqtt-dashboard.com:1883";
	private String sensorTopic;
	private IMqttClient mqttClient;
	private BlockingQueue<String> messageList = new LinkedBlockingDeque<>();
	
	

	public MqttReceiver(String sensorTopic) {
		this.sensorTopic = sensorTopic;
		String clientId = UUID.randomUUID().toString();
		try {
			mqttClient = new MqttClient(CLOUDSERVER,clientId);
			MqttConnectOptions options = new MqttConnectOptions();
			options.setAutomaticReconnect(true);
			options.setCleanSession(false);
			options.setConnectionTimeout(60);
			mqttClient.connect(options);
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}
	
	public void subscribe() throws MqttSecurityException, MqttException {
		mqttClient.setCallback(this);
		mqttClient.subscribe(sensorTopic);
	}

	@Override
	public void connectionLost(Throwable arg0) {

	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public String getMessage() throws InterruptedException {
		return messageList.take();
	}

	@Override
	public void messageArrived(String arg0, MqttMessage arg1) throws Exception {
		//System.out.println(arg1.toString());
		messageList.put(arg1.toString());
	}

}

