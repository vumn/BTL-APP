package com.example.btl_app.View;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_app.MainActivity;
import com.example.btl_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginAnhRegister extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText txtEmail, txtPass;
    private Button btnLogin, btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_anh_register);

        mAuth = FirebaseAuth.getInstance();

        txtEmail = findViewById(R.id.txtUsername);
        txtPass = findViewById(R.id.txtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);


        // 👇 Lắng nghe nhập liệu
        TextWatcher watcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInput();
            }
        };

        txtEmail.addTextChangedListener(watcher);
        txtPass.addTextChangedListener(watcher);

        btnLogin.setOnClickListener(v -> loginUser());

        btnRegister.setOnClickListener(v -> registerUser());
    }

    private void checkInput() {
        String email = txtEmail.getText().toString().trim();
        String pass = txtPass.getText().toString().trim();

        boolean isValid = !email.isEmpty()
                && !pass.isEmpty()
                && Patterns.EMAIL_ADDRESS.matcher(email).matches()
                && pass.length() >= 6;

        btnLogin.setEnabled(isValid);

        btnRegister.setEnabled(isValid);
    }


    private void loginUser() {
        String email = txtEmail.getText().toString().trim();
        String pass = txtPass.getText().toString().trim();

        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("Main", "SignInUserWithEmail:succes");
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(this, "Login: " + user.getEmail(), Toast.LENGTH_SHORT).show();
                        Intent it = new Intent(LoginAnhRegister.this, MainActivity.class);
                        startActivity(it);
                    } else {
                        Log.w("Main", task.getException().getMessage());
                        Toast.makeText(this, "Login failed!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void registerUser() {
        String email = txtEmail.getText().toString().trim();
        String pass = txtPass.getText().toString().trim();

        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(this, "Register: " + user.getEmail(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Register failed!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}