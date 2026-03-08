package com.manilalinkup.app;

import android.annotation.SuppressLint;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class OTPVerificationActivity extends AppCompatActivity {

    EditText otp1, otp2, otp3, otp4, otp5, otp6;
    TextView timerText, resendButton;

    FirebaseAuth mAuth;
    String verificationId;
    CountDownTimer timer;

    SMSBroadcastReceiver smsReceiver; // Receiver reference

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverification);

        otp1 = findViewById(R.id.otp1);
        otp2 = findViewById(R.id.otp2);
        otp3 = findViewById(R.id.otp3);
        otp4 = findViewById(R.id.otp4);
        otp5 = findViewById(R.id.otp5);
        otp6 = findViewById(R.id.otp6);

        timerText = findViewById(R.id.timerText);
        resendButton = findViewById(R.id.resendButton);

        mAuth = FirebaseAuth.getInstance();
        verificationId = getIntent().getStringExtra("verificationId");

        setupOTPInputs();
        startTimer();
        startSMSListener();
    }

    private void setupOTPInputs() {
        EditText[] boxes = {otp1, otp2, otp3, otp4, otp5, otp6};

        for (int i = 0; i < boxes.length; i++) {
            int index = i;
            boxes[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() == 1 && index < boxes.length - 1)
                        boxes[index + 1].requestFocus();

                    if (getOTP().length() == 6)
                        verifyOTP();
                }

                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            });
        }
    }

    private String getOTP() {
        return otp1.getText().toString() +
                otp2.getText().toString() +
                otp3.getText().toString() +
                otp4.getText().toString() +
                otp5.getText().toString() +
                otp6.getText().toString();
    }

    private void verifyOTP() {
        PhoneAuthCredential credential =
                PhoneAuthProvider.getCredential(verificationId, getOTP());

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                        Toast.makeText(this, "OTP Verified", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                });
    }

    private void startTimer() {
        resendButton.setVisibility(TextView.GONE);

        timer = new CountDownTimer(30000, 1000) {
            public void onTick(long millis) {
                timerText.setText("Resend in " + millis / 1000 + "s");
            }

            public void onFinish() {
                timerText.setText("");
                resendButton.setVisibility(TextView.VISIBLE);
            }
        }.start();
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    private void startSMSListener() {
        SmsRetriever.getClient(this).startSmsRetriever();

        IntentFilter filter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);

        // Create receiver and pass listener in constructor
        smsReceiver = new SMSBroadcastReceiver(message -> {
            if (message != null && message.length() >= 6) {
                String otp = message.replaceAll("[^0-9]", "").substring(0, 6);
                fillOTP(otp);
            }
        });

        registerReceiver(smsReceiver, filter);
    }

    private void fillOTP(String otp) {
        otp1.setText(String.valueOf(otp.charAt(0)));
        otp2.setText(String.valueOf(otp.charAt(1)));
        otp3.setText(String.valueOf(otp.charAt(2)));
        otp4.setText(String.valueOf(otp.charAt(3)));
        otp5.setText(String.valueOf(otp.charAt(4)));
        otp6.setText(String.valueOf(otp.charAt(5)));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (smsReceiver != null) unregisterReceiver(smsReceiver);
    }
}