package com.example.android.popularmoviesstage1;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import com.example.android.popularmoviesstage1.Model.Review;
import com.example.android.popularmoviesstage1.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import static com.example.android.popularmoviesstage1.utilities.ReviewJsonUtils.getReviewsFromJson;

/**
 * Created by joycelin12 on 6/10/18.
 */

public class MovieReviewTask extends AsyncTask< String[], Void, ArrayList<Review>> {

    public ReviewResponse reviews = null;
    private String ReviewString;
    private Context context;
    private RecyclerView mReviewsList;
    private ReviewAdapter rAdapter;
    private ReviewAdapter.ItemClickListener mClickListener;


    public MovieReviewTask (Context context, ReviewAdapter.ItemClickListener itemClickListener, RecyclerView mReviewsList,
                             ReviewResponse reviews) {
        this.context = context;
        this.reviews = reviews;
        this.mClickListener = itemClickListener;
        this.mReviewsList = mReviewsList;

    }

    @Override
    protected ArrayList<Review> doInBackground(String[]... params) {

        String[] type = params[0];
        URL reviewUrl = NetworkUtils.buildUrl2(type);

        try {

            //JSONString = NetworkUtils.getResponseFromHttpUrl(movieUrl);
            NetworkUtils review = new NetworkUtils();

            ReviewString = review.run(reviewUrl.toString());

            reviews.processReview(ReviewString);
            ArrayList<Review> reviewData  = getReviewsFromJson(this.context, ReviewString);


            return reviewData;

        } catch (IOException e) {
            e.printStackTrace();
            return null;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

    // TODO (3) Override onPostExecute to display the results in the GridView
    @Override
    protected void onPostExecute(ArrayList<Review> reviewData) {


        rAdapter = new ReviewAdapter(reviewData.size(), reviewData, this.context);
        rAdapter.setClickListener(this.mClickListener);
        this.mReviewsList.setAdapter(rAdapter);


    }

    public interface ReviewResponse{
        void processReview(String output);
    }

}
