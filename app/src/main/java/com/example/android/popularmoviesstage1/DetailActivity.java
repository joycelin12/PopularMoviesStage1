package com.example.android.popularmoviesstage1;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmoviesstage1.Model.Movie;
import com.example.android.popularmoviesstage1.Model.Trailer;
import com.example.android.popularmoviesstage1.utilities.MovieJsonUtils;
import com.example.android.popularmoviesstage1.utilities.TrailerJsonUtils;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


import static com.example.android.popularmoviesstage1.FavouritesContract.FavouritesEntry.COLUMN_ID;
import static com.example.android.popularmoviesstage1.FavouritesContract.FavouritesEntry.COLUMN_TIMESTAMP;
import static com.example.android.popularmoviesstage1.FavouritesContract.FavouritesEntry.TABLE_NAME;
import static com.example.android.popularmoviesstage1.utilities.MovieJsonUtils.parseJSON;
import static com.example.android.popularmoviesstage1.utilities.MovieJsonUtils.parseMovieJson;
import static com.example.android.popularmoviesstage1.utilities.TrailerJsonUtils.parseSingleTrailerJson;

public class DetailActivity extends AppCompatActivity implements TrailerAdapter.ItemClickListener,
        MovieTrailerTask.TrailerResponse, ReviewAdapter.ItemClickListener,
        MovieReviewTask.ReviewResponse  {

    public static final String EXTRA_POSITION = "extra_position";
    public static final String MOVIE_DETAILS = "movie_details";
    public static final String MOVIE_FAV = "movie_favourite";
    private static final int DEFAULT_POSITION = -1;
    private static final int NUM_COLS = 1;
    private RecyclerView mTrailersList;
    private RecyclerView mReviewsList;
    private String TrailerString;
    private String ReviewString;
    public static final String VID_URL = "http://www.youtube.com/watch?v=";
    private SQLiteDatabase mDb;
    private static String M_BASEURL = "http://image.tmdb.org/t/p/w185";
    private RecyclerView mMoviesList;
    private MovieAdapter mAdapter;
    private ArrayList<Movie> moviesList;



    //get all the textview by their id
    @BindView(R.id.title_detail) TextView titleTextView;
    @BindView(R.id.release_date_detail) TextView releaseDateTextView;
    @BindView(R.id.rating_detail) TextView ratingTextView;
    @BindView(R.id.overview_detail) TextView overviewTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        ImageView posterPic = findViewById(R.id.image_iv);

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        final Movie movie = (Movie) getIntent().getParcelableExtra(MOVIE_DETAILS);

        if (movie == null) {
            // movie data unavailable
            closeOnError();
            return;
        }

        populateUI(movie);
        //Log.i("TAG", movie.getPoster_path());
        //referencing this for image cache https://stackoverflow.com/questions/23391523/load-images-from-disk-cache-with-picasso-if-offline
        Picasso.with(this)
                .load(M_BASEURL + movie.getPoster_path())
                .placeholder(R.drawable.user_placeholder)
                .error(R.drawable.user_placeholder_error)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(posterPic);


        setTitle(movie.getTitle());

        //recyclerview for trailer
        mTrailersList = (RecyclerView) findViewById(R.id.movieTrailersGrid);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, NUM_COLS);
        mTrailersList.setLayoutManager(gridLayoutManager);

        mTrailersList.setHasFixedSize(true);


        String[] videos = new String[2];
        videos[0] = movie.getId();
        videos[1] ="videos";

        //recyclerview for review
        mReviewsList = (RecyclerView) findViewById(R.id.movieReviewsGrid);

        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(this, NUM_COLS);
        mReviewsList.setLayoutManager(gridLayoutManager2);

        mReviewsList.setHasFixedSize(true);


        String[] reviews = new String[2];
        reviews[0] = movie.getId();
        reviews[1] ="reviews";

        if (isOnline()) {
            new MovieTrailerTask(this,  this, mTrailersList, this).execute(videos);
            new MovieReviewTask(this,  this, mReviewsList, this).execute(reviews);

        } else {
            String message = "There is no internet connection";
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();

        }
        // Create a DB helper (this will create the DB if run for the first time)
        FavouritesDbHelper dbHelper = new FavouritesDbHelper(this);

        // Keep a reference to the mDb until paused or killed. Get a writable database
        // because you will be adding restaurant customers
        mDb = dbHelper.getWritableDatabase();

        //favourite
        // from the website https://alvinalexander.com/source-code/android/android-checkbox-listener-setoncheckedchangelisteneroncheckedchangelistener-exam
        CheckBox checkBox = (CheckBox) findViewById(R.id.favbutton);

        //query the database to check the checkbox if is favourite
        if(getFav(movie.getId())) {
           checkBox.setChecked(true);
        }


        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // update your model (or other business logic) based on isChecked
                if (isChecked) {
                    addMovie(movie);
                    //Log.i("Tag", "add to database");

                } else {
                    //Log.i("Tag", "remove from database");
                    removeMovie(movie.getId());

                }
            }
        });


    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Movie movie) {

            if (movie != null) {
            titleTextView.setText(movie.getTitle());
            releaseDateTextView.setText(movie.getReleaseDate());
            ratingTextView.setText(movie.getRating());
            overviewTextView.setText(movie.getOverview());

        } else  {
            closeOnError();

        }


    }

    @Override
    public void onItemClick2(View view, int position) throws JSONException {

        Trailer trailer = parseSingleTrailerJson(TrailerString, position);
        //referencing from the website: https://stackoverflow.com/questions/574195/android-youtube-app-play-video-intent
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + trailer.getKey()));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(VID_URL + trailer.getKey()));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }


    }

    @Override
    public void onItemClick3(View view, int position) throws JSONException {

       Log.i("TAG","testing");


    }

    //referencing from https://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out/27312494#27312494
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public void processTrailer(String output) {
        TrailerString = output;
    }

    @Override
    public void processReview(String output) {
        ReviewString = output;
    }

    private Uri addMovie(Movie movie) {
        //create ContentValues to pass values onto insert query
        ContentValues cv = new ContentValues();
        //call put to insert name value with the key COLUMN_TITLE
        cv.put(COLUMN_ID, movie.getId());
        cv.put(FavouritesContract.FavouritesEntry.COLUMN_TITLE, movie.getTitle());
        cv.put(FavouritesContract.FavouritesEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        cv.put(FavouritesContract.FavouritesEntry.COLUMN_RATING, movie.getRating());
        cv.put(FavouritesContract.FavouritesEntry.COLUMN_OVERVIEW, movie.getOverview());
        cv.put(FavouritesContract.FavouritesEntry.COLUMN_POSTER_PATH, movie.getPoster_path());
        //call insert to run an insert query on TABLE_NAME with content values
        return getContentResolver().insert(FavouritesContract.FavouritesEntry.CONTENT_URI, cv);


    }

    private boolean removeMovie(String id) {

     int rows = getContentResolver()
                .delete(FavouritesContract.FavouritesEntry.buildMoviesUriWithId(id),COLUMN_ID, new String[]{id});

        if (rows > 0) {
            return true;
        } else {
            return false;
        }


    }

    private boolean getFav(String id) {

        Cursor cursor = getContentResolver()
                .query(FavouritesContract.FavouritesEntry.buildMoviesUriWithId(id),null,null,null,null);


          /*  Cursor cursor = mDb.query(TABLE_NAME,
                    new String[]{COLUMN_ID},
                    COLUMN_ID + "=?",
                    new String[]{String.valueOf(id)}, null, null, null, null); */

            if (cursor != null && cursor.moveToFirst()) {
                return true;
            } else {
                return false;
            }


    }


}
