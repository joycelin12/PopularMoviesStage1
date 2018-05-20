package com.example.android.popularmoviesstage1.Model;

/**
 * Created by joycelin12 on 5/19/18.
 */

public class Movie {

    private String title;
    private String release_date;
    private String rating;
    private String overview;
    private String poster_path;
    private static String M_BASEURL = "http://image.tmdb.org/t/p/w185";



    /**
     * No args constructor for use in serialization
     */
    public Movie() {
    }

    public Movie(String title, String release_date, String rating, String overview, String poster_path) {
        this.title = title;
        this.release_date = release_date;
        this.rating= rating;
        this.overview = overview;
        this.poster_path = M_BASEURL + poster_path;

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
        return M_BASEURL + poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = M_BASEURL + poster_path;
    }


}
