package com.example.btl_app.Model;

import java.util.List;

public class Question {
    private String questionId;
    private String content;
    private List<String> answers;
    private int correctIndex;
    private int level;

    public Question() {
    }

    public Question(String questionId, String content, List<String> answers, int correctIndex, int level) {
        this.answers = answers;
        this.content = content;
        this.correctIndex = correctIndex;
        this.level = level;
        this.questionId = questionId;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public String getContent() {
        return content;
    }

    public int getCorrectIndex() {
        return correctIndex;
    }

    public int getLevel() {
        return level;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCorrectIndex(int correctAIndex) {
        this.correctIndex = correctAIndex;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }
}
