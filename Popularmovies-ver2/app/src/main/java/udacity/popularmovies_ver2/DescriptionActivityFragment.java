package udacity.popularmovies_ver2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


/**
 * A placeholder fragment containing a simple view.
 */
public class DescriptionActivityFragment extends Fragment {

    public DescriptionActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_description, container, false);

        TextView title = (TextView) rootview.findViewById(R.id.title);

        TextView date = (TextView) rootview.findViewById(R.id.date);

        ImageView poster = (ImageView) rootview.findViewById(R.id.image);

        TextView synopsis = (TextView) rootview.findViewById(R.id.synopsis);

        TextView rating = (TextView) rootview.findViewById(R.id.rating);

        Intent intent = getActivity().getIntent();

        title.setText(intent.getStringExtra("title"));

         date.setText(intent.getStringExtra("date"));

         synopsis.setText(intent.getStringExtra("synopsis"));

        rating.setText(intent.getStringExtra("rating"));

        Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w185/" + intent.getStringExtra("poster")).into(poster);

        return rootview;
    }
}
