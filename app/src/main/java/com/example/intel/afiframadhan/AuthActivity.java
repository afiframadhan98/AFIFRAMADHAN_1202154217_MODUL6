package com.example.intel.afiframadhan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AuthActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    EditText loginEmail, loginPassword;
    Button btnLogin;
    TextView linkSignUp;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        // create new firebase instance
        auth = FirebaseAuth.getInstance();

        // check if user already logged in
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(AuthActivity.this, MainActivity.class));
            finish();
        }

        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        linkSignUp = findViewById(R.id.linkSignUp);
        btnLogin = findViewById(R.id.btnLogin);

        progressDialog = new ProgressDialog(this);


        // to sign up activity
        linkSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AuthActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginFirebase();
            }
        });
    }

    private void loginFirebase() {
        String email = loginEmail.getText().toString();
        final String password = loginPassword.getText().toString();

        // checking fields
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Your email is still empty",
                    Toast.LENGTH_SHORT).show();
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            loginEmail.setError("Please enter a valid email");
            loginEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Your password is still empty",
                    Toast.LENGTH_SHORT).show();
        }

        if (password.length() < 6) { // if password less than 6 chars
            loginPassword.setError(getString(R.string.minimum_password));
        }

        progressDialog.setMessage("Signing In...");
        progressDialog.show();

        // login user
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(AuthActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // if login not success
                        if (!task.isSuccessful()) {
                            Toast.makeText(AuthActivity.this, "Authentication Failed!", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }

                        // if login success
                        else {
                            progressDialog.dismiss();
                            Intent intent = new Intent(AuthActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }
}
