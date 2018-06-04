package com.example.android.popularmoviesstage1;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmoviesstage1.Model.Movie;
import com.example.android.popularmoviesstage1.utilities.MovieJsonUtils;
import com.example.android.popularmoviesstage1.utilities.TrailerJsonUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;


import butterknife.BindView;
import butterknife.ButterKnife;


import static com.example.android.popularmoviesstage1.utilities.MovieJsonUtils.parseJSON;
import static com.example.android.popularmoviesstage1.utilities.MovieJsonUtils.parseMovieJson;

public class DetailActivity extends AppCompatActivity implements  TrailerAdapter.ItemClickListener,
        MovieTrailerTask.TrailerResponse {

    public static final String EXTRA_POSITION = "extra_position";
    public static final String MOVIE_DETAILS = "movie_details";
    public static final String MOVIE_TRAILERS = "movie_trailers";
    private static final int DEFAULT_POSITION = -1;
    private static final int NUM_COLS = 1;
    private RecyclerView mTrailersList;
    private TrailerAdapter tAdapter;
    private TrailerAdapter.ItemClickListener mClickListener;
    private String TrailerString;
    public String[] movieTrailerData;



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

        String movieJson = intent.getStringExtra(MOVIE_DETAILS);
        if(movieJson == null) {
            closeOnError();
            return;
        }

        Movie movie = null;

        movie = parseJSON(movieJson);

        if (movie == null) {
            // movie data unavailable
            closeOnError();
            return;
        }

        populateUI(movie);
        //Log.i("TAG", movie.getPoster_path());
        Picasso.with(this)
                .load(movie.getPoster_path())
                .placeholder(R.drawable.user_placeholder)
                .error(R.drawable.user_placeholder_error)
                .into(posterPic);


        setTitle(movie.getTitle());

        mTrailersList = (RecyclerView) findViewById(R.id.movieTrailersGrid);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, NUM_COLS);
        mTrailersList.setLayoutManager(gridLayoutManager);

        mTrailersList.setHasFixedSize(true);

        String[] videos = new String[2];
        videos[0] = movie.getId();
        videos[1] ="videos";
        if (isOnline()) {
            new MovieTrailerTask(this,  this, mTrailersList, this).execute(videos);
        } else {
            String message = "There is no internet connection";
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();

        }



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

        Log.i("Tag", "tap tap");

        /* String details = MovieJsonUtils.parseSingleMovieJson(JSONString, position);
        Movie movie = parseJSON(details);
        String[] videos = new String[2];
        videos[0] = movie.getId();
        videos[1] ="videos";

        new MovieTrailerTask(this).execute(videos);*/

        //launchDetailActivity(details, position, TrailerString);


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






}
