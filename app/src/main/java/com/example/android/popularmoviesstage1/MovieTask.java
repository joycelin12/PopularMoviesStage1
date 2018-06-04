package com.example.android.popularmoviesstage1;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;


import com.example.android.popularmoviesstage1.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

import static com.example.android.popularmoviesstage1.utilities.MovieJsonUtils.*;

/**
 * Created by joycelin12 on 6/2/18.
 */

//Got the code from T02.05 Exercise CreateAsync Task & S02.01 Exercise Networking
//Use this website to move Asynctask to another class https://stackoverflow.com/questions/34619188/why-i-cant-set-adapter-in-asynctask-for-listview-from-another-class
//Use this website to bring output of asynctask to mainactivity  https://stackoverflow.com/questions/12575068/how-to-get-the-result-of-onpostexecute-to-main-activity-because-asynctask-is-a
public class MovieTask extends AsyncTask<String, Void, String[]>  {
    // TODO (2) Override the doInBackground method to perform the query. Return the results. (Hint: You've already written the code to perform the query)

    private static final int NUM_LIST_ITEMS = 20;
    private static final int NUM_COLS = 2;
    private MovieAdapter mAdapter;
    private String JSONString;
    private RecyclerView mMoviesList;
    private Context context;
    private MovieAdapter.ItemClickListener mClickListener;
    public MovieResponse movies = null;

    public MovieTask(Context context, MovieAdapter.ItemClickListener itemClickListener, RecyclerView mMoviesList,
                     MovieResponse movies){
        this.context = context;
        this.mClickListener = itemClickListener;
        this.mMoviesList = mMoviesList;
        this.movies = movies;

    }


    @Override
    protected String[] doInBackground(String... params) {

        if (params.length == 0) {
            return null;
        }

        String sort = params[0];
        URL movieUrl = NetworkUtils.buildUrl(sort);

        try {

            //JSONString = NetworkUtils.getResponseFromHttpUrl(movieUrl);
            NetworkUtils test = new NetworkUtils();
            JSONString = test.run(movieUrl.toString());
            movies.processFinish(JSONString);
        //    String[] simpleJsonMovieData = MovieJsonUtils.
        //            getSimpleMovieFromJson(MainActivity.this, JSONString);

            String[] simpleJsonMovieData  = getSimpleMovieFromJson(this.context, JSONString);

            return simpleJsonMovieData;

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
    protected void onPostExecute(String[] movieData) {


      //  mAdapter = new MovieAdapter(NUM_LIST_ITEMS, movieData, MainActivity.this);
      //  mAdapter.setClickListener(MainActivity.this);

        mAdapter = new MovieAdapter(NUM_LIST_ITEMS, movieData, this.context);
        mAdapter.setClickListener(this.mClickListener);
        this.mMoviesList.setAdapter(mAdapter);

    }

    public interface MovieResponse{
        void processFinish(String output);
    }



}

