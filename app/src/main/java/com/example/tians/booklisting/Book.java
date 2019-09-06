package com.example.tians.booklisting;

public class Book {
    private String title;
    private String[] author;
    private int coverImage;
    private String url;

    public Book(String title, String[] author){
        this.title = title;
        this.author = author;
        this.coverImage = 0;
        this.url = null;
    }

    public Book(String title, String[] author, String url){
        this.title = title;
        this.author = author;
        this.coverImage = 0;
        this.url = url;
    }

    public Book(String title, String[] author, int imgResource){
        this.title = title;
        this.author = author;
        this.coverImage = imgResource;
        this.url = null;
    }

    public Book(String title, String[] author, int imgResource, String url){
        this.title = title;
        this.author = author;
        this.coverImage = imgResource;
        this.url = url;
    }

    public String getAuthors() {
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<author.length;i++){
            sb.append(author[i] + ", ");
        }
        return sb.substring(0, sb.length()-2).toString();
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public int getCoverImage() {
        return coverImage;
    }

    private boolean hasImage() {
        if (coverImage == 0) return false;
        else return true;
    }

    @Override
    public String toString() {
        return "\nTitle: " + getTitle() +
                "\nAuthor: " + getAuthors() +
                "\nLink: " + getUrl() +
                "\nHas image: " + hasImage();
    }


}
