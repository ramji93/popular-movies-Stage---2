package udacity.popularmovies_ver2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;


public class DescriptionActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        DescriptionActivityFragment descriptionActivityFragment = new DescriptionActivityFragment();

        Bundle bundle = new Bundle();

         Intent intent = getIntent();

         if(intent.getData()!=null)
         {
             bundle.putParcelable("uri",intent.getData());

         }

        else
         {
             bundle.putString("rating",getIntent().getStringExtra("rating"));
             bundle.putString("synopsis",getIntent().getStringExtra("synopsis"));
             bundle.putString("title",getIntent().getStringExtra("title"));
             bundle.putString("poster",getIntent().getStringExtra("poster"));
             bundle.putString("date",getIntent().getStringExtra("date"));
             bundle.putInt("id",getIntent().getIntExtra("id",0));

         }

        descriptionActivityFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().replace(R.id.detailfragment,descriptionActivityFragment).commit();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_description, menu);
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
}
