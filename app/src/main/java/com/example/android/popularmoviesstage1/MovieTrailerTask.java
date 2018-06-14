package com.example.android.popularmoviesstage1;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.android.popularmoviesstage1.Model.Movie;
import com.example.android.popularmoviesstage1.Model.Trailer;
import com.example.android.popularmoviesstage1.utilities.NetworkUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import static com.example.android.popularmoviesstage1.utilities.TrailerJsonUtils.getTrailersFromJson;


/**
 * Created by joycelin12 on 6/3/18.
 */

public class MovieTrailerTask extends AsyncTask< String[], Void, ArrayList<Trailer>> {

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
    protected ArrayList<Trailer> doInBackground(String[]... params) {

        String[] type = params[0];
        URL trailerUrl = NetworkUtils.buildUrl2(type);

        try {

            NetworkUtils trailer = new NetworkUtils();

            TrailerString = trailer.run(trailerUrl.toString());

            trailers.processTrailer(TrailerString);
            ArrayList<Trailer> trailerData  = getTrailersFromJson(this.context, TrailerString);

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
    protected void onPostExecute(ArrayList<Trailer> trailerData) {


        tAdapter = new TrailerAdapter(trailerData.size(), trailerData, this.context);
        tAdapter.setClickListener(this.mClickListener);
        this.mTrailersList.setAdapter(tAdapter);


    }

    public interface TrailerResponse{
        void processTrailer(String output);
    }
}
