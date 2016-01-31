package udacity.popularmovies_ver2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by e on 24-01-2016.
 */
public class Moviedbhelper extends SQLiteOpenHelper
{
    public final static String DATABASE_NAME = "movie.db";

    public final static int DATABASE_VERSION = 2;

    public Moviedbhelper(Context context)
    {

        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

         final String CREATE_STATEMENT = "CREATE TABLE "+MoviesContract.MovieEntry.TABLE_NAME+" ("+

                MoviesContract.MovieEntry.COLUMN_ID + " INTEGER PRIMARY KEY, " +
                MoviesContract.MovieEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                MoviesContract.MovieEntry.COLUMN_RATING + " TEXT NOT NULL, " +
                MoviesContract.MovieEntry.COLUMN_SYNOPSIS + " TEXT NOT NULL, " +
                MoviesContract.MovieEntry.COLUMN_YEAR_RELEASED + " TEXT NOT NULL, " +
                MoviesContract.MovieEntry.COLUMN_IMAGE + " BLOB NOT NULL)";

        Log.i("creating database",CREATE_STATEMENT);


        db.execSQL(CREATE_STATEMENT);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}


