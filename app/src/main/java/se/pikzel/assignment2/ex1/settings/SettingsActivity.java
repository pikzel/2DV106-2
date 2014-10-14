package se.pikzel.assignment2.ex1.settings;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import java.util.List;

import se.pikzel.assignment2.R;

/**
 * @author Pontus Palmenäs
 */
public class SettingsActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onBuildHeaders(List<Header> target) {
        // TODO Auto-generated method stub
        loadHeadersFromResource(R.xml.prefs_headers, target);
    }

    @Override
    protected boolean isValidFragment(String fragmentName) {
        return TextSizeSettings.class.getName().equals(fragmentName);
    }
}
