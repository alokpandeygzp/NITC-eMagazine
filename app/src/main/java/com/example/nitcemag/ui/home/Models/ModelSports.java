package com.example.nitcemag.ui.home.Models;

public class ModelSports {
    String title, image, description, category, author, date, key,email;
    int editor;

    public ModelSports() {

    }

    public ModelSports(String title, String image, String description, String category, String author, String date, String key, int editor, String email) {
        this.title = title;
        this.image = image;
        this.description = description;
        this.category = category;
        this.author = author;
        this.date = date;
        this.key=key;
        this.editor=editor;
        this.email=email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getEditor() {
        return editor;
    }

    public void setEditor(int editor) {
        this.editor = editor;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}