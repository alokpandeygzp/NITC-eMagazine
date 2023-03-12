package com.example.nitcemag.ui.postArticles;

public class UserArticles {
    public String author_name,author_email,article_title,description,article_category;

   //public UserArticles(){}

    public UserArticles(String author_name,String author_email,String article_title,String description,String article_category)
    {
        this.author_name=author_name;
        this.article_category=article_category;
        this.article_title=article_title;
        this.author_email=author_email;
        this.description=description;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public String getAuthor_email() {
        return author_email;
    }

    public String getArticle_title() {
        return article_title;
    }

    public String getDescription() {
        return description;
    }

    public String getArticle_category() {
        return article_category;
    }
}
