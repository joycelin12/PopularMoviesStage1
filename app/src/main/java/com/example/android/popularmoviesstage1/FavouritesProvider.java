package com.example.android.popularmoviesstage1;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.example.android.popularmoviesstage1.FavouritesContract.FavouritesEntry.COLUMN_ID;
import static com.example.android.popularmoviesstage1.FavouritesContract.FavouritesEntry.TABLE_NAME;

/**
 * Created by joycelin12 on 6/16/18.
 * referencing from https://medium.com/@sanjeevy133/an-idiots-guide-to-android-content-providers-part-2-7ccfbc88d75c
 */

public class FavouritesProvider extends ContentProvider {

    private FavouritesDbHelper mHelper;
    public static final int CODE_MOVIES = 100;
    public static final int CODE_MOVIES_WITH_ID = 101;

    private static final UriMatcher mUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FavouritesContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, FavouritesContract.FavouritesEntry.TABLE_NAME, CODE_MOVIES);

        matcher.addURI(authority, FavouritesContract.FavouritesEntry.TABLE_NAME + "/#", CODE_MOVIES_WITH_ID);

        return matcher;

    }


        @Override
    public boolean onCreate() {


        mHelper = new FavouritesDbHelper(getContext());
        if (mHelper != null)
            return true;
        else
            return false;

    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        Cursor cursor;

        switch (mUriMatcher.match(uri)) {

            case CODE_MOVIES_WITH_ID: {
                String _ID = uri.getLastPathSegment();
                String[] selectionArguments = new String[]{_ID};

                cursor = mHelper.getReadableDatabase().query(
                        FavouritesContract.FavouritesEntry.TABLE_NAME,
                        projection,
                        FavouritesContract.FavouritesEntry.COLUMN_ID + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);

                break;
            }

            case CODE_MOVIES: {
                cursor = mHelper.getReadableDatabase().query(
                        FavouritesContract.FavouritesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;


    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        final SQLiteDatabase db = mHelper.getWritableDatabase();

        switch (mUriMatcher.match(uri)) {
            case CODE_MOVIES:

                long _id = db.insert(FavouritesContract.FavouritesEntry.TABLE_NAME, null, values);

            /* if _id is equal to -1 insertion failed */
                if (_id != -1) {
                /*
                 * This will help to broadcast that database has been changed,
                 * and will inform entities to perform automatic update.
                 */
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return FavouritesContract.FavouritesEntry.buildMoviesUriWithId(Long.toString(_id));

            default:
                return null;
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        final SQLiteDatabase db = mHelper.getWritableDatabase();

        int numRowsDeleted;

        String _ID = uri.getLastPathSegment();
        String[] selectionArguments = new String[]{_ID};

        switch(mUriMatcher.match(uri)) {

            case CODE_MOVIES_WITH_ID:

                numRowsDeleted = db.delete(TABLE_NAME, COLUMN_ID + "=?", selectionArguments);

                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }

            if (numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
            }

        return numRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
