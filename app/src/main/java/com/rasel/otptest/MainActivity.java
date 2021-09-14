package com.rasel.otptest;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private TextView tvOtpCode;
    private SmsBroadcastReceiver smsBroadcastReceiver;

    private static final int REQ_USER_CONSENT = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvOtpCode = findViewById(R.id.tvOtpCode);
    }

    @Override
    protected void onResume() {
        super.onResume();

      //  startSmsUserConsent();

        SmsRetrieverClient client = SmsRetriever.getClient(this);
        client.startSmsUserConsent(null);


    }

    private void startSmsUserConsent() {
        SmsRetrieverClient client = SmsRetriever.getClient(this);

        //We can add sender phone number or leave it blank
        // I'm adding null here
       /* client.startSmsUserConsent(null).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "On Success", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "On OnFailure", Toast.LENGTH_LONG).show();
            }
        });*/

        client.startSmsUserConsent(null);
    }

    private void registerBroadcastReceiver() {
        smsBroadcastReceiver = new SmsBroadcastReceiver();
        smsBroadcastReceiver.smsBroadcastReceiverListener =
                new SmsBroadcastReceiver.SmsBroadcastReceiverListener() {
                    @Override
                    public void onSuccess(Intent intent) {
                        startActivityForResult(intent, REQ_USER_CONSENT);
                    }

                    @Override
                    public void onFailure() {

                    }
                };
        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        registerReceiver(smsBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerBroadcastReceiver();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(smsBroadcastReceiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_USER_CONSENT) {
            if ((resultCode == RESULT_OK) && (data != null)) {
                //That gives all message to us.
                // We need to get the code from inside with regex
                String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
                //  tvOtpCode.setText(String.format("%s - %s", getString(R.string.received_message), message));
                // tvOtpCode.setText( message);
                //  getOtpFromMessage(message);

                // This will match any 6 digit number in the message
                Pattern pattern = Pattern.compile("(|^)\\d{6}");
                Matcher matcher = pattern.matcher(message);
                if (matcher.find()) {
                    tvOtpCode.setText(matcher.group(0));
                }
            }
        }
    }

    private void getOtpFromMessage(String message) {
        // This will match any 6 digit number in the message
        Pattern pattern = Pattern.compile("(|^)\\d{6}");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            tvOtpCode.setText(matcher.group(0));
        }
    }

}
