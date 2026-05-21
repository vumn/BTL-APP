package com.example.btl_app.Model;

public class MoneyItem {

    private String money;
    private boolean selected;

    public MoneyItem(String money,
                     boolean selected) {

        this.money = money;
        this.selected = selected;
    }

    public String getMoney() {
        return money;
    }

    public boolean isSelected() {
        return selected;
    }
}