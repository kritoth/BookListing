package com.example.tians.booklisting;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable{
    private String title;
    private String[] authors;
    private Bitmap coverImage;
    private String url;
/*
    public Book(String title, String[] authors){
        this.title = title;
        this.authors = authors;
        this.coverImage = null;
        this.url = null;
    }

    public Book(String title, String[] authors, String url){
        this.title = title;
        this.authors = authors;
        this.coverImage = null;
        this.url = url;
    }

    public Book(String title, String[] authors, Bitmap imgResource){
        this.title = title;
        this.authors = authors;
        this.coverImage = imgResource;
        this.url = null;
    }
*/
    public Book(String title, String[] authors, Bitmap imgResource, String url){
        this.title = title;
        this.authors = authors;
        this.coverImage = imgResource;
        this.url = url;
    }

    public String getAuthors() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i< authors.length; i++){
            sb.append(authors[i] + ", ");
        }
        return sb.substring(0, sb.length()-2).toString();
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public Bitmap getCoverImage() {
        return coverImage;
    }

    public boolean hasImage() {
        if (coverImage == null) return false;
        else return true;
    }

    @Override
    public String toString() {
        return "\nTitle: " + getTitle() +
                "\nAuthor: " + getAuthors() +
                "\nLink: " + getUrl() +
                "\nHas image: " + hasImage();
    }

    //Below are Parcelable constructors and methods implemented

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeStringArray(authors);
        dest.writeValue(coverImage);
        dest.writeString(url);
    }

    public Book(Parcel p){
        this.title = p.readString();
        this.authors = (String[]) p.readArray(String.class.getClassLoader());
        this.coverImage = (Bitmap) p.readValue(Bitmap.class.getClassLoader());
        this.url = p.readString();
    }

    public static final Parcelable.Creator<Book> CREATOR
            = new Parcelable.Creator<Book>() {
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        public Book[] newArray(int size) {
            return new Book[size];
        }
    };
}
