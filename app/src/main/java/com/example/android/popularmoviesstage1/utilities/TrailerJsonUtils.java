package com.example.android.popularmoviesstage1.utilities;

import android.content.Context;
import android.util.Log;

import com.example.android.popularmoviesstage1.Model.Movie;
import com.example.android.popularmoviesstage1.Model.Trailer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;

/**
 * Created by joycelin12 on 6/3/18.
 */

public class TrailerJsonUtils {

    private static final String OWM_MESSAGE_CODE = "cod";
    private static final String M_RESULTS = "results";
    private static String M_BASEURL = "https://img.youtube.com/vi/";
    private static final String JSON_IMAGE_KEY = "key";
    private static String M_ENDURL = "/0.jpg";


    /**This method parses JSON from a web response and returns an array of Strings of movie posters
     * @param TrailerJsonStr JSON response from server
     *
     * @return Array of Strings describing weather data
     *
     * @throws JSONException If JSON data cannot be properly parsed
     */
    public static ArrayList<Trailer> getTrailersFromJson(Context context, String TrailerJsonStr)
            throws JSONException {

        //string array to hold each movie string
        //String[] parsedTrailersData = null;

        JSONObject trailerJson = new JSONObject(TrailerJsonStr);

         /* Is there an error? */
        if (trailerJson.has(OWM_MESSAGE_CODE)) {
            int errorCode = trailerJson.getInt(OWM_MESSAGE_CODE);

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

        JSONArray trailerArray = trailerJson.getJSONArray(M_RESULTS);

        ArrayList<Trailer> parsedTrailersData = new ArrayList<>();


        for (int i = 0; i < trailerArray.length(); i++) {

            //Get the JSON object representing results
            JSONObject trailerObject =
                    trailerArray.getJSONObject(i);

            //String poster_path = trailerObject.getString(JSON_IMAGE_KEY);

            //parsedTrailersData[i] = M_BASEURL + poster_path + M_ENDURL;

            Gson gson = new GsonBuilder().create();
            Trailer trailer = gson.fromJson(trailerArray.getJSONObject(i).toString(), Trailer.class);
            parsedTrailersData.add(new Trailer(trailer.getId(),trailer.getKey(),trailer.getName(),trailer.getSize()));
            

        }

        return parsedTrailersData;

    }



}
