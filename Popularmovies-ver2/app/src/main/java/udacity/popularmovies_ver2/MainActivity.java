package udacity.popularmovies_ver2;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity implements MainActivityFragment.Callback {


    public static boolean mTwoPane;

    public String detail_tag = "DESCRIPTION FRAGMENT TAG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(findViewById(R.id.detailfragment)!=null)
        {

            mTwoPane = true;


        }

        else
        {
            mTwoPane =false;
        }



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Intent intent = new Intent(this,SettingsActivity.class);

            startActivity(intent);


            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void Onitemselected_favourites(Uri uri) {

        if(mTwoPane)
        {
            DescriptionActivityFragment descriptionActivityFragment = new DescriptionActivityFragment();

            Bundle bundle = new Bundle();

            bundle.putParcelable("uri",uri);

            descriptionActivityFragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction().replace(R.id.detailfragment,descriptionActivityFragment,detail_tag).commit();

        }
        else
        {

            Intent intent = new Intent(this,DescriptionActivity.class);
            intent.setData(uri);
            startActivity(intent);

        }

    }

    @Override
    public void onitemselected_notfavourites(Movie movieitem) {

        if(mTwoPane)
        {

            DescriptionActivityFragment descriptionActivityFragment = new DescriptionActivityFragment();

            Bundle bundle = new Bundle();

            bundle.putString("rating", movieitem.getRating());
            bundle.putString("synopsis", movieitem.getSynopsis());
            bundle.putString("title", movieitem.getTitle());
            bundle.putString("poster", movieitem.getPoster());
            bundle.putString("date", movieitem.getDate());
            bundle.putInt("id", movieitem.getId());


            descriptionActivityFragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction().replace(R.id.detailfragment,descriptionActivityFragment,detail_tag).commit();


        }
        else
        {
            Intent intent = new Intent(this, DescriptionActivity.class);

            intent.putExtra("rating", movieitem.getRating());
            intent.putExtra("synopsis", movieitem.getSynopsis());
            intent.putExtra("title", movieitem.getTitle());
            intent.putExtra("poster", movieitem.getPoster());
            intent.putExtra("date", movieitem.getDate());
            intent.putExtra("id", movieitem.getId());

            startActivity(intent);

        }

    }
}
