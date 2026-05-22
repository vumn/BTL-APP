package com.example.btl_app.Controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

        // TIME
        timeSeek =
                findViewById(R.id.timeSeek);

        timeValue =
                findViewById(R.id.timeValue);

        // Load time
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

                        saveSettingInt(
                                "questionTime",
                                finalTime);
                    }
                });

        // Load switches
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

        // Events
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

        switchSoundCorrect
                .setOnCheckedChangeListener(
                        (b, isChecked) ->
                                saveSetting(
                                        "soundCorrect",
                                        isChecked));

        switchSoundWrong
                .setOnCheckedChangeListener(
                        (b, isChecked) ->
                                saveSetting(
                                        "soundWrong",
                                        isChecked));

        switchSoundMenu
                .setOnCheckedChangeListener(
                        (b, isChecked) ->
                                saveSetting(
                                        "soundMenu",
                                        isChecked));

        // Back
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