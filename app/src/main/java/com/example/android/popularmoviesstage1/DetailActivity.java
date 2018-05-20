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
        try {
            movie = parseMovieJson(movieJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (movie == null) {
            // movie data unavailable
            closeOnError();
            return;
        }

        populateUI(movie);
        Picasso.with(this)
                .load(movie.getImage())
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

        //TextView alsoKnownAsTextView = findViewById(R.id.also_known_tv);

        //set the textview with the text
        //to convert arrayliststring to charseqence https://stackoverflow.com/questions/3032342/arrayliststring-to-charsequence
        //remove the brackets using the url: https://stackoverflow.com/questions/7536154/remove-brackets-from-a-list-set-to-a-textview
        // alsoKnownAsTextView.setText(Arrays.toString(sandwich.getAlsoKnownAs().toArray(new CharSequence[sandwich.getAlsoKnownAs().size()])).replace("[", "").replace("]", ""));
        Log.i("TAG", "this is movie movie" + movie.getTitle());
        Log.i("TAG", "this is movie movie 2" + movie.getReleaseDate());
        Log.i("TAG", "this is movie movie 3" + movie.getRating());
        Log.i("TAG", "this is movie movie 4" + movie.getOverview());
        if (movie != null) {
            titleTextView.setText(movie.getTitle());
            releaseDateTextView.setText(movie.getReleaseDate());
            ratingTextView.setText(movie.getRating());
            overviewTextView.setText(movie.getOverview());

        } else  {
            closeOnError();

        }

        Log.i("TAG", "how movie movie" + movie.getTitle());
        Log.i("TAG", "how movie movie 2" + movie.getReleaseDate());
        Log.i("TAG", "how movie movie 3" + movie.getRating());
        Log.i("TAG", "how movie movie 4" + movie.getOverview());

    }





}
