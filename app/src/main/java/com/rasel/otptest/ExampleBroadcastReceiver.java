package com.rasel.otptest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class ExampleBroadcastReceiver extends BroadcastReceiver {

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
}