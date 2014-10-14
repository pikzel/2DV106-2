package se.pikzel.assignment2.ex1.settings;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import java.util.List;

import se.pikzel.assignment2.R;

/**
 * @author Pontus Palmenäs
 */
public class SettingsActivity extends PreferenceActivity {
    private static final String[] validFragments = {
            TextSizeSettings.class.getName(),
            BackgroundColorSettings.class.getName() };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.prefs_headers, target);
    }

    @Override
    protected boolean isValidFragment(String fragmentName) {
        boolean isValid = false;
        for (String s : validFragments) {
            if (s.equals(fragmentName)) {
                isValid = true;
            }
        }
        return isValid;
    }
}
