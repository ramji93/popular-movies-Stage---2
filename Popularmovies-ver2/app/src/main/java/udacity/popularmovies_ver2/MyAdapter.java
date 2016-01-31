package udacity.popularmovies_ver2;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by e on 12-12-2015.
 */
public class MyAdapter extends BaseAdapter {
    private Context mContext;

    public static List<Movie> movies;

    public static boolean mTwopane;

    public MyAdapter(Context c, List<Movie> list,boolean Twopane) {


        movies = list;
        mContext = c;
        mTwopane = Twopane;
    }

    public int getCount() {
        return movies.size();
    }

    public Object getItem(int position) {
        return movies.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            if(mTwopane)
            {
                imageView.setLayoutParams(new GridView.LayoutParams(350,450));

                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            }
            else {
                imageView.setLayoutParams(new GridView.LayoutParams(500, 650));
                imageView.setPadding(8, 8, 8, 8);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }

        } else {
            imageView = (ImageView) convertView;
        }


        Picasso.with(mContext).load("http://image.tmdb.org/t/p/w185/" + movies.get(position).getPoster()).into(imageView);


        return imageView;
    }

}

