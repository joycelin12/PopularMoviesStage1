package com.example.android.popularmoviesstage1;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by joycelin12 on 6/10/18.
 */

public class FavouritesContract {

    //referencing from https://medium.com/@sanjeevy133/an-idiots-guide-to-android-content-providers-part-1-970cba5d7b42
    public static final String CONTENT_AUTHORITY = "com.example.android.popularmoviesstage1";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    //Create an inner class that implements BaseColumns interface
    public static final class FavouritesEntry implements BaseColumns {

        public static final String TABLE_NAME = "movies";
        public static final String COLUMN_ID ="movieId";
        public static final String COLUMN_TITLE ="title";
        public static final String COLUMN_RELEASE_DATE ="release_date";
        public static final String COLUMN_RATING ="rating";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_TIMESTAMP = "timestamp";


        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(TABLE_NAME)
                .build();

        public static Uri buildMoviesUriWithId(String id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(id)
                    .build();
        }




    }
}
