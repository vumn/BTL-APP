package com.example.btl_app.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.btl_app.MainActivity;
import com.example.btl_app.R;
import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeAdmin extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_admin);

        CircleImageView btnAvatar = findViewById(R.id.ImgBtnAvatarAdmin);
        Button btnUserManagement = findViewById(R.id.btnUserManagement);
        Button btnQuestionManagement = findViewById(R.id.btnQuestionManagement);


        btnAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popupMenu = new PopupMenu(HomeAdmin.this, view);
                popupMenu.setOnMenuItemClickListener(HomeAdmin.this);
                popupMenu.inflate(R.menu.profile_menu);
                popupMenu.show();
            }
        });

        btnUserManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(HomeAdmin.this, UserManagementScreen.class);
                startActivity(it);
            }
        });
        btnQuestionManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(HomeAdmin.this, QuestionManagementScreen.class);
                startActivity(it);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.account) {
            Intent it = new Intent(HomeAdmin.this, AccountInformation.class);
            startActivity(it);
        }
        if (id == R.id.logOut) {
            performLogout();
            return true;
        }
        return false;
    }

    private void performLogout() {
        FirebaseAuth.getInstance().signOut();
        Intent it = new Intent(HomeAdmin.this, MainActivity.class);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(it);
        finish();
        Toast.makeText(getApplicationContext(), "Log out thành công", Toast.LENGTH_SHORT).show();
    }
}