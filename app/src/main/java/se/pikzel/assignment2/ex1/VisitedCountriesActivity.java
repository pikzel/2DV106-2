package se.pikzel.assignment2.ex1;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import se.pikzel.assignment2.Message;
import se.pikzel.assignment2.R;

/**
 * @author Pontus Palmenäs
 */
public class VisitedCountriesActivity extends Activity {
    private ListView listView;
    private ArrayAdapter<Visit> listAdapter;
    private Activity mainActivity;
    private DataSource dataSource;
    private List<Visit> visits;
    private final int MENU_DELETE = 0;
    private final int MENU_EDIT = 1;
    private final int REQUEST_CREATE = 0;
    private final int REQUEST_EDIT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visited_countries);

        mainActivity = this; // For intents

        dataSource = new DataSource(this);

        dataSource.open();
        visits = dataSource.getAllVisits();
        dataSource.close();

        listView = (ListView) findViewById(R.id.listView);
        listAdapter = new ArrayAdapter<Visit>(this, R.layout.list_item, visits);
        listView.setAdapter(listAdapter);
        registerForContextMenu(listView);

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.listView) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle(visits.get(info.position).toString());
            menu.add(0, MENU_DELETE, MENU_DELETE, R.string.menuDeleteVisit);
            menu.add(0, MENU_EDIT, MENU_EDIT, R.string.menuEditVisit);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (item.getItemId() == MENU_DELETE) {
            final Visit visit = visits.get(info.position);
            dataSource.deleteVisit(visit);
            listAdapter.remove(visit);
        } else if (item.getItemId() == MENU_EDIT) {
            final Visit visit = visits.get(info.position);
            final Intent intent = new Intent(mainActivity, EditVisitedCountriesActivity.class);
            intent.putExtra(Visit.ID, visit.getId());
            intent.putExtra(Visit.POSITION, info.position); // This is a bit silly, but we need to keep track of the position in the list somehow, for the edit callback.
            mainActivity.startActivityForResult(intent, REQUEST_EDIT);
        }
        return true;
    }

    @Override
    protected void onResume() {
        dataSource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        dataSource.close();
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // XML based menu, specified in res/menu/visited_countries.xml
        getMenuInflater().inflate(R.menu.visited_countries, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.addCountryMenu) {
            final Intent intent = new Intent(mainActivity, AddCountryActivity.class);
            mainActivity.startActivityForResult(intent, REQUEST_CREATE);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * When the activity receives results, create visit and update the list
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        switch (requestCode) {
            case REQUEST_CREATE:
                onActivityResultCreate(resultCode, result);
                break;
            case REQUEST_EDIT:
                onActivityResultEdit(resultCode, result);
                break;
        }

    }

    private void onActivityResultCreate(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            int year = result.getIntExtra(Visit.YEAR, -1);
            String country = result.getStringExtra(Visit.COUNTRY);
            Visit visit = createVisit(year, country);
            visits.add(visit);
            listView.setAdapter(listAdapter); // Force ListView to update
        }
    }

    private void onActivityResultEdit(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            int year = result.getIntExtra(Visit.YEAR, -1);
            String country = result.getStringExtra(Visit.COUNTRY);
            long id = result.getLongExtra(Visit.ID, -1);
            if (editVisit(id, year, country)) {
                int position = result.getIntExtra(Visit.POSITION, 0);
                visits.set(position, new Visit(year, country));
                listView.setAdapter(listAdapter); // Force ListView to update
            } else {
                new Message(this).showErrorMessage("Edit failed");
            }
        }

    }

    private Visit createVisit(int year, String country) {
        dataSource.open();
        Visit visit = dataSource.createVisit(year, country);
        dataSource.close();
        return visit;
    }

    private boolean editVisit(long visitId, int year, String country) {
        dataSource.open();
        boolean success = dataSource.updateVisit(visitId, year, country);
        dataSource.close();
        return success;
    }
}
