package udacity.popularmovies_ver2;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import udacity.popularmovies_ver2.data.MoviesContract;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment  extends Fragment  {


    GridView gridView;

    public String sort_value;

    public int mPosition=gridView.INVALID_POSITION;

    public Bundle savedbundle;


    public String detail_tag = "DESCRIPTION FRAGMENT TAG";

    public MainActivityFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();


        sort_value = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(getString(R.string.sort_key),getString(R.string.popularity_value));

       // sort_value = "favourites";


        if(sort_value.equals(getString(R.string.favourites)))
        {

            Cursor c = getActivity().getContentResolver().query(MoviesContract.MovieEntry.CONTENT_URI,new String[]{MoviesContract.MovieEntry.COLUMN_ID, MoviesContract.MovieEntry.COLUMN_IMAGE},null,null,null);
            MovieAdapter movieAdapter = new MovieAdapter(getActivity(),c,((MainActivity) getActivity()).mTwoPane);
            gridView.setAdapter(movieAdapter);
        }

        else

        {
            ConnectivityManager cm =
                    (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

             if(activeNetwork != null && activeNetwork.isConnectedOrConnecting())

             {

                 Fetchmovieinfo fetchmovieinfo = new Fetchmovieinfo();
                 fetchmovieinfo.execute(sort_value);

             }

            else
             {
                 Toast.makeText(getActivity(),"select favourites in the settings for offline mode",Toast.LENGTH_LONG).show();

             }

        }


        if(savedbundle!=null)
        {
            if(savedbundle.containsKey("position")&& savedbundle.containsKey("sortby")&& savedbundle.getString("sortby").equals(sort_value))
            {
//Code to handle savedinstance bundle

            }
        }



    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
         View RootView =  inflater.inflate(R.layout.fragment_main, container, false);


        savedbundle = savedInstanceState;

        gridView =  (GridView) RootView.findViewById(R.id.gridview);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

               if(sort_value.equals(getString(R.string.favourites)))
               {


                   Cursor c = (Cursor) gridView.getAdapter().getItem(position);

                   ((Callback)getActivity()).Onitemselected_favourites(MoviesContract.MovieEntry.appendmovieUriwithid(c.getInt(0)));

                   c.close();

                   mPosition = position;

               }

               else

               {
                   Movie movieitem = (Movie) gridView.getAdapter().getItem(position);

                   ((Callback)getActivity()).onitemselected_notfavourites(movieitem);

                   mPosition = position;

               }
            }
        });



        return RootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {


        super.onSaveInstanceState(outState);

        if(mPosition!=gridView.INVALID_POSITION)
        {
            outState.putInt("position",mPosition);
            outState.putString("sortby",sort_value);
        }

    }

    public interface Callback

    {

       public void Onitemselected_favourites(Uri uri);

       public void onitemselected_notfavourites(Movie movie);



    }


    public List<Movie> jsonformatter(String jsonstring) {

        ArrayList<Movie> movies = new ArrayList<>();

        try {


            JSONObject object = new JSONObject(jsonstring);

            JSONArray results = object.getJSONArray("results");

            for(int i=0;i<results.length();i++)
            {


                JSONObject movieobject = results.getJSONObject(i);

                Movie movie = new Movie();

               movie.setId(movieobject.getInt("id"));

                movie.setDate(movieobject.getString("release_date"));

                movie.setPoster(movieobject.getString("poster_path"));

                movie.setSynopsis(movieobject.getString("overview"));

                movie.setRating(Double.toString(movieobject.getDouble("vote_average")));

                movie.setTitle(movieobject.getString("title"));



                movies.add(movie);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }



        return movies;
    }








    class Fetchmovieinfo extends AsyncTask<String,Void,List<Movie>> {

        @Override
        protected List<Movie> doInBackground(String... params) {

            HttpURLConnection connection = null;

            String tag = getClass().getName();

            BufferedReader  bufferedReader = null;


            String jsonstring = null;

            try {

//
                Uri uri = Uri.parse("http://api.themoviedb.org/3/discover/movie").buildUpon()
                        .appendQueryParameter("sort_by",params[0]).appendQueryParameter("api_key", "your api key").build();


                URL url = new URL(uri.toString());

                connection = (HttpURLConnection)  url.openConnection();

                connection.setRequestMethod("GET");

                connection.connect();

                InputStream inputStream;

                inputStream = connection.getInputStream();

                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuffer bufferstring = new StringBuffer();

                String line;

                while ((line=bufferedReader.readLine()) != null )
                {

                    bufferstring.append(line+"\n");

                }

                if (bufferstring == null) {

                   return  null;

                }

                jsonstring = bufferstring.toString();

               // Log.v(tag, jsonstring);

            }


            catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            finally {
                connection.disconnect();
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            return jsonformatter(jsonstring);
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            super.onPostExecute(movies);

            MyAdapter myAdapter = new MyAdapter(getActivity(),movies,((MainActivity) getActivity()).mTwoPane);

            gridView.setAdapter(myAdapter);

        }
    }








}
