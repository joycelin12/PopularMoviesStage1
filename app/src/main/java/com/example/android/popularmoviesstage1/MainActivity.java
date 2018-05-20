package com.example.android.popularmoviesstage1;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.android.popularmoviesstage1.utilities.MovieJsonUtils;
import com.example.android.popularmoviesstage1.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ItemClickListener {

    private static final int NUM_LIST_ITEMS = 20;
    private static final int NUM_COLS = 2;
    private MovieAdapter mAdapter;
    private RecyclerView mMoviesList;
    private String JSONString;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //https://stackoverflow.com/questions/40587168/simple-android-grid-example-using-recyclerview-with-gridlayoutmanager-like-the is used to
        //to build recyclerview with grid.

        /*String[] data = {"https://upload.wikimedia.org/wikipedia/commons/thumb/5/50/Grilled_ham_and_cheese_014.JPG/800px-Grilled_ham_and_cheese_014.JPG"
                , "https://upload.wikimedia.org/wikipedia/commons/c/ca/Bosna_mit_2_Bratw%C3%BCrsten.jpg",
                "https://upload.wikimedia.org/wikipedia/commons/4/48/Chivito1.jpg",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/4/4f/Club_sandwich.png/800px-Club_sandwich.png"};
       */

        String sort = "popular";

        loadMovieData(sort);

        mMoviesList = (RecyclerView) findViewById(R.id.movieGrid);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, NUM_COLS);
        mMoviesList.setLayoutManager(gridLayoutManager);

        mMoviesList.setHasFixedSize(true);

        //mAdapter = new MovieAdapter(NUM_LIST_ITEMS, data, getApplicationContext());

        //mMoviesList.setAdapter(mAdapter);

        //mAdapter = new MovieAdapter(NUM_LIST_ITEMS, data, getApplicationContext());
        //MovieAdapter.ItemClickListener mClickListener = (MovieAdapter.ItemClickListener) this;
        //mAdapter.setClickListener(this);
        //mMoviesList.setAdapter(mAdapter);


        //allow up navigation
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItemThatWasSelected = item.getItemId();
        if (menuItemThatWasSelected == R.id.action_popular) {
            Context context = MainActivity.this;
            String message = "Popular clicked";
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            String sort = "popular";
            loadMovieData(sort);
        }
        if (menuItemThatWasSelected == R.id.action_toprated) {
            Context context = MainActivity.this;
            String message = "Ratings clicked";
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            String sort = "top_rated";
            loadMovieData(sort);
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadMovieData(String sort) {
        new MovieTask().execute(sort);
    }

    @Override
    public void onItemClick(View view, int position) throws JSONException {
       // Log.i("TAG", "You clicked number " + mAdapter.getItem(position) + ", which is at cell position " + position);

        String details = MovieJsonUtils.parseSingleMovieJson(JSONString, position);
        //Log.i("TAG", "this is JSONString 2 " + details);

        launchDetailActivity(details, position);
    }

    //Got the code from T02.05 Exercise CreateAsync Task & S02.01 Exercise Networking
    public class MovieTask extends AsyncTask<String, Void, String[]> {
        // TODO (2) Override the doInBackground method to perform the query. Return the results. (Hint: You've already written the code to perform the query)
        @Override
        protected String[] doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            String sort = params[0];
            URL movieUrl = NetworkUtils.buildUrl(sort);

            try {

                JSONString = NetworkUtils.getResponseFromHttpUrl(movieUrl);

                String[] simpleJsonMovieData = MovieJsonUtils.
                        getSimpleMovieFromJson(MainActivity.this, JSONString);

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

            /*
            if (movieData != null && movieData.length > 0) {
                for (String movieString : movieData) {
                   Log.i("TAG", "this is " + movieString);
                }
            } */

            //Log.i("TAG", "this is JSONString " + JSONString);

            mAdapter = new MovieAdapter(NUM_LIST_ITEMS, movieData, getApplicationContext());
            mAdapter.setClickListener(MainActivity.this);
            mMoviesList.setAdapter(mAdapter);

        }

    }

    private void launchDetailActivity(String JSONString, int position) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_POSITION, position);
        intent.putExtra(DetailActivity.MOVIE_DETAILS, JSONString);
        startActivity(intent);
    }

}
