package com.example.android.popularmoviesstage1;

import android.provider.BaseColumns;

/**
 * Created by joycelin12 on 6/10/18.
 */

public class FavouritesContract {

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


    }
}
