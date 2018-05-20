package com.example.android.popularmoviesstage1.Model;

/**
 * Created by joycelin12 on 5/19/18.
 */

public class Movie {

    private String title;
    private String release_date;
    private String rating;
    private String overview;
    private String image;


    /**
     * No args constructor for use in serialization
     */
    public Movie() {
    }

    public Movie(String title, String release_date, String rating, String overview, String image) {
        this.title = title;
        this.release_date = release_date;
        this.rating= rating;
        this.overview = overview;
        this.image = image;

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


}
