package com.anypresence.gw.mqtt;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

import org.fusesource.mqtt.client.Future;
import org.fusesource.mqtt.client.FutureConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.Message;
import org.fusesource.mqtt.client.QoS;
import org.fusesource.mqtt.client.Topic;

public class MQTTHandler {
    MQTT mqtt;
    FutureConnection connection;

    public MQTTHandler(String host, int port) throws URISyntaxException {
        mqtt = new MQTT();
        mqtt.setHost("tcp://" + host + ":" + port);

        mqtt.futureConnection();
    }

    public void connect() throws Exception {
        Future<Void> f = connection.connect();
        f.await();
    }

    public void subscribe(String topic) throws Exception {
        Future<byte[]> future = connection.subscribe(new Topic[]{new Topic(topic, QoS.AT_LEAST_ONCE)});
        Future<Message> receive = connection.receive();
        receive.await();
    }

    public void publish(String topic, String message) throws Exception {
        // We can start future receive..
        Future<Message> receive = connection.receive();

        Future<Void> future = connection.publish(topic, message.getBytes(), QoS.AT_LEAST_ONCE, false);
        Message msg = receive.await();
        msg.ack();

        Future<Void> futureDisc = connection.disconnect();
        futureDisc.await();
    }
}
