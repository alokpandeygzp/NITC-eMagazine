package com.example.nitcemag;

public class Articles {
    String title, image, description, category, author, date;
    int editor, reviewer;

    public Articles() {

    }

    public Articles(String title, String image, String description, String category, String author, String date, int editor, int reviewer) {
        this.title = title;
        this.image = image;
        this.description = description;
        this.category = category;
        this.author = author;
        this.date = date;
        this.editor=editor;
        this.reviewer=reviewer;
    }

    public int getEditor() {
        return editor;
    }

    public void setEditor(int editor) {
        this.editor = editor;
    }

    public int getReviewer() {
        return reviewer;
    }

    public void setReviewer(int reviewer) {
        this.reviewer = reviewer;
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
