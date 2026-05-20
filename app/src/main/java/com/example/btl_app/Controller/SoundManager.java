package com.example.btl_app.Controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;

import com.example.btl_app.R;

public class SoundManager {
    private static MediaPlayer bgMediaPlayer;
    private static MediaPlayer menuMediaPlayer;

    public static void playMenuMusic(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("GameSettings", Context.MODE_PRIVATE);

        // Kiểm tra xem cả Âm thanh tổng và Âm thanh menu có bật không
        if (prefs.getBoolean("soundEnabled", true) && prefs.getBoolean("soundMenu", true)) {
            if (menuMediaPlayer == null) {
                menuMediaPlayer = MediaPlayer.create(context, R.raw.lobby_music);
                menuMediaPlayer.setLooping(true); // Bật chế độ lặp đi lặp lại
            }
            if (!menuMediaPlayer.isPlaying()) {
                menuMediaPlayer.start();
            }
        }
    }

    public static void stopMenuMusic() {
        if (menuMediaPlayer != null && menuMediaPlayer.isPlaying()) {
            menuMediaPlayer.pause();
        }
    }

    public static void releaseMenuMusic() {
        if (menuMediaPlayer != null) {
            menuMediaPlayer.stop();
            menuMediaPlayer.release();
            menuMediaPlayer = null;
        }
    }

    // 1. CHƠI NHẠC NỀN
    public static void playBgMusic(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("GameSettings", Context.MODE_PRIVATE);
        boolean isSoundEnabled = prefs.getBoolean("soundEnabled", true);

        if (isSoundEnabled) {
            if (bgMediaPlayer == null) {
                bgMediaPlayer = MediaPlayer.create(context, R.raw.bg_music);
                bgMediaPlayer.setLooping(true); // Lặp lại nhạc nền
            }
            if (!bgMediaPlayer.isPlaying()) {
                bgMediaPlayer.start();
            }
        }
    }

    // DỪNG NHẠC NỀN
    public static void stopBgMusic() {
        if (bgMediaPlayer != null && bgMediaPlayer.isPlaying()) {
            bgMediaPlayer.pause();
        }
    }

    // GIẢI PHÓNG NHẠC NỀN (Nên gọi khi thoát hẳn app)
    public static void releaseBgMusic() {
        if (bgMediaPlayer != null) {
            bgMediaPlayer.stop();
            bgMediaPlayer.release();
            bgMediaPlayer = null;
        }
    }

    // 2. ÂM THANH TRẢ LỜI ĐÚNG
    public static void playCorrectSound(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("GameSettings", Context.MODE_PRIVATE);
        // Kiểm tra xem âm thanh tổng và âm thanh đúng có đang được bật không
        if (prefs.getBoolean("soundEnabled", true) && prefs.getBoolean("soundCorrect", true)) {
            MediaPlayer correctPlayer = MediaPlayer.create(context, R.raw.correct_sound);
            correctPlayer.start();
            // Giải phóng bộ nhớ sau khi chạy xong
            correctPlayer.setOnCompletionListener(mp -> {
                mp.release();
            });
        }
    }

    // 3. ÂM THANH TRẢ LỜI SAI
    public static void playWrongSound(Context context) {
        SharedPreferences prefs =
                context.getSharedPreferences(
                        "GameSettings",
                        Context.MODE_PRIVATE
                );

        if (prefs.getBoolean("soundEnabled", true)
                && prefs.getBoolean("soundWrong", true)) {

            stopBgMusic();

            MediaPlayer wrongPlayer =
                    MediaPlayer.create(context, R.raw.wrong_sound);

            wrongPlayer.start();

            wrongPlayer.setOnCompletionListener(mp -> {
                mp.release();
            });
        }
    }

    public static void resumeBgMusic() {
        if (bgMediaPlayer != null && !bgMediaPlayer.isPlaying()) {
            bgMediaPlayer.start();
        }
    }

    public static void playClickSound(Context context) {

        SharedPreferences prefs =
                context.getSharedPreferences(
                        "GameSettings",
                        Context.MODE_PRIVATE
                );

        if (prefs.getBoolean("soundEnabled", true)) {

            MediaPlayer clickPlayer =
                    MediaPlayer.create(context, R.raw.choose);

            clickPlayer.start();

            clickPlayer.setOnCompletionListener(mp -> {
                mp.release();
            });
        }
    }

}
