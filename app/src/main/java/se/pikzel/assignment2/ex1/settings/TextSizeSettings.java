package se.pikzel.assignment2.ex1.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import se.pikzel.assignment2.R;

/**
 * @author Pontus Palmenäs
 */
public class TextSizeSettings extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.textsize_prefs);
    }
}
