package in.udacity.gagan.cinephilia.MovieDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;
import in.udacity.gagan.cinephilia.Classes.MovieDetail;

/**
 * Created by Gagan on 6/11/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 2;

    public static final String DATABASE_NAME = "movieManager";

    public static final String TABLE_MOVIE_DETAILS = "moviesDetails";

    public static final String KEY_ID = "id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_RATING = "rating";
    public static final String KEY_GENRE = "genre";
    public static final String KEY_DATE = "date";
    public static final String KEY_STATUS = "status";
    public static final String KEY_OVERVIEW = "overview";
    public static final String KEY_BACKDROP = "backdrop";
    public static final String KEY_VOTE_COUNT = "vote_count";
    public static final String KEY_TAG_LINE = "tag_line";
    public static final String KEY_RUN_TIME = "runtime";
    public static final String KEY_LANGUAGE = "language";
    public static final String KEY_POPULARITY = "popularity";
    public static final String KEY_POSTER = "poster";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MOVIES_TABLE = "CREATE TABLE " + TABLE_MOVIE_DETAILS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TITLE + " TEXT,"
                + KEY_RATING + " TEXT,"
                + KEY_GENRE + " TEXT,"
                + KEY_DATE + " TEXT,"
                + KEY_STATUS + " TEXT,"
                + KEY_OVERVIEW + " TEXT,"
                + KEY_BACKDROP + " TEXT,"
                + KEY_VOTE_COUNT + " TEXT,"
                + KEY_TAG_LINE + " TEXT,"
                + KEY_RUN_TIME + " TEXT,"
                + KEY_LANGUAGE + " TEXT,"
                + KEY_POPULARITY + " TEXT,"
                + KEY_POSTER + " TEXT" + ")";
        db.execSQL(CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIE_DETAILS);
        onCreate(db);
    }


    public void addMovie(MovieDetail movie) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, movie.getId());
        values.put(KEY_TITLE, movie.getTitle());
        values.put(KEY_RATING, movie.getmVotecount());
        values.put(KEY_GENRE, movie.getGenre());
        values.put(KEY_DATE, movie.getReleasedate());
        values.put(KEY_STATUS, movie.getStatus());
        values.put(KEY_OVERVIEW, movie.getOverview());
        values.put(KEY_BACKDROP, movie.getBackdrop_path());
        values.put(KEY_VOTE_COUNT, movie.getmVotecount());
        values.put(KEY_TAG_LINE, movie.getmTagline());
        values.put(KEY_RUN_TIME, movie.getmRuntime());
        values.put(KEY_LANGUAGE, movie.getmLanguage());
        values.put(KEY_POPULARITY, movie.getmPopularity());
        values.put(KEY_POSTER, movie.getPoster_path());

        db.insert(TABLE_MOVIE_DETAILS, null, values);
        db.close();

    }

    public MovieDetail getMovie(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_MOVIE_DETAILS, new String[]{KEY_ID,
                        KEY_TITLE, KEY_RATING, KEY_GENRE, KEY_DATE,
                        KEY_STATUS, KEY_OVERVIEW, KEY_POSTER, KEY_BACKDROP,
                        KEY_TAG_LINE, KEY_LANGUAGE, KEY_RUN_TIME, KEY_POPULARITY, KEY_VOTE_COUNT}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        MovieDetail movie = new MovieDetail(cursor.getString(0),
                cursor.getString(1), cursor.getString(2), cursor.getString(3),
                cursor.getString(4), cursor.getString(5), cursor.getString(6),
                cursor.getString(7), cursor.getString(8), cursor.getString(9),
                cursor.getString(10), cursor.getString(11), cursor.getString(12),
                cursor.getString(13));
        return movie;
    }

    public List<MovieDetail> getAllMovies() {
        List<MovieDetail> movieList = new ArrayList<MovieDetail>();

        String selectQuery = "SELECT  * FROM " + TABLE_MOVIE_DETAILS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                MovieDetail movie = new MovieDetail(cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(8),
                        cursor.getString(9),
                        cursor.getString(10),
                        cursor.getString(11),
                        cursor.getString(12),
                        cursor.getString(13));
                movieList.add(movie);
            } while (cursor.moveToNext());
        }
        return movieList;
    }

    public int getMovieCount() {
        String countQuery = "SELECT  * FROM " + TABLE_MOVIE_DETAILS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public void deleteMovie(MovieDetail movie) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MOVIE_DETAILS, KEY_ID + " = ?",
                new String[]{String.valueOf(movie.getId())});
        db.close();
    }
}
