/*
       Licensed to the Apache Software Foundation (ASF) under one
       or more contributor license agreements.  See the NOTICE file
       distributed with this work for additional information
       regarding copyright ownership.  The ASF licenses this file
       to you under the Apache License, Version 2.0 (the
       "License"); you may not use this file except in compliance
       with the License.  You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing,
       software distributed under the License is distributed on an
       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       KIND, either express or implied.  See the License for the
       specific language governing permissions and limitations
       under the License.
 */

package io.cordova.hellocordova;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import org.apache.cordova.*;
import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MainActivity extends CordovaActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // enable Cordova apps to be started in the background
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.getBoolean("cdvStartInBackground", false)) {
            moveTaskToBack(true);
        }

        // Set by <content src="index.html" /> in config.xml
        loadUrl(launchUrl);

        populateConnectionList();
    }

    IMqttAsyncClient mqttClient = null;
    private void populateConnectionList(){
        Context context = this.getApplicationContext();
        mqttClient = new MqttAndroidClient(context, "tcp://192.168.31.75:1883", "testConnect");
        try{
            //设置消息监听器（接收消息使用）
            MqttReceiver mqttV3Receiver = new MqttReceiver();
            mqttClient.setCallback(mqttV3Receiver);
            //设置mqtt参数
            MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
            mqttConnectOptions.setCleanSession(false);
            mqttClient.connect(mqttConnectOptions,context,new ActionListener());
        }catch(MqttException exception){
            exception.printStackTrace();
        }
    }

    private class MqttReceiver implements MqttCallback
    {

        @Override
        public void connectionLost(Throwable cause) {
        }

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            System.out.println("topic:  "+topic+"消息接收成功啦！！！！！！！！！" + new String(message.getPayload()));
            mqttClient.publish("topic.confirmed",new String(new String(message.getPayload())+"__confirmed").getBytes(),0,false);
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
        }
    }

    private class ActionListener implements IMqttActionListener {
        @Override
        public void onSuccess(IMqttToken asyncActionToken) {
            try{
                String[] topicNames = new String[]{"/Android/Topic"};
                //QoS level 0
                //至多发送一次，发送即丢弃。没有确认消息，也不知道对方是否收到。
                //QoS level 1
                //Qos level 1，意味着消息至少被传输一次。
                //QoS level 2
                //但确保了仅仅传输接收一次
                //当前cleanSession设置为false，且QOS为2，则默认为activemq的持久化消息（客户端掉线后，回来还能收到）！
                int[] topicQos = {2};
                mqttClient.subscribe(topicNames, topicQos);
            }catch(MqttException e){
                e.printStackTrace();
            }
            Log.d(TAG, "Success. Release lock(消息服务器连接成功！！！！！！！！！！！):"
                    + System.currentTimeMillis());
        }

        @Override
        public void onFailure(IMqttToken asyncActionToken,
                              Throwable exception) {
            Log.d(TAG, "Failure. Release lock(消息服务器连接失败！！！！！！！！！！！):"
                    + System.currentTimeMillis());
        }
    }
}
