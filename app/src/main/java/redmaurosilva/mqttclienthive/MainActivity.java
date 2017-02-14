package redmaurosilva.mqttclienthive;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {
    String clientId = MqttClient.generateClientId();
    MqttAndroidClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button myButton = (Button) findViewById(R.id.button);
        final TextView myTextView = (TextView) findViewById(R.id.textView);

        client = new MqttAndroidClient(this.getApplicationContext(), "tcp://broker.hivemq.com:1883",
                clientId);
        MqttConnectOptions options = new MqttConnectOptions();


        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Log.d("mqtt", "onSuccess");

                    setSubscriber();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Log.d("mqtt", "onFailure");

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                myTextView.setText(new String(message.getPayload()));
                Toast.makeText(MainActivity.this,"RECEIVED",Toast.LENGTH_LONG);
                Toast.makeText(MainActivity.this,"RECEIVED",Toast.LENGTH_LONG);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });



        myButton.setOnClickListener(
                new Button.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        String topic = "androidTest";
                        String message = "hello android";
                        try {
                             client.publish(topic, message.getBytes(),0,false);
                            Toast.makeText(MainActivity.this,"Sent",Toast.LENGTH_LONG);
                        } catch ( MqttException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }
    private void setSubscriber(){
        try{
            client.subscribe("androidTest",0);
        }catch(MqttException e){
            e.printStackTrace();
        }
    };
}
