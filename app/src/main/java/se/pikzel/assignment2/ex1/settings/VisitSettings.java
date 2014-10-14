package se.pikzel.assignment2.ex1.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import se.pikzel.assignment2.ex1.VisitSortOrder;

/**
 * @author Pontus Palmenäs
 */
public class VisitSettings {
    private final String prefNameSortedBy = "sortedBy";

    private SharedPreferences sharedPreferences;

    public VisitSettings(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public String getSortedBy(VisitSortOrder defaultSortOrder) {
        return sharedPreferences.getString(prefNameSortedBy, defaultSortOrder.name());
    }

    public String getTextSize(String defaultTextSize) {
        String prefNameTextSize = "textSize";
        return sharedPreferences.getString(prefNameTextSize, defaultTextSize);
    }

    public String getBgColor(String defaultColor) {
        String prefNameBgColor = "backgroundColor";
        return sharedPreferences.getString(prefNameBgColor, defaultColor);
    }

    public void saveSortOrder(VisitSortOrder sortOrder) {
        sharedPreferences.edit().putString(prefNameSortedBy, sortOrder.name()).apply();
    }
}
