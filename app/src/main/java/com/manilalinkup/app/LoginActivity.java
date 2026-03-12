package com.manilalinkup.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    MaterialToolbar toolbar;
    MaterialButton loginNowButton;

    TextInputEditText emailInput;
    TextInputEditText passwordInput;

    TextInputLayout emailLayout;
    TextInputLayout passwordLayout;

    ImageView googleLoginButton;

    private static final int RC_SIGN_IN = 100;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // --- VIEW BINDING ---
        loginNowButton = findViewById(R.id.material_button_login_now_2);
        emailInput = findViewById(R.id.text_input_email_input);
        passwordInput = findViewById(R.id.text_input_password_input);
        emailLayout = findViewById(R.id.text_input_layout_email_address);
        passwordLayout = findViewById(R.id.text_input_layout_password);
        googleLoginButton = findViewById(R.id.image_view_login_google);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void loginUser() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        emailLayout.setError(null);
        passwordLayout.setError(null);

        if (email.isEmpty()) {
            emailLayout.setError("Email is required.");
            emailInput.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            passwordLayout.setError("Password is required.");
            passwordInput.requestFocus();
            return;
        }

        if (password.length() < 8) {
            passwordLayout.setError("Password must be at least 8 characters.");
            passwordInput.requestFocus();
            return;
        }
    }

    }
