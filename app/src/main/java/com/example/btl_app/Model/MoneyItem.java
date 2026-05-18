package com.example.btl_app.Model;

public class MoneyItem {

    private String text;
    private boolean selected;

    public MoneyItem(String text, boolean selected) {
        this.text = text;
        this.selected = selected;
    }

    public String getText() {
        return text;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}