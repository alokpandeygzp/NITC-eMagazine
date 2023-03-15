package com.example.nitcemag;

public class ModelComment {
    String email, article, text, name, key;
    public   ModelComment()
    {

    }

    public ModelComment(String email, String article, String text, String name, String key) {
        this.email = email;
        this.article = article;
        this.text = text;
        this.name = name;
        this.key=key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
