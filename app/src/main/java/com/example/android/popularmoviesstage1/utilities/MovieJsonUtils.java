package com.example.android.popularmoviesstage1.utilities;

import android.content.Context;
import android.util.Log;

import com.example.android.popularmoviesstage1.Model.Movie;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

/**
 * Created by joycelin12 on 5/16/18.
 */

public final class MovieJsonUtils {

    //movie information.
    private static final String M_RESULTS = "results";
    private static final String OWM_MESSAGE_CODE = "cod";
    private static String M_BASEURL = "http://image.tmdb.org/t/p/w185";
    private static final String JSON_TITLE_KEY = "title";
    private static final String JSON_RELEASE_DATE_KEY = "release_date";
    private static final String JSON_RATING_KEY = "vote_average";
    private static final String JSON_OVERVIEW_KEY = "overview";
    private static final String JSON_IMAGE_KEY = "poster_path";


    /**This method parses JSON from a web response and returns an array of Strings of movie posters
     * @param MovieJsonStr JSON response from server
     *
     * @return Array of Strings describing weather data
     *
     * @throws JSONException If JSON data cannot be properly parsed
     */
     public static String[] getSimpleMovieFromJson(Context context, String MovieJsonStr)
            throws JSONException {

         //string array to hold each movie string
         String[] parsedMovieData = null;

         JSONObject movieJson = new JSONObject(MovieJsonStr);

         /* Is there an error? */
         if (movieJson.has(OWM_MESSAGE_CODE)) {
             int errorCode = movieJson.getInt(OWM_MESSAGE_CODE);

             switch (errorCode) {
                 case HttpURLConnection.HTTP_OK:
                     break;
                 case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Location invalid */
                     return null;
                 default:
                    /* Server probably down */
                     return null;
             }
         }

         JSONArray movieArray = movieJson.getJSONArray(M_RESULTS);

         parsedMovieData = new String[movieArray.length()];


         for (int i = 0; i < movieArray.length(); i++) {

             //Get the JSON object representing results
             JSONObject movieObject =
                     movieArray.getJSONObject(i);

             String poster_path = movieObject.getString(JSON_IMAGE_KEY);

             parsedMovieData[i] = M_BASEURL + poster_path;


         }

         return parsedMovieData;

     }

    public static Movie parseMovieJson(String json) throws JSONException {

        //create a new variable to store movie details
        Movie details = new Movie();
        JSONObject jsonObj = new JSONObject(json);


        String poster_path = jsonObj.getString(JSON_IMAGE_KEY);

        String title = jsonObj.optString(JSON_TITLE_KEY);

        String release_date = jsonObj.optString(JSON_RELEASE_DATE_KEY);

        String rating = jsonObj.optString(JSON_RATING_KEY);

        String overview = jsonObj.optString(JSON_OVERVIEW_KEY);

        details.setTitle(title);
        details.setRelease_date(release_date);
        details.setRating(rating);
        details.setOverview(overview);
        details.setPoster_path(poster_path);

        return details;

    }

    public static String parseSingleMovieJson(String MovieJsonStr, int position) throws JSONException {

        JSONObject movieJson = new JSONObject(MovieJsonStr);
        JSONArray movieArray = movieJson.getJSONArray(M_RESULTS);
        JSONObject movieObject = movieArray.getJSONObject(position);
        return movieObject.toString();

    }

    public static Movie parseJSON(String response) {
        Gson gson = new GsonBuilder().create();
        Movie movie = gson.fromJson(response, Movie.class);
        return movie;
    }



}
