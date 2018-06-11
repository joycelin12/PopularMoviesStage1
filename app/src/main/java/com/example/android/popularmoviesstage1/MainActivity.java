package com.example.android.popularmoviesstage1;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.android.popularmoviesstage1.Model.Movie;
import com.example.android.popularmoviesstage1.utilities.MovieJsonUtils;

import org.json.JSONException;

import static com.example.android.popularmoviesstage1.FavouritesContract.FavouritesEntry.COLUMN_TIMESTAMP;
import static com.example.android.popularmoviesstage1.FavouritesContract.FavouritesEntry.TABLE_NAME;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ItemClickListener,
        MovieTask.MovieResponse, MovieTrailerTask.TrailerResponse {

    private static final int NUM_COLS = 2;
    private RecyclerView mMoviesList;
    private String JSONString;
    private String TrailerString;
    private SQLiteDatabase mDb;
    private MovieAdapter mAdapter;


    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //https://stackoverflow.com/questions/40587168/simple-android-grid-example-using-recyclerview-with-gridlayoutmanager-like-the is used to
        //to build recyclerview with grid.

        mMoviesList = (RecyclerView) findViewById(R.id.movieGrid);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, NUM_COLS);
        mMoviesList.setLayoutManager(gridLayoutManager);

        mMoviesList.setHasFixedSize(true);

        //allow up navigation
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String sort = "popular";

        if (isOnline()) {
            loadMovieData(sort);
        } else  {
            String message = "There is no internet connection";
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }

        // Create a DB helper (this will create the DB if run for the first time)
        FavouritesDbHelper dbHelper = new FavouritesDbHelper(this);

        // Keep a reference to the mDb until paused or killed. Get a writable database
        // because you will be adding restaurant customers
        mDb = dbHelper.getWritableDatabase();


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

        if (menuItemThatWasSelected == R.id.action_favourites) {

                String message = "This is the list of favourite movies";
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                //Run the getAllMovies
                Cursor cursor = getAllMovies();
                mAdapter = new MovieAdapter(cursor.getCount(), cursor, this);
                mMoviesList.setAdapter(mAdapter);

        }

        return super.onOptionsItemSelected(item);
    }

    private void loadMovieData(String sort) {

        new MovieTask(this,  this, mMoviesList, this).execute(sort);

    }

    @Override
    public void processFinish(String output) {
        JSONString = output;
    }

    @Override
    public void processTrailer(String output) {
        TrailerString = output;
    }


    @Override
    public void onItemClick(View view, int position) throws JSONException {

            String details = MovieJsonUtils.parseSingleMovieJson(JSONString, position);

            launchDetailActivity(details, position, TrailerString);


    }


    private void launchDetailActivity(String JSONString, int position, String TrailerString) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_POSITION, position);
        intent.putExtra(DetailActivity.MOVIE_DETAILS, JSONString);
        intent.putExtra(DetailActivity.MOVIE_TRAILERS, TrailerString);
        startActivity(intent);
    }


    //referencing from https://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out/27312494#27312494
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private Cursor getAllMovies() {

        return mDb.query(
                TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                COLUMN_TIMESTAMP
        );

    }




}
