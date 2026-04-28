package com.example.btl_app.View;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.btl_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountInformation extends AppCompatActivity {

    private String name, imageUri, createAt, role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account_information);

        TextView txtName = findViewById(R.id.txtNameUserAccount);
        TextView txtEmail = findViewById(R.id.txtEmailUserAccount);
        TextView txtCreateAt = findViewById(R.id.txtCreateAtUserAccount);
        CircleImageView imgAvatarAccount = findViewById(R.id.AvatarAccount);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;
        String userId = user.getUid();
        String userEmail = user.getEmail();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(userId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                name = documentSnapshot.getString("userName");
                imageUri = documentSnapshot.getString("imageUri");

                txtName.setText(name);
                txtEmail.setText(userEmail);

                if (documentSnapshot.getTimestamp("createAt") != null) {
                    txtCreateAt.setText(documentSnapshot.getTimestamp("createAt").toDate().toString());
                }

                //load avatar
                if (imageUri != null && !imageUri.isEmpty()) {
                    Glide.with(AccountInformation.this).load(imageUri).into(imgAvatarAccount);
                }

            }
        }).addOnFailureListener(e -> {
            e.printStackTrace();
        });

        Button btnBackHomeUser = findViewById(R.id.btnBackHomeUser);
        btnBackHomeUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                db.collection("users").document(userId).get().addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        role = documentSnapshot.getString("role");
                        if (role.equals("admin")) {
                            Intent it = new Intent(AccountInformation.this, HomeAdmin.class);
                            it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(it);
                        }
                        if (role.equals("user")) {
                            Intent it = new Intent(AccountInformation.this, HomeUser.class);
                            it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(it);
                        }
                    }
                });
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}