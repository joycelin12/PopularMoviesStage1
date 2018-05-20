package com.example.android.popularmoviesstage1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmoviesstage1.Model.Movie;
import com.squareup.picasso.Picasso;

import org.json.JSONException;


import butterknife.BindView;
import butterknife.ButterKnife;


import static com.example.android.popularmoviesstage1.utilities.MovieJsonUtils.parseJSON;
import static com.example.android.popularmoviesstage1.utilities.MovieJsonUtils.parseMovieJson;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    public static final String MOVIE_DETAILS = "movie_details";
    private static final int DEFAULT_POSITION = -1;

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
        /*
        try {

            movie = parseMovieJson(movieJson);

        } catch (JSONException e) {
            e.printStackTrace();
        }*/

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





}
