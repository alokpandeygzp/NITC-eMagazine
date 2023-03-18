package com.example.nitcemag.ui;

public class ModelLike {
    String email, article, name, key;
    public void Modelike()
    {

    }

    public ModelLike(String email, String article, String name, String key) {
        this.email = email;
        this.article = article;
        this.name = name;
        this.key = key;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
