package com.example.btl_app.Model;

import java.util.Date;
import java.util.List;

public class GameSession {
    private String sessionId;
    private String userId;
    private int score;
    private int currentLevel;
    private int money;
    private String result; //(playing, win, lose, quit)
    private List<String> lifelinesUsed;
    private long playedAt;

    private boolean isFinished;

    // Constructor rỗng (Firebase bắt buộc)
    public GameSession() {
    }

    public GameSession(String sessionId, String userId, int score, int currentLevel,
                        int money, String result, List<String> lifelinesUsed, long playedAt, boolean isFinished) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.score = score;
        this.currentLevel = currentLevel;
        this.money = money;
        this.result = result;
        this.lifelinesUsed = lifelinesUsed;
        this.playedAt = playedAt;
        this.isFinished = isFinished;
    }

    // Getter & Setter
    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<String> getLifelinesUsed() {
        return lifelinesUsed;
    }

    public void setLifelinesUsed(List<String> lifelinesUsed) {
        this.lifelinesUsed = lifelinesUsed;
    }

    public long getPlayedAt() {
        return playedAt;
    }

    public void setPlayedAt(long playedAt) {
        this.playedAt = playedAt;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }
}
