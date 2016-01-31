package udacity.popularmovies_ver2;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;
import udacity.popularmovies_ver2.data.MoviesContract;


/**
 * A placeholder fragment containing a simple view.
 */
public class DescriptionActivityFragment extends Fragment {



    ViewGroup viewGroup;

    public  int mId;

    public String mName;

    public String mSynopsis;

    public String mRating;

    public String mYearrealeased;

    public Bitmap mimage;

    String[] detail_projection = {MoviesContract.MovieEntry.COLUMN_ID, MoviesContract.MovieEntry.COLUMN_NAME, MoviesContract.MovieEntry.COLUMN_RATING, MoviesContract.MovieEntry.COLUMN_YEAR_RELEASED, MoviesContract.MovieEntry.COLUMN_SYNOPSIS, MoviesContract.MovieEntry.COLUMN_IMAGE};

    ShareActionProvider shareActionProvider;

    public DescriptionActivityFragment() {
    }

    public class response{
        int id;
        result_trailer[] results;

    }

    public class result_trailer{

        String id;
        String iso_639_1;
        String key;
        String name;
        String size;
        String type;
        String site;

    }

    public class response_review{
        String id;
        int page;
        result_review[] results;
        int total_pages;
        int total_results;
    }

    public class result_review{

        String id;
        String author;
        String content;
        String url;
    }




    public interface Movieapi{

        @GET("3/movie/{movieid}/videos?api_key= your api key")
        Call<response> fetchtrailer(@Path("movieid") String movieid);

        @GET("3/movie/{movieid}/reviews?api_key= your api key")
        Call<response_review> fetchreviews(@Path("movieid") String movieid);



    }


 class trailerclicklistener implements View.OnClickListener
 {
     @Override
     public void onClick(View v) {

         Intent intent = new Intent();
         intent.setAction(Intent.ACTION_VIEW);
         intent.setData(Uri.parse("https://www.youtube.com/watch?v="+ v.getTag()));
         if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
             startActivity(intent);
         }
         }
 }

    class reviewclicklistener implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData((Uri) v.getTag());
            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivity(intent);
            }
        }
    }


    class fetchimage extends AsyncTask<String,Void,Bitmap>
    {


        @Override
        protected Bitmap doInBackground(String... params) {

            Bitmap img = null;

            try {
                img = Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w185/" + params[0]).get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return img;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            mimage = bitmap;

            insert_movie();

        }
    }


    class favbuttonlistener implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {

            fetchimage f = new fetchimage();

            String posterpath = getActivity().getIntent().getStringExtra("poster");

            ConnectivityManager cm =
                    (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

            if(activeNetwork != null && activeNetwork.isConnectedOrConnecting())

            {
                f.execute(posterpath);

            }
            else

            {
                Toast.makeText(getActivity(),"Need internet connection to perform this operation",Toast.LENGTH_LONG).show();
            }

        }
    }

    public void insert_movie()
    {


        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        mimage.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        ContentValues values = new ContentValues();
        values.put(MoviesContract.MovieEntry.COLUMN_IMAGE,byteArray);
        values.put(MoviesContract.MovieEntry.COLUMN_ID,(int)mId);
        values.put(MoviesContract.MovieEntry.COLUMN_NAME,mName);
        values.put(MoviesContract.MovieEntry.COLUMN_RATING,mRating);
        values.put(MoviesContract.MovieEntry.COLUMN_SYNOPSIS, mSynopsis);
        values.put(MoviesContract.MovieEntry.COLUMN_YEAR_RELEASED, mYearrealeased);

        try {

            Uri returi = getActivity().getContentResolver().insert(MoviesContract.MovieEntry.CONTENT_URI, values);

            long ret = ContentUris.parseId(returi);

            if(ret>0)
            {

                Toast.makeText(getActivity(),"Added to favourites",Toast.LENGTH_LONG).show();
            }

        }

        catch (android.database.SQLException e)
        {



            Toast.makeText(getActivity(),"This movie already exists in favourites",Toast.LENGTH_LONG).show();

            e.printStackTrace();
        }

    }



    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootview = inflater.inflate(R.layout.fragment_description, container, false);

        Button favbutton = (Button) rootview.findViewById(R.id.fav_button);

        favbutton.setOnClickListener(new favbuttonlistener());

        TextView title = (TextView) rootview.findViewById(R.id.title);

        TextView date = (TextView) rootview.findViewById(R.id.date);

        ImageView poster = (ImageView) rootview.findViewById(R.id.image);

        TextView synopsis = (TextView) rootview.findViewById(R.id.synopsis);

        TextView rating = (TextView) rootview.findViewById(R.id.rating);


        Bundle bundle = getArguments();

        if(bundle!=null) {

            //if(intent.getData()!=null)

            if (bundle.getParcelable("uri") != null)

            {

                Cursor c = getActivity().getContentResolver().query((Uri) bundle.getParcelable("uri"), detail_projection, null, null, null);

                c.moveToFirst();

                mName = c.getString(c.getColumnIndex(MoviesContract.MovieEntry.COLUMN_NAME));
                mRating = c.getString(c.getColumnIndex(MoviesContract.MovieEntry.COLUMN_RATING));
                mYearrealeased = c.getString(c.getColumnIndex(MoviesContract.MovieEntry.COLUMN_YEAR_RELEASED));
                mSynopsis = c.getString(c.getColumnIndex(MoviesContract.MovieEntry.COLUMN_SYNOPSIS));
                mId = c.getInt(c.getColumnIndex(MoviesContract.MovieEntry.COLUMN_ID));
                byte[] bytea = c.getBlob(c.getColumnIndex(MoviesContract.MovieEntry.COLUMN_IMAGE));

                poster.setImageBitmap(BitmapFactory.decodeByteArray(bytea, 0, bytea.length));

                favbutton.setVisibility(View.GONE);

                c.close();

            } else {
                mName = bundle.getString("title");
                mYearrealeased = bundle.getString("date").substring(0, 4);
                mSynopsis = bundle.getString("synopsis");
                mRating = bundle.getString("rating");
                mId = bundle.getInt("id", 0);
                Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w185/" + bundle.getString("poster")).into(poster);

            }


            title.setText(mName);

            date.setText(mYearrealeased);

            synopsis.setText(mSynopsis);

            rating.setText(mRating);

//        poster.setDrawingCacheEnabled(true);
//
//        poster.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
//                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//        poster.layout(0, 0, poster.getMeasuredWidth(), poster.getMeasuredHeight());
//
//        poster.buildDrawingCache();


            viewGroup = (ViewGroup) rootview.findViewById(R.id.viewgroup);


            ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

            if (activeNetwork != null && activeNetwork.isConnectedOrConnecting())

            {

                Fetchlinks fetchlinks = new Fetchlinks();

                fetchlinks.execute(mId);

            }

        }

        setHasOptionsMenu(true);

        return rootview;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_desc_fragment, menu);

        MenuItem menuItem = menu.findItem(R.id.menu_item_share);

        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

    }

    class finallinks{

        result_review[] reviews;
        result_trailer[] trailers;

    }


     class Fetchlinks extends AsyncTask<Integer,Void,finallinks>
     {


         @Override
         protected finallinks doInBackground(Integer... params) {

             result_trailer[] trailers = null;
             result_review[] reviews = null;

            try {
                int id = params[0];

                Retrofit restAdapter = new Retrofit.Builder()
                        .baseUrl("http://api.themoviedb.org")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                Movieapi movieapi = restAdapter.create(Movieapi.class);

                Call<response> res = movieapi.fetchtrailer(Integer.toString(id));
                Response<response> resp1 = res.execute();
                response obj = resp1.body();

                trailers = obj.results;


                Call<response_review> res2 = movieapi.fetchreviews(Integer.toString(id));
                Response<response_review> resp2  = res2.execute();
                response_review obj1 = resp2.body();
                reviews = obj1.results;






            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
                  finallinks fl = new finallinks();
                  fl.reviews = reviews;
                  fl.trailers = trailers;

             return fl;

         }

         @Override
         protected void onPostExecute(finallinks fl) {
             super.onPostExecute(fl);

             result_review[] reviews = fl.reviews;
             result_trailer[] trailers = fl.trailers;


             if(trailers!=null && trailers.length!=0)
             {
                 TextView trailerlabel = new TextView(getActivity());
                 trailerlabel.setText("Trailers : ");
                 trailerlabel.setTextSize(20);
                 viewGroup.addView(trailerlabel);


                 for(int i=0;i<trailers.length;i++)
                 {

                    View rootview = (View) getActivity().getLayoutInflater().inflate(R.layout.trailer_layout,null);

                     Button button = (Button) rootview.findViewById(R.id.trailerbutton);

                     button.setText("trailer " + (i + 1));

                     button.setTag(trailers[i].key);

                     if(i==0)
                     {
                         Intent shareintent = new Intent(Intent.ACTION_SEND);
                         shareintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                         //shareintent.setData(Uri.parse("https://www.youtube.com/watch?v=" +trailers[i].key) );
                         shareintent.setType("text/plain");
                         shareintent.putExtra(Intent.EXTRA_TEXT,"https://www.youtube.com/watch?v="+trailers[0].key);

                         shareActionProvider.setShareIntent(shareintent);
                     }

                     button.setOnClickListener(new trailerclicklistener());

                     viewGroup.addView(rootview);

                 }
             }


             if(reviews!=null && reviews.length!=0)
             {
                 TextView reviewlabel = new TextView(getActivity());
                 reviewlabel.setText("Reviews : ");
                 reviewlabel.setTextSize(20);
                 reviewlabel.setPadding(0, 20, 0, 20);

                 viewGroup.addView(reviewlabel);


                 for(int i=0;i<reviews.length;i++)
                 {
                     Button button = new Button(getActivity());

                     button.setText("review " + (i + 1));

                     button.setTag(Uri.parse(reviews[i].url));

                     button.setOnClickListener(new reviewclicklistener());

                     viewGroup.addView(button);

                 }
             }



         }
     }


}
