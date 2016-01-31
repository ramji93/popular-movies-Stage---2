package udacity.popularmovies_ver2;

import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by e on 24-01-2016.
 */
public class MovieAdapter extends BaseAdapter
{

    public static Cursor mCursor;
    public static Context mContext;
    public static boolean mTwopane;

    public MovieAdapter(Context context,Cursor cursor,boolean twopane)
    {

        mContext = context;
        mCursor = cursor;
        mTwopane = twopane;

    }


    @Override
    public int getCount() {
        return mCursor.getCount();
    }

    @Override
    public Object getItem(int position) {

         mCursor.moveToPosition(position);
        return mCursor;

    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
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

        mCursor.moveToPosition(position);
        byte[] bytea =  mCursor.getBlob(1);

        imageView.setImageBitmap(BitmapFactory.decodeByteArray(bytea,0,bytea.length));

        return imageView;
    }
}
