package com.example.btl_app.Controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.btl_app.Controller.SoundManager;
import com.example.btl_app.R;
import com.example.btl_app.View.HomeUser;

import java.security.PrivateKey;
import java.util.ArrayList;

public class SettingScreen extends AppCompatActivity {


    private static final String[] Gamemode = {"Tự do", "Thời gian"};
    private Switch switchSoundEnabled,
            switchSoundCorrect,
            switchSoundWrong,
            switchSoundMenu;

    private SeekBar timeSeek;
    private TextView timeValue;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_setting_screen);

        // SharedPreferences
        sharedPreferences =
                getSharedPreferences(
                        "GameSettings",
                        Context.MODE_PRIVATE);

        // Lấy thời gian đã lưu
        int questionTimeInSeconds =
                sharedPreferences.getInt(
                        "questionTime",
                        30);

        // Đổi sang milliseconds
        long timeInMillis =
                questionTimeInSeconds * 1000L;

        // Timer demo
        CountDownTimer myTimer =
                new CountDownTimer(
                        timeInMillis,
                        1000) {

                    @Override
                    public void onTick(long millisUntilFinished) {

                        // Cập nhật thời gian nếu cần
                    }

                    @Override
                    public void onFinish() {

                        // Hết giờ
                    }
                };

        // Views
        switchSoundEnabled =
                findViewById(R.id.soundEnabled);

        switchSoundCorrect =
                findViewById(R.id.soundCorrect);

        switchSoundWrong =
                findViewById(R.id.soundWrong);

        switchSoundMenu =
                findViewById(R.id.soundMenu);

        ImageView btnBack =
                findViewById(R.id.btnClose);

        timeSeek =
                findViewById(R.id.timeSeek);

        timeValue =
                findViewById(R.id.timeValue);

        // Load thời gian
        int savedTime =
                sharedPreferences.getInt(
                        "questionTime",
                        30);

        timeSeek.setProgress(savedTime);

        timeValue.setText(
                savedTime + " giây");

        // SeekBar listener
        timeSeek.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onProgressChanged(
                            SeekBar seekBar,
                            int progress,
                            boolean fromUser) {

                        int actualTime =
                                Math.max(progress, 5);

                        timeValue.setText(
                                actualTime + " giây");
                    }

                    @Override
                    public void onStartTrackingTouch(
                            SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(
                            SeekBar seekBar) {

                        int finalTime =
                                Math.max(
                                        seekBar.getProgress(),
                                        5);

                        seekBar.setProgress(finalTime);

                        saveSettingInt(
                                "questionTime",
                                finalTime);
                    }
                });

        // Load switch states
        switchSoundEnabled.setChecked(
                sharedPreferences.getBoolean(
                        "soundEnabled",
                        true));

        switchSoundCorrect.setChecked(
                sharedPreferences.getBoolean(
                        "soundCorrect",
                        true));

        switchSoundWrong.setChecked(
                sharedPreferences.getBoolean(
                        "soundWrong",
                        true));

        switchSoundMenu.setChecked(
                sharedPreferences.getBoolean(
                        "soundMenu",
                        true));

        // Sound Enabled
        switchSoundEnabled
                .setOnCheckedChangeListener(
                        (buttonView, isChecked) -> {

                            saveSetting(
                                    "soundEnabled",
                                    isChecked);

                            if (!isChecked) {

                                SoundManager.stopBgMusic();

                            } else {

                                SoundManager.playBgMusic(this);
                            }
                        });

        // Sound Correct
        switchSoundCorrect
                .setOnCheckedChangeListener(
                        (buttonView, isChecked) ->
                                saveSetting(
                                        "soundCorrect",
                                        isChecked));

        // Sound Wrong
        switchSoundWrong
                .setOnCheckedChangeListener(
                        (buttonView, isChecked) ->
                                saveSetting(
                                        "soundWrong",
                                        isChecked));

        // Sound Menu
        switchSoundMenu
                .setOnCheckedChangeListener(
                        (buttonView, isChecked) ->
                                saveSetting(
                                        "soundMenu",
                                        isChecked));

        // Back button
        btnBack.setOnClickListener(view -> {

            Intent it =
                    new Intent(
                            SettingScreen.this,
                            HomeUser.class);

            it.setFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(it);

            finish();
        });

        // Insets
        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (v, insets) -> {

                    Insets systemBars =
                            insets.getInsets(
                                    WindowInsetsCompat.Type.systemBars());

                    v.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom);

                    return insets;
                });


        ChooseGameMode();
    }

    private void ChooseGameMode() {
        Spinner spinner = findViewById(R.id.gameMode);
        android.widget.ArrayAdapter<String> adapter = new android.widget.ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Gamemode);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //Load mode đã lưu
        int saveMode = sharedPreferences.getInt("gameMode", 0);

        spinner.setSelection(saveMode);

        //xử lý chọn item
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                saveSettingInt("gameMode", i);

                //mode tự do
                if(i == 0)
                {
                    timeSeek.setEnabled(false);
                    timeValue.setText("không giới hạn!");
                }

                //mode có thời gian
                else {
                    timeSeek.setEnabled(true);
                    int savedTime = sharedPreferences.getInt("questionTime", 30);
                    timeValue.setText(savedTime + "giây");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    // Save boolean
    private void saveSetting(
            String key,
            boolean value) {

        SharedPreferences.Editor editor =
                sharedPreferences.edit();

        editor.putBoolean(key, value);

        editor.apply();
    }

    // Save int
    private void saveSettingInt(
            String key,
            int value) {

        SharedPreferences.Editor editor =
                sharedPreferences.edit();

        editor.putInt(key, value);

        editor.apply();
    }
}
