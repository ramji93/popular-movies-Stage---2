package udacity.popularmovies_ver2;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;


/**
 * A placeholder fragment containing a simple view.
 */
public class SettingsActivityFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

    public SettingsActivityFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.sharedpreferences);

        Preference sort = findPreference(getString(R.string.sort_key));


        sort.setOnPreferenceChangeListener(this);

        onPreferenceChange(sort,PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(sort.getKey(),""));


    }


    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

            String Value = (String) newValue;

                    //PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(getString(R.string.sort_key), getString(R.string.popularity_value));

            ListPreference sortpreference = (ListPreference) preference;

            int index = sortpreference.findIndexOfValue(Value);

            sortpreference.setSummary(sortpreference.getEntries()[index]);

            return true;
    }
}
