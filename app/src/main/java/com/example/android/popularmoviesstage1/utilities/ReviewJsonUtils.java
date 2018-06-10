package com.example.android.popularmoviesstage1.utilities;

import android.content.Context;

import com.example.android.popularmoviesstage1.Model.Review;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;

/**
 * Created by joycelin12 on 6/10/18.
 */

public class ReviewJsonUtils {

    private static final String OWM_MESSAGE_CODE = "cod";
    private static final String M_RESULTS = "results";

    /**This method parses JSON from a web response and returns an array of Strings of movie posters
     * @param ReviewJsonStr JSON response from server
     *
     * @return Array of Strings describing weather data
     *
     * @throws JSONException If JSON data cannot be properly parsed
     */
    public static ArrayList<Review> getReviewsFromJson(Context context, String ReviewJsonStr)
            throws JSONException {


        JSONObject reviewJson = new JSONObject(ReviewJsonStr);

         /* Is there an error? */
        if (reviewJson.has(OWM_MESSAGE_CODE)) {
            int errorCode = reviewJson.getInt(OWM_MESSAGE_CODE);

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

        JSONArray reviewArray = reviewJson.getJSONArray(M_RESULTS);

        ArrayList<Review> parsedReviewsData = new ArrayList<>();

        for (int i = 0; i < reviewArray.length(); i++) {

            //Get the JSON object representing results
            JSONObject reviewObject =
                    reviewArray.getJSONObject(i);

            //String poster_path = trailerObject.getString(JSON_IMAGE_KEY);

            //parsedTrailersData[i] = M_BASEURL + poster_path + M_ENDURL;

            Gson gson = new GsonBuilder().create();
            Review review = gson.fromJson(reviewObject.toString(), Review.class);
            parsedReviewsData.add(new Review(review.getAuthor(),review.getContent(),review.getId(),review.getUrl()));


        }

        return parsedReviewsData;

    }
}
