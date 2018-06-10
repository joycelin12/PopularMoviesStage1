package com.example.android.popularmoviesstage1.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by joycelin12 on 6/10/18.
 */

public class Review implements Parcelable {

    String author;
    String content;
    String id;
    String url;

    public Review(String author, String content, String id, String url) {

        this.author = author;
        this.content = content;
        this.id = id;
        this.url= url;

    }


    public String getAuthor() {
        return author;
    }

    public void setAuthor(String key) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String name) {
        this.content = content;
    }

    public String getId() {return id;}

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String size) {this.url = url;}


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.author);
        dest.writeString(this.content);
        dest.writeString(this.id);
        dest.writeString(this.url);
    }

    public Review() {
    }

    protected Review(Parcel in) {
        this.author = in.readString();
        this.content = in.readString();
        this.id = in.readString();
        this.url = in.readString();
    }

    public static final Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel source) {
            return new Review(source);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };
}
