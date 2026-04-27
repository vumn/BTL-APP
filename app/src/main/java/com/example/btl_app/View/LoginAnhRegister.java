package com.example.btl_app.View;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_app.MainActivity;
import com.example.btl_app.Model.User;
import com.example.btl_app.R;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Date;

public class LoginAnhRegister extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Uri selectedImageUri;

    // Đổi tên các biến màn hình chính cho rõ ràng
    private EditText txtEmailSignIn, txtPassSignIn;
    private Button btnLogin, btnOpenRegisterForm;

    // Biến cho ảnh Avatar (để Launcher có thể truy cập được)
    private ImageButton imgBtnSelectAvarta;

    // Khởi tạo Launcher để chọn ảnh
    private final ActivityResultLauncher<String> selectImageLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            result -> {
                if (result != null && imgBtnSelectAvarta != null) {
                    imgBtnSelectAvarta.setImageURI(result);
                    selectedImageUri = result;
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_anh_register);

        mAuth = FirebaseAuth.getInstance();

        txtEmailSignIn = findViewById(R.id.txtUsername);
        txtPassSignIn = findViewById(R.id.txtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnOpenRegisterForm = findViewById(R.id.btnRegister); // Đổi tên biến cho khỏi nhầm lẫn

        // Lắng nghe nhập liệu
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

        // Mở dialog đăng ký
        btnOpenRegisterForm.setOnClickListener(v -> showDialogRegister());
    }

    private void checkInput() {
        String email = txtEmailSignIn.getText().toString().trim();
        String pass = txtPassSignIn.getText().toString().trim();

        boolean isValid = !email.isEmpty()
                && !pass.isEmpty()
                && Patterns.EMAIL_ADDRESS.matcher(email).matches()
                && pass.length() >= 6;

        btnLogin.setEnabled(isValid);
    }

    private void loginUser() {
        String email = txtEmailSignIn.getText().toString().trim();
        String pass = txtPassSignIn.getText().toString().trim();
        final ProgressBar progressBar = findViewById(R.id.btnLoading); // Đưa lên đây để hiện ngay lập tức
        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Tạm dừng 1 chút trước khi chuyển màn hình (Theo ý tưởng code cũ của bạn)
                        new android.os.Handler(getMainLooper()).postDelayed(() -> {
                            Log.d("Main", "SignInUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(this, "Login: " + user.getEmail(), Toast.LENGTH_SHORT).show();

                            // Đừng quên ẩn Progressbar
                            progressBar.setVisibility(View.GONE);

                            Intent it = new Intent(LoginAnhRegister.this, HomeUser.class);
                            startActivity(it);
                            finish(); // Nên finish màn hình đăng nhập để user không back lại được
                        }, 2000); // 10000ms (10 giây) là quá lâu cho trải nghiệm người dùng, mình khuyên để 2000ms thôi
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Log.w("Main", task.getException().getMessage());
                        Toast.makeText(this, "Login failed!", Toast.LENGTH_SHORT).show();
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

        // TẠO BIẾN CỤC BỘ CHO CÁC VIEW TRONG DIALOG ĐỂ TRÁNH XUNG ĐỘT
        imgBtnSelectAvarta = dialog.findViewById(R.id.ImgBtnAvarta);
        Button btnOKRegister = dialog.findViewById(R.id.btnOKRegister);
        Button btnCancelDialog = dialog.findViewById(R.id.btnCancelRegister);
        EditText txtUserNameReg = dialog.findViewById(R.id.txtNameRegister);
        EditText txtEmailReg = dialog.findViewById(R.id.txtEmailRegister);
        EditText txtPassReg = dialog.findViewById(R.id.txtPasswordRegister);
        EditText txtRePassReg = dialog.findViewById(R.id.txtRePasswordRegister);
        TextView txtErrorRePassMsg = dialog.findViewById(R.id.txtErrorRePass);

        // --- SỰ KIỆN CHỌN ẢNH AVATAR ---
        imgBtnSelectAvarta.setOnClickListener(v -> {
            selectImageLauncher.launch("image/*");
        });

        // Hủy Dialog
        btnCancelDialog.setOnClickListener(view -> {
            dialog.dismiss();
            Toast.makeText(getApplicationContext(), "Đã hủy", Toast.LENGTH_SHORT).show();
        });

        // Xác nhận Đăng ký
        btnOKRegister.setOnClickListener(view -> {
            String email = txtEmailReg.getText().toString().trim();
            String pass = txtPassReg.getText().toString().trim();
            String rePass = txtRePassReg.getText().toString().trim();

            if (pass.length() < 6) {
                Toast.makeText(getApplicationContext(), "Mật khẩu phải từ 6 ký tự!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (rePass.equals(pass) && !pass.isEmpty()) {
                txtErrorRePassMsg.setVisibility(View.GONE);

                mAuth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                String userId = firebaseUser.getUid();
                                String userName = txtUserNameReg.getText().toString().trim();
                                if (selectedImageUri != null) {
                                    uploadAvartaAndSaveUser(userId, userName, selectedImageUri);
                                } else {
                                    saveUserToFirebase(userId, userName, "");
                                }
                                Toast.makeText(getApplicationContext(), "Register: " + firebaseUser.getEmail(), Toast.LENGTH_SHORT).show();
                                dialog.dismiss(); // Đăng ký thành công thì đóng dialog
                            } else {
                                Toast.makeText(getApplicationContext(), "Register failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                txtErrorRePassMsg.setVisibility(View.VISIBLE);
                txtRePassReg.setText("");
            }
        });

        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void saveUserToFirebase(String userId, String userName, String s) {
        User user = new User(userId, userName, s, new Date());
        FirebaseFirestore.getInstance().collection("users").document(userId).set(user).addOnSuccessListener(unused -> Log.d("FireStore", "user saved")).addOnFailureListener(e -> Log.d("Firestore", "Error" + e.getMessage()));
    }

    private void uploadAvartaAndSaveUser(String userId, String userName, Uri selectedImageUri) {
        StorageReference reference = FirebaseStorage.getInstance().getReference("avatars/" + userId + ".jpg");
        reference.putFile(selectedImageUri).addOnSuccessListener(taskSnapshot -> reference.getDownloadUrl().addOnSuccessListener(uri -> {
            String selectImageUri = uri.toString();
            saveUserToFirebase(userId, userName, selectImageUri);
        })).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Upload ảnh lỗi!", Toast.LENGTH_SHORT).show());
    }
}