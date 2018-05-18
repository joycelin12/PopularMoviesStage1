package com.example.android.popularmoviesstage1.utilities;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

/**
 * Created by joycelin12 on 5/16/18.
 */

public final class MovieJsonUtils {

    /**This method parses JSON from a web response and returns an array of Strings
     * @param MovieJsonStr JSON response from server
     *
     * @return Array of Strings describing weather data
     *
     * @throws JSONException If JSON data cannot be properly parsed
     */
     public static String[] getSimpleMovieStringsFromJson(Context context, String MovieJsonStr)
            throws JSONException {

         //movie information.
         final String M_RESULTS = "results";
         final String OWM_MESSAGE_CODE = "cod";
         final String M_BASEURL = "http://image.tmdb.org/t/p/w185";


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


             String poster_path = movieObject.getString("poster_path");

             String id = movieObject.getString("id");

             parsedMovieData[i] = M_BASEURL + poster_path;


         }

         return parsedMovieData;

     }


    }
