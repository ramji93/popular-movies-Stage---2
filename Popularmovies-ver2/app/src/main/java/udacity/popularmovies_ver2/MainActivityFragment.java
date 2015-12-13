package udacity.popularmovies_ver2;

import android.content.Intent;
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


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment  extends Fragment  {


    GridView gridView;

    public MainActivityFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();

        String sort_value = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(getString(R.string.sort_key),getString(R.string.popularity_value));

        Fetchmovieinfo fetchmovieinfo = new Fetchmovieinfo();
        fetchmovieinfo.execute(sort_value);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
         View RootView =  inflater.inflate(R.layout.fragment_main, container, false);

        gridView =  (GridView)  RootView.findViewById(R.id.gridview);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Movie movieitem = (Movie)  gridView.getAdapter().getItem(position);


                Intent intent = new Intent(getActivity(),DescriptionActivity.class);

                intent.putExtra("rating", movieitem.getRating());
                intent.putExtra("synopsis",movieitem.getSynopsis());
                intent.putExtra("title",movieitem.getTitle());
                intent.putExtra("poster",movieitem.getPoster());
                intent.putExtra("date",movieitem.getDate());

                startActivity(intent);
            }
        });



        return RootView;
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
                        .appendQueryParameter("sort_by",params[0]).appendQueryParameter("api_key", "your_api_key").build();


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

            MyAdapter myAdapter = new MyAdapter(getActivity(),movies);

            gridView.setAdapter(myAdapter);

        }
    }








}
