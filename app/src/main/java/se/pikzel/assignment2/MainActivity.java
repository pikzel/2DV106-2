package se.pikzel.assignment2;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.pikzel.assignment2.ex1.VisitedCountriesActivity;

/**
 * @author Pontus Palmenäs
 */
public class MainActivity extends ListActivity {

    private List<String> activities = new ArrayList<String>();
    private Map<String, Class> name2class = new HashMap<String, Class>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupActivities();
        setListAdapter(new ArrayAdapter<String>(this, R.layout.activity_main, activities));

        ListView lv = getListView();
        lv.setOnItemClickListener(new OnItemClick());
    }

    private void setupActivities() {
        addActivity("Visited Countries", VisitedCountriesActivity.class);
    }

    private void addActivity(String name, Class activity) {
        activities.add(name);
        name2class.put(name, activity);
    }

    // Handle click on item in list
    private class OnItemClick implements AdapterView.OnItemClickListener {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // Get and start activity
            String activity_name = activities.get(position);
            Class activity_class = name2class.get(activity_name);
            Intent intent = new Intent(MainActivity.this, activity_class);
            MainActivity.this.startActivity(intent);
        }
    }

}
