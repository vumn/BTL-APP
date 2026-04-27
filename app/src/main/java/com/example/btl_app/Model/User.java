package com.example.btl_app.Model;

import java.util.Date;

public class User {
    private String userName;
    private String userId;
    private String imageUri;
    private Date createAt;

    public User(String userId, String userName, String imageUri, Date createAt) {
        this.createAt = createAt;
        this.imageUri = imageUri;
        this.userId = userId;
        this.userName = userName;
    }

    public User() {
    }

    public Date getCreateAt() {
        return createAt;
    }

    public String getImageUri() {
        return imageUri;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
