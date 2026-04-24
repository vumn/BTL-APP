package com.example.btl_app.Model;

public class Statistics {
    private String userId;
    private int totalGames;
    private int totalWins;
    private int totalLoses;
    private int highestScore;
    private int averageScore;
    private int corectAnswers;
    private int wrongAnswers;

    public Statistics(){}
    public Statistics(int averageScore, int corectAnswers, int highestScore, int totalGames, int totalLoses, int totalWins, String userId, int wrongAnswers) {
        this.averageScore = averageScore;
        this.corectAnswers = corectAnswers;
        this.highestScore = highestScore;
        this.totalGames = totalGames;
        this.totalLoses = totalLoses;
        this.totalWins = totalWins;
        this.userId = userId;
        this.wrongAnswers = wrongAnswers;
    }

    public int getAverageScore() {
        return averageScore;
    }

    public int getCorectAnswers() {
        return corectAnswers;
    }

    public int getHighestScore() {
        return highestScore;
    }

    public int getTotalGames() {
        return totalGames;
    }

    public int getTotalLoses() {
        return totalLoses;
    }

    public int getTotalWins() {
        return totalWins;
    }

    public String getUserId() {
        return userId;
    }

    public int getWrongAnswers() {
        return wrongAnswers;
    }

    public void setAverageScore(int averageScore) {
        this.averageScore = averageScore;
    }

    public void setCorectAnswers(int corectAnswers) {
        this.corectAnswers = corectAnswers;
    }

    public void setHighestScore(int highestScore) {
        this.highestScore = highestScore;
    }

    public void setTotalGames(int totalGames) {
        this.totalGames = totalGames;
    }

    public void setTotalLoses(int totalLoses) {
        this.totalLoses = totalLoses;
    }

    public void setTotalWins(int totalWins) {
        this.totalWins = totalWins;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setWrongAnswers(int wrongAnswers) {
        this.wrongAnswers = wrongAnswers;
    }
}
