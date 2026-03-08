package com.manilalinkup.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

public class SMSBroadcastReceiver extends BroadcastReceiver {

    public interface OTPListener {
        void onOTPReceived(String message);
    }

    private OTPListener listener;

    public SMSBroadcastReceiver(OTPListener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {

            Status status = (Status) intent.getParcelableExtra(SmsRetriever.EXTRA_STATUS);

            if (status != null) {
                switch (status.getStatusCode()) {
                    case CommonStatusCodes.SUCCESS:
                        // Get the SMS message
                        String message = intent.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
                        if (listener != null && message != null) {
                            listener.onOTPReceived(message);
                        }
                        break;

                    case CommonStatusCodes.TIMEOUT:
                        // Timeout, OTP not received
                        break;
                }
            }
        }
    }
}