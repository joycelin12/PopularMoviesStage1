package com.example.android.popularmoviesstage1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.popularmoviesstage1.FavouritesContract.*;

/**
 * Created by joycelin12 on 6/10/18.
 */

public class FavouritesDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favourites.db";

    private static final int DATABASE_VERSION = 1;

    public FavouritesDbHelper (Context context) {
        super(context, DATABASE_NAME,  null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE " +
              FavouritesEntry.TABLE_NAME + " (" +
                FavouritesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FavouritesEntry.COLUMN_ID + " TEXT NOT NULL, "  +
                FavouritesEntry.COLUMN_TITLE + " TEXT NOT NULL, "  +
                FavouritesEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, "  +
                FavouritesEntry.COLUMN_RATING + " TEXT , "  +
                FavouritesEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, "  +
                FavouritesEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, "  +
                FavouritesEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                //");";
                //add unique to replace entry with the same column id
               " UNIQUE ("+ FavouritesEntry.COLUMN_ID + ") ON CONFLICT REPLACE);";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavouritesEntry.TABLE_NAME);
            onCreate(sqLiteDatabase);
    }
}
