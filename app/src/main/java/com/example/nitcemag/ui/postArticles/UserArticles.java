package com.example.nitcemag.ui.postArticles;

public class UserArticles {
    public String author,email,title,description,category, image, key;
    public int editor,reviewer;
    public UserArticles(){}

    public UserArticles(String author,String email,String title,String description,String category, String image, String key)
    {
        this.author=author;
        this.category=category;
        this.key=key;
        this.title=title;
        this.email=email;
        this.description=description;
        this.image=image;
        this.editor=0;
        this.reviewer=0;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
}
