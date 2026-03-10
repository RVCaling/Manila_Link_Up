package com.manilalinkup.app;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;

public class OTPVerificationActivity extends AppCompatActivity {

    MaterialToolbar toolbar;
    EditText b1, b2, b3, b4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_otpverification);

        b1 = findViewById(R.id.edit_text_otp_box1);
        b2 = findViewById(R.id.edit_text_otp_box2);
        b3 = findViewById(R.id.edit_text_otp_box3);
        b4 = findViewById(R.id.edit_text_otp_box4);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        setupOtpBox(b1, null, b2);
        setupOtpBox(b2, b1, b3);
        setupOtpBox(b3, b2, b4);
        setupOtpBox(b4, b3, null);
    }

    private void setupOtpBox(EditText current, EditText previous, EditText next) {

        current.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() == 1 && next != null) {
                    next.requestFocus(); // move forward
                }

            }

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        current.setOnKeyListener((v, keyCode, event) -> {

            if (event.getAction() == KeyEvent.ACTION_DOWN &&
                    keyCode == KeyEvent.KEYCODE_DEL &&
                    current.getText().toString().isEmpty() &&
                    previous != null) {

                previous.requestFocus(); // move back
                previous.setSelection(previous.getText().length());
                return true;
            }

            return false;
        });
    }
}