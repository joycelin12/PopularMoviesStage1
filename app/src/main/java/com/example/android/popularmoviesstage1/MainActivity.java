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

import org.json.JSONException;

import java.util.ArrayList;

import static com.example.android.popularmoviesstage1.FavouritesContract.FavouritesEntry.COLUMN_ID;
import static com.example.android.popularmoviesstage1.FavouritesContract.FavouritesEntry.COLUMN_OVERVIEW;
import static com.example.android.popularmoviesstage1.FavouritesContract.FavouritesEntry.COLUMN_POSTER_PATH;
import static com.example.android.popularmoviesstage1.FavouritesContract.FavouritesEntry.COLUMN_RATING;
import static com.example.android.popularmoviesstage1.FavouritesContract.FavouritesEntry.COLUMN_RELEASE_DATE;
import static com.example.android.popularmoviesstage1.FavouritesContract.FavouritesEntry.COLUMN_TIMESTAMP;
import static com.example.android.popularmoviesstage1.FavouritesContract.FavouritesEntry.COLUMN_TITLE;
import static com.example.android.popularmoviesstage1.FavouritesContract.FavouritesEntry.TABLE_NAME;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ItemClickListener,
        MovieTask.MovieResponse {

    private static final int NUM_COLS = 2;
    private RecyclerView mMoviesList;
    private SQLiteDatabase mDb;
    private MovieAdapter mAdapter;
    private ArrayList<Movie> moviesList;
    public Boolean favourite = false;
    private MovieAdapter.ItemClickListener mClickListener;


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

        // Create a DB helper (this will create the DB if run for the first time)
        FavouritesDbHelper dbHelper = new FavouritesDbHelper(this);

        // Keep a reference to the mDb until paused or killed. Get a writable database
        // because you will be adding restaurant customers
        mDb = dbHelper.getWritableDatabase();


        if(savedInstanceState != null) {
            this.moviesList = savedInstanceState.getParcelableArrayList("movies");
            mAdapter = new MovieAdapter(this.moviesList.size(), this.moviesList, this);
            mAdapter.setClickListener(this.mClickListener);
            this.mMoviesList.setAdapter(mAdapter);
        } else {
            String sort = "popular";

            if (isOnline()) {
                loadMovieData(sort);
            } else  {
                String message = "There is no internet connection";
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }



    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            outState.putParcelableArrayList("movies", moviesList);


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
                moviesList = getAllMovies();


            mAdapter = new MovieAdapter(moviesList.size(), moviesList, this);
                mAdapter.setClickListener(this);
                mMoviesList.setAdapter(mAdapter);

        }

        return super.onOptionsItemSelected(item);
    }

    private void loadMovieData(String sort) {

        new MovieTask(this,  this, mMoviesList, this).execute(sort);

    }

    @Override
    public void processFinish(ArrayList<Movie> output) {
        moviesList = output;
    }

    @Override
    public void onItemClick(View view, int position) throws JSONException {

            Movie movie = null;

            movie = moviesList.get(position);

            launchDetailActivity(movie, position, favourite);


    }


    private void launchDetailActivity(Movie movie, int position, boolean favourite) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_POSITION, position);
        intent.putExtra(DetailActivity.MOVIE_DETAILS, movie);
        intent.putExtra(DetailActivity.MOVIE_FAV, favourite);
        startActivity(intent);
    }


    //referencing from https://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out/27312494#27312494
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private ArrayList<Movie> getAllMovies() {

        favourite = true;

        ArrayList<Movie> movies = new ArrayList<>();

        Cursor cursor = mDb.query(
                TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                COLUMN_TIMESTAMP
        );

        //https://www.androidhive.info/2011/11/android-sqlite-database-tutorial/
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Movie movie = new Movie();
                movie.setId(cursor.getString(cursor.getColumnIndex(COLUMN_ID)));
                movie.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
                movie.setRating(cursor.getString(cursor.getColumnIndex(COLUMN_RATING)));
                movie.setOverview(cursor.getString(cursor.getColumnIndex(COLUMN_OVERVIEW)));
                movie.setRelease_date(cursor.getString(cursor.getColumnIndex(COLUMN_RELEASE_DATE)));
                movie.setPoster_path(cursor.getString(cursor.getColumnIndex(COLUMN_POSTER_PATH)));

                movies.add(movie);
            } while (cursor.moveToNext());
        }

        // close db connection
        cursor.close();
        mDb.close();

        // return movies list
        return movies;

    }




}
