package com.example.android.popularmoviesstage1;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import java.net.InetSocketAddress;
import java.net.Socket;
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

        String sort = "popular";

        if (isOnline()) {
            loadMovieData(sort);
        } else  {
            String message = "There is no internet connection";
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }


        mMoviesList = (RecyclerView) findViewById(R.id.movieGrid);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, NUM_COLS);
        mMoviesList.setLayoutManager(gridLayoutManager);

        mMoviesList.setHasFixedSize(true);

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
            String sort = "popular";
            if (isOnline()) {
                loadMovieData(sort);
            } else  {
                String message = "There is no internet connection";
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
        if (menuItemThatWasSelected == R.id.action_toprated) {
             String sort = "top_rated";
            if (isOnline()) {
                loadMovieData(sort);
            } else  {
                String message = "There is no internet connection";
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadMovieData(String sort) {
        new MovieTask().execute(sort);
    }

    @Override
    public void onItemClick(View view, int position) throws JSONException {

        String details = MovieJsonUtils.parseSingleMovieJson(JSONString, position);

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

                //JSONString = NetworkUtils.getResponseFromHttpUrl(movieUrl);
                NetworkUtils test = new NetworkUtils();
                JSONString = test.run(movieUrl.toString());

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

    //referencing from https://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out/27312494#27312494
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


}
