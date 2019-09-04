package com.example.tians.booklisting;

public class Book {
    private String title;
    private String author;
    private int coverImage;
    private String url;

    public Book(String title, String author){
        this.title = title;
        this.author = author;
        this.coverImage = 0;
        this.url = null;
    }

    public Book(String title, String author, int imgResource){
        this.title = title;
        this.author = author;
        this.coverImage = imgResource;
        this.url = null;
    }

    public Book(String title, String author, int imgResource, String url){
        this.title = title;
        this.author = author;
        this.coverImage = imgResource;
        this.url = url;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }
}
