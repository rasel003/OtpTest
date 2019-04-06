package com.rasel.otptest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ExampleBroadcastReceiver exampleBroadcastReceiver = new ExampleBroadcastReceiver();
    private TextView tvOtpCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvOtpCode = findViewById(R.id.tvOtpCode);
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(otpCodeReceiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(otpCodeReceiver);
    }
    private BroadcastReceiver otpCodeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("android.provider.Telephony.SMS_RECEIVED".equals(intent.getAction())) {
                final Bundle bundle = intent.getExtras();
                try {
                    if (bundle != null) {
                        final Object[] pdusObj = (Object[]) bundle.get("pdus");
                        for (Object aPdusObj : pdusObj) {
                            SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) aPdusObj);
                            String senderNum = currentMessage.getDisplayOriginatingAddress();
                            String message = currentMessage.getDisplayMessageBody();
                            try {

                                List<String> msg = Arrays.asList(message.split(":"));
                                String code = msg.get(1).trim();
                                tvOtpCode.setText(code);
                                Log.d("rasel", "onReceive: messsage is : "+message);
                                Log.d("rasel", "onReceive: messsage sender num : "+senderNum);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }else{
                        Log.d("rasel", "onReceive: bunder is null");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };
}
