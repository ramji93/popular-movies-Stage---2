package udacity.popularmovies_ver2.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import java.util.List;

/**
 * Created by e on 24-01-2016.
 */
public class MoviesContract {

    public static final String CONTENT_AUTHORITY = "udacity.popularmovies_ver2";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIES = "movies";

    public static final class MovieEntry implements BaseColumns {


        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;



        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String TABLE_NAME = "movies";

        public static final String COLUMN_ID = "id";

        public static final String COLUMN_NAME = "name";

        public static final String COLUMN_YEAR_RELEASED = "year";

        public static final String COLUMN_RATING = "rating";

        public static final String COLUMN_SYNOPSIS = "synopsis";

        public static final String COLUMN_IMAGE = "image";

        public static final Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static final Uri appendmovieUriwithid(long id)
        {

            return CONTENT_URI.buildUpon().appendPath(Long.toString(id)).build();

        }

         public static String getidfromuri(Uri uri)
         {
             List<String> list = uri.getPathSegments();

              return (list.get(1));

         }


    }





}
