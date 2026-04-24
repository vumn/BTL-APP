package com.example.btl_app.Model;

import java.util.Set;

public class Setting {
    private String theme;
    private boolean soundEnabled;
    private boolean soundCorrect;
    private boolean soundWrong;
    private boolean soundMenu;
    private String gameMode;
    private int timePerQuestion;

    public Setting(){}
    public Setting(String gameMode, boolean soundCorrect, boolean soundEnabled, boolean soundMenu, boolean soundWrong, String theme, int timePerQuestion) {
        this.gameMode = gameMode;
        this.soundCorrect = soundCorrect;
        this.soundEnabled = soundEnabled;
        this.soundMenu = soundMenu;
        this.soundWrong = soundWrong;
        this.theme = theme;
        this.timePerQuestion = timePerQuestion;
    }

    public int getTimePerQuestion() {
        return timePerQuestion;
    }

    public String getTheme() {
        return theme;
    }

    public boolean isSoundWrong() {
        return soundWrong;
    }

    public boolean isSoundMenu() {
        return soundMenu;
    }

    public boolean isSoundEnabled() {
        return soundEnabled;
    }

    public boolean isSoundCorrect() {
        return soundCorrect;
    }

    public String getGameMode() {
        return gameMode;
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

    public void setSoundCorrect(boolean soundCorrect) {
        this.soundCorrect = soundCorrect;
    }

    public void setSoundEnabled(boolean soundEnabled) {
        this.soundEnabled = soundEnabled;
    }

    public void setSoundMenu(boolean soundMenu) {
        this.soundMenu = soundMenu;
    }

    public void setSoundWrong(boolean soundWrong) {
        this.soundWrong = soundWrong;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public void setTimePerQuestion(int timePerQuestion) {
        this.timePerQuestion = timePerQuestion;
    }
}
