package udacity.popularmovies_ver2.data;

import android.test.AndroidTestCase;
import android.util.Log;

/**
 * Created by e on 24-01-2016.
 */
public class Testdb extends AndroidTestCase {

    public  void testdelete()
    {

        getContext().deleteDatabase(Moviedbhelper.DATABASE_NAME);
        Log.i("inside deletedb","database deleted");
    }

}
