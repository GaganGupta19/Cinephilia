package com.moviephilia.MovieDB;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import java.util.HashMap;
/**
 * Created by Gagan on 6/11/2016.
 */
public class MoviesContentProvider extends ContentProvider {


    private static final String AUTHORITY = "in.udacity.gagan.cinephilia";
    private static final String BASE_PATH = "movie";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
    private DatabaseHelper mDatabaseHelper;
    private static HashMap<String, String> PROJECTION_MAP;

    @Override
    public boolean onCreate() {
        mDatabaseHelper = new DatabaseHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();


        queryBuilder.setTables(DatabaseHelper.TABLE_MOVIE_DETAILS);

        queryBuilder.setProjectionMap(PROJECTION_MAP);

        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        SQLiteDatabase sqlDB = mDatabaseHelper.getWritableDatabase();
        long id;
        id = sqlDB.insert(DatabaseHelper.TABLE_MOVIE_DETAILS, null, values);
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase sqlDB = mDatabaseHelper.getWritableDatabase();
        int rowsDeleted;
        rowsDeleted = sqlDB.delete(DatabaseHelper.TABLE_MOVIE_DETAILS, selection,
                selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        SQLiteDatabase sqlDB = mDatabaseHelper.getWritableDatabase();
        int rowsUpdated;
        rowsUpdated = sqlDB.update(DatabaseHelper.TABLE_MOVIE_DETAILS,
                values,
                selection,
                selectionArgs);

        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }
}
