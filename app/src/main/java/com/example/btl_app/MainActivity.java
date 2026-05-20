package com.example.btl_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_app.Controller.GameActivity;
import com.example.btl_app.Controller.SoundManager;
import com.example.btl_app.View.LoginAnhRegister;

public class MainActivity extends AppCompatActivity {
    private Button btnPlay;
    private Button btnLogin;

    @Override
    protected void onResume() {
        super.onResume();
        // Chắc chắn rằng nhạc trong game đã tắt trước khi bật nhạc Menu
        SoundManager.stopBgMusic();

        // Bật nhạc Menu
        SoundManager.playMenuMusic(this);
    }

    // Hàm này chạy khi người dùng rời khỏi màn hình Menu (Vào game, vào setting, ẩn app...)
    @Override
    protected void onPause() {
        super.onPause();
        // Tạm dừng nhạc Menu
        SoundManager.stopMenuMusic();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        btnPlay = findViewById(R.id.btnPlayCustomer);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MainActivity.this, GameActivity.class);
                startActivity(it);
            }
        });

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MainActivity.this, LoginAnhRegister.class);
                startActivity(it);
            }
        });


//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
    }
}