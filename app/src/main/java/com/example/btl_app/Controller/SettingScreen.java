package com.example.btl_app.Controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.btl_app.R;
import com.example.btl_app.View.HomeUser;

public class SettingScreen extends AppCompatActivity {

    private Switch switchSoundEnabled, switchSoundCorrect, switchSoundWrong, switchSoundMenu;
    private SharedPreferences sharedPreferences;
    private SeekBar timeSeek;
    private TextView timeValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_setting_screen);

        // Khởi tạo SharedPreferences
        sharedPreferences = getSharedPreferences("GameSettings", Context.MODE_PRIVATE);
        // Lấy thời gian đã cài đặt (trả về 30 nếu người dùng chưa từng cài đặt)
        SharedPreferences prefs = getSharedPreferences("GameSettings", Context.MODE_PRIVATE);
        int questionTimeInSeconds = prefs.getInt("questionTime", 30);

        // Đổi ra milliseconds để dùng cho CountDownTimer (ví dụ: 30 * 1000 = 30,000 ms)
        long timeInMillis = questionTimeInSeconds * 1000L;

        // Sử dụng timeInMillis cho CountDownTimer của bạn
        CountDownTimer myTimer = new CountDownTimer(timeInMillis, 1000) {
            public void onTick(long millisUntilFinished) {
                // Cập nhật giao diện đồng hồ: millisUntilFinished / 1000
            }

            public void onFinish() {
                // Hết giờ -> Báo thua hoặc chuyển câu
            }
        }.start();

        // Ánh xạ View
        switchSoundEnabled = findViewById(R.id.soundEnabled);
        switchSoundCorrect = findViewById(R.id.soundCorrect);
        switchSoundWrong = findViewById(R.id.soundWrong);
        switchSoundMenu = findViewById(R.id.soundMenu);
        ImageView btnBack = findViewById(R.id.btnClose);

        // View thời gian
        timeSeek = findViewById(R.id.timeSeek);
        timeValue = findViewById(R.id.timeValue);

        // Tải dữ liệu thời gian (30s)
        int savedTime = sharedPreferences.getInt("questionTime", 30);
        timeSeek.setProgress(savedTime);
        timeValue.setText(savedTime + " giây");

        timeSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Ràng buộc thời gian tối thiểu là 5 giây (tránh lỗi nếu người dùng kéo về 0)
                int actualTime = Math.max(progress, 5);
                timeValue.setText(actualTime + " giây");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Không cần làm gì khi bắt đầu chạm
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Khi người dùng thả tay ra -> Lưu giá trị vào SharedPreferences
                int finalTime = Math.max(seekBar.getProgress(), 5);
                saveSettingInt("questionTime", finalTime);
            }
        });


        // Load trạng thái đã lưu trước đó (mặc định là true/bật)
        switchSoundEnabled.setChecked(sharedPreferences.getBoolean("soundEnabled", true));
        switchSoundCorrect.setChecked(sharedPreferences.getBoolean("soundCorrect", true));
        switchSoundWrong.setChecked(sharedPreferences.getBoolean("soundWrong", true));
        switchSoundMenu.setChecked(sharedPreferences.getBoolean("soundMenu", true));

        // Lắng nghe sự kiện thay đổi để lưu lại
        switchSoundEnabled.setOnCheckedChangeListener((buttonView, isChecked) -> {
            saveSetting("soundEnabled", isChecked);
            // Nếu tắt âm thanh tổng, có thể tắt luôn nhạc nền đang chạy (gọi SoundManager)
            if(!isChecked) SoundManager.stopBgMusic();
            else SoundManager.playBgMusic(this);
        });

        switchSoundCorrect.setOnCheckedChangeListener((b, isChecked) -> saveSetting("soundCorrect", isChecked));
        switchSoundWrong.setOnCheckedChangeListener((b, isChecked) -> saveSetting("soundWrong", isChecked));
        switchSoundMenu.setOnCheckedChangeListener((b, isChecked) -> saveSetting("soundMenu", isChecked));

        // Nút Back
        btnBack.setOnClickListener(view -> {
            Intent it = new Intent(SettingScreen.this, HomeUser.class);
            it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(it);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Hàm hỗ trợ lưu SharedPreferences
    private void saveSetting(String key, boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }
    // Hàm luuw kiểu Int cho tgian
    private void saveSettingInt(String key, int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }
}