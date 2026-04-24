package com.example.btl_app.Model;

public class Audio {
    private String typeName;
    private String url;
    private String context;
    private String category;

    public Audio(String url, String typeName, String context, String category) {
        this.url = url;
        this.typeName = typeName;
        this.context = context;
        this.category = category;
    }

    public Audio() {
    }

    public String getCategory() {
        return category;
    }

    public String getContext() {
        return context;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getUrl() {
        return url;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
