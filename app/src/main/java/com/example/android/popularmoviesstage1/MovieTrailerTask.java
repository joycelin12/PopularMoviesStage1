package com.example.android.popularmoviesstage1;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.android.popularmoviesstage1.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

import static com.example.android.popularmoviesstage1.utilities.TrailerJsonUtils.getTrailersFromJson;


/**
 * Created by joycelin12 on 6/3/18.
 */

public class MovieTrailerTask extends AsyncTask<String[], Void, String[]> {

    public TrailerResponse trailers = null;
    private String TrailerString;
    private Context context;
    private RecyclerView mTrailersList;
    private TrailerAdapter tAdapter;
    private TrailerAdapter.ItemClickListener mClickListener;


    public MovieTrailerTask (Context context, TrailerAdapter.ItemClickListener itemClickListener, RecyclerView mTrailersList,
                             TrailerResponse trailers) {
        this.context = context;
        this.trailers = trailers;
        this.mClickListener = itemClickListener;
        this.mTrailersList = mTrailersList;

    }

    @Override
    protected String[] doInBackground(String[]... params) {

        String[] type = params[0];
        URL trailerUrl = NetworkUtils.buildUrl2(type);

        try {

            //JSONString = NetworkUtils.getResponseFromHttpUrl(movieUrl);
            NetworkUtils trailer = new NetworkUtils();

            TrailerString = trailer.run(trailerUrl.toString());

            trailers.processTrailer(TrailerString);
            //    String[] simpleJsonMovieData = MovieJsonUtils.
            //            getSimpleMovieFromJson(MainActivity.this, JSONString);

            String[] trailerData  = getTrailersFromJson(this.context, TrailerString);

            //return trailerData;
            return trailerData;

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
    protected void onPostExecute(String[] trailerData) {


        //  mAdapter = new MovieAdapter(NUM_LIST_ITEMS, movieData, MainActivity.this);
        //  mAdapter.setClickListener(MainActivity.this);
        Log.i("TAG8", trailerData[0].toString());
        Log.i("TAG9", trailerData[1].toString());

        tAdapter = new TrailerAdapter(trailerData.length, trailerData, this.context);
        tAdapter.setClickListener(this.mClickListener);
        this.mTrailersList.setAdapter(tAdapter);


    }

    public interface TrailerResponse{
        void processTrailer(String output);
    }
}
