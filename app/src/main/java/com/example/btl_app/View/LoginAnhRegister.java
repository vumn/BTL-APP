package com.example.btl_app.View;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_app.MainActivity;
import com.example.btl_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.time.Duration;

public class LoginAnhRegister extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText txtEmailSignIn, txtPassSignIn, txtUserNameRegister, txtEmailRegister, txtPassRegister, txtRePassRegister;
    private ImageButton imgBtnSelectAvarta;
    private Button btnLogin, btnRegisterAtSignIn, btnRegister, btnCancelRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_anh_register);

        mAuth = FirebaseAuth.getInstance();

        txtEmailSignIn = findViewById(R.id.txtUsername);
        txtPassSignIn = findViewById(R.id.txtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);


        // 👇 Lắng nghe nhập liệu
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInput();
            }
        };

        txtEmailSignIn.addTextChangedListener(watcher);
        txtPassSignIn.addTextChangedListener(watcher);

        btnLogin.setOnClickListener(v -> loginUser());

        btnRegister.setOnClickListener(v -> registerUser());
    }

    private void checkInput() {
        String email = txtEmailSignIn.getText().toString().trim();
        String pass = txtPassSignIn.getText().toString().trim();

        boolean isValid = !email.isEmpty()
                && !pass.isEmpty()
                && Patterns.EMAIL_ADDRESS.matcher(email).matches()
                && pass.length() >= 6;

        btnLogin.setEnabled(isValid);

        btnRegister.setEnabled(isValid);
    }


    private void loginUser() {
        String email = txtEmailSignIn.getText().toString().trim();
        String pass = txtPassSignIn.getText().toString().trim();

        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        new android.os.Handler(getMainLooper()).postDelayed(() -> {
                            Log.d("Main", "SignInUserWithEmail:succes");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(this, "Login: " + user.getEmail(), Toast.LENGTH_SHORT).show();
                            Intent it = new Intent(LoginAnhRegister.this, MainActivity.class);
                            startActivity(it);
                        }, 10000);
                        final ProgressBar progressBar = findViewById(R.id.btnLoading);
                        progressBar.setVisibility(View.VISIBLE);
                    } else {
                        Log.w("Main", task.getException().getMessage());
                        Toast.makeText(this, "Login failed!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void registerUser() {

        showDialogRegister();
        String email = txtEmailSignIn.getText().toString().trim();
        String pass = txtPassSignIn.getText().toString().trim();

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

    private void showDialogRegister() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_register);
        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        imgBtnSelectAvarta = dialog.findViewById(R.id.ImgBtnAvarta);
        btnRegister = dialog.findViewById(R.id.btnOKRegister);
        btnCancelRegister = dialog.findViewById(R.id.btnCancelRegister);
        txtUserNameRegister = dialog.findViewById(R.id.txtUsername);
        txtEmailRegister = dialog.findViewById(R.id.txtEmailRegister);
        txtPassRegister = dialog.findViewById(R.id.txtPasswordRegister);
        txtRePassRegister = dialog.findViewById(R.id.txtRePasswordRegister);




    }
}