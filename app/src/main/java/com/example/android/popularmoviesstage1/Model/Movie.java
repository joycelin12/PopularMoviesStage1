package com.example.android.popularmoviesstage1.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by joycelin12 on 5/19/18.
 */

public class Movie implements Parcelable {

    //https://guides.codepath.com/android/using-parcelable
    private String id;
    private String title;
    private String release_date;
    private String rating;
    private String overview;
    private String poster_path;


    /**
     * No args constructor for use in serialization
     */
    public Movie() {
    }

    public Movie(String id, String title, String release_date, String rating, String overview, String poster_path) {
        this.id = id;
        this.title = title;
        this.release_date = release_date;
        this.rating= rating;
        this.overview = overview;
        this.poster_path = poster_path;


    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {this.rating = rating;}


    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {this.poster_path = poster_path;}


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.release_date);
        dest.writeString(this.rating);
        dest.writeString(this.overview);
        dest.writeString(this.poster_path);
    }

    protected Movie(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.release_date = in.readString();
        this.rating = in.readString();
        this.overview = in.readString();
        this.poster_path = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
