package com.manilalinkup.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import com.google.firebase.auth.*;

public class LoginActivity extends AppCompatActivity {

    MaterialToolbar toolbar;
    MaterialButton loginNowButton;

    TextInputEditText emailInput;
    TextInputEditText passwordInput;

    TextInputLayout emailLayout;
    TextInputLayout passwordLayout;

    ImageView googleLoginButton;

    FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;

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

        mAuth = FirebaseAuth.getInstance();

        // --- GOOGLE SIGN-IN CONFIG ---
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // --- BUTTON LISTENERS ---
        loginNowButton.setOnClickListener(v -> loginUser());
        googleLoginButton.setOnClickListener(v -> signInWithGoogle());
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

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, OTPVerificationActivity.class));
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Login Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Toast.makeText(this, "Google sign in failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Google Login Success", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, OTPVerificationActivity.class));
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}