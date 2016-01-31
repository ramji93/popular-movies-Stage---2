package udacity.popularmovies_ver2.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by e on 24-01-2016.
 */
public class MoviesProvider extends ContentProvider {


    private static Moviedbhelper moviedbhelper;

    private static final int MOVIES = 100;

    private static final int MOVIE_WITHID = 110;

    static UriMatcher mUrimatcher = buildurimatcher();

    static UriMatcher buildurimatcher()
    {
        UriMatcher um = new UriMatcher(UriMatcher.NO_MATCH);

        um.addURI(MoviesContract.CONTENT_AUTHORITY, MoviesContract.PATH_MOVIES,100);


        um.addURI(MoviesContract.CONTENT_AUTHORITY, MoviesContract.PATH_MOVIES+"/*",110);

        return um;
    }

    @Override
    public boolean onCreate() {

        moviedbhelper =  new Moviedbhelper(getContext());

        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor cursor;

        switch (mUrimatcher.match(uri)) {
            case MOVIES: {

                SQLiteDatabase db = moviedbhelper.getWritableDatabase();

                cursor = db.query(MoviesContract.MovieEntry.TABLE_NAME, projection, null, null, null, null, null);

                break;

            }

            case MOVIE_WITHID: {

                SQLiteDatabase db = moviedbhelper.getWritableDatabase();

                String id = MoviesContract.MovieEntry.getidfromuri(uri);

                cursor = db.query(MoviesContract.MovieEntry.TABLE_NAME, projection, MoviesContract.MovieEntry.COLUMN_ID + " = ?", new String[]{id}, null, null, null);

                break;
            }

            default:

                throw new UnsupportedOperationException("unsupported uri = " + uri);

        }

        cursor.setNotificationUri(getContext().getContentResolver(),uri);

            return cursor;
    }

    @Override
    public String getType(Uri uri) {



        switch (mUrimatcher.match(uri))
        {

            case MOVIES:
                return MoviesContract.MovieEntry.CONTENT_TYPE;

            case MOVIE_WITHID:
                return MoviesContract.MovieEntry.CONTENT_ITEM_TYPE;

            default:
                throw new UnsupportedOperationException("Unknown Uri" + uri);
        }



    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        Uri returnuri;

        switch (mUrimatcher.match(uri)) {

            case MOVIES: {
                SQLiteDatabase db = moviedbhelper.getWritableDatabase();

                    long rowid = db.insert(MoviesContract.MovieEntry.TABLE_NAME, null, values);



                if(rowid > 0) {

                    returnuri = MoviesContract.MovieEntry.buildMovieUri(rowid);
                }
                else

                    throw new android.database.SQLException("Failed to insert row into " + uri);


                break;
            }

            default: {

                throw new UnsupportedOperationException("Unsupported uri = " + uri);

            }

        }

            getContext().getContentResolver().notifyChange(uri,null);

            return returnuri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        int ret;

        switch(mUrimatcher.match(uri))
        {
            case MOVIES: {

                SQLiteDatabase db = moviedbhelper.getWritableDatabase();

                if (selection != null) {
                    ret = db.delete(MoviesContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
                } else {
                    ret = db.delete(MoviesContract.MovieEntry.TABLE_NAME, "1", null);
                }

                break;
            }

            default:{

                throw new UnsupportedOperationException("Unsupported uri = " + uri);
            }
        }

        if(ret>0)
        getContext().getContentResolver().notifyChange(uri,null);

        return ret;

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        int ret;

        switch (mUrimatcher.match(uri)) {
            case MOVIES: {

                SQLiteDatabase db = moviedbhelper.getWritableDatabase();

                ret = db.update(MoviesContract.MovieEntry.TABLE_NAME, values, selection, selectionArgs);

                break;
            }

            default:

                throw new UnsupportedOperationException("Unsupported uri = " + uri);

        }

        if (ret > 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return ret;

      }

}