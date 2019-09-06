package com.example.tians.booklisting;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable{
    private String title;
    private String[] authors;
    private int coverImage;
    private String url;

    public Book(String title, String[] authors){
        this.title = title;
        this.authors = authors;
        this.coverImage = 0;
        this.url = null;
    }

    public Book(String title, String[] authors, String url){
        this.title = title;
        this.authors = authors;
        this.coverImage = 0;
        this.url = url;
    }

    public Book(String title, String[] authors, int imgResource){
        this.title = title;
        this.authors = authors;
        this.coverImage = imgResource;
        this.url = null;
    }

    public Book(String title, String[] authors, int imgResource, String url){
        this.title = title;
        this.authors = authors;
        this.coverImage = imgResource;
        this.url = url;
    }

    public Book(Parcel source) {
        this.title = source.readString();
        this.authors = source.createStringArray();
        this.coverImage = source.readInt();
        this.url = source.readString();
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeStringArray(authors);
        dest.writeInt(coverImage);
        dest.writeString(url);
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
