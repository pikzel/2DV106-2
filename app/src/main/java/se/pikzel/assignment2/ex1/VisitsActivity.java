package se.pikzel.assignment2.ex1;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import se.pikzel.assignment2.Message;
import se.pikzel.assignment2.R;
import se.pikzel.assignment2.ex1.settings.BackgroundColor;
import se.pikzel.assignment2.ex1.settings.SettingsActivity;
import se.pikzel.assignment2.ex1.settings.TextSize;
import se.pikzel.assignment2.ex1.settings.VisitSettings;

/**
 * @author Pontus Palmenäs
 */
public class VisitsActivity extends Activity {
    private ListView listView;
    private ArrayAdapter<Visit> listAdapter;
    private Activity mainActivity;
    private DataSource dataSource;
    private List<Visit> visits;
    private VisitSettings settings;
    private String textSize = TextSize.MEDIUM; // Todo Can it be converted to local field?
    private VisitSortOrder sortOrder = VisitSortOrder.YEAR;
    private final int MENU_DELETE = 0;
    private final int MENU_EDIT = 1;
    private final int REQUEST_CREATE = 0;
    private final int REQUEST_EDIT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings = new VisitSettings(this);

        final String prefSortedBy = settings.getSortedBy(VisitSortOrder.YEAR);

        setContentView(R.layout.activity_visits);
        mainActivity = this;
        dataSource = new DataSource(this);

        getVisitsFromDatabase(VisitSortOrder.valueOf(prefSortedBy));

        listView = (ListView) findViewById(R.id.listView);
        listAdapter = new ArrayAdapter<Visit>(this, R.layout.list_item, visits) {
            @Override
            public View getView(int position, View row, ViewGroup parent) {
                row = getLayoutInflater().inflate(R.layout.list_item, parent, false);
                TextView listItem = (TextView)row.findViewById(R.id.listItem);
                textSize = settings.getTextSize(TextSize.MEDIUM);
                if (textSize.equals(TextSize.SMALL)) {
                    listItem.setTextSize(10);
                } else if (textSize.equals(TextSize.MEDIUM)) {
                    listItem.setTextSize(20);
                } else if (textSize.equals(TextSize.LARGE)) {
                    listItem.setTextSize(30);
                }
                listItem.setText(getItem(position).toString());
                return listItem;
            }
        };
        listView.setAdapter(listAdapter);

        registerForContextMenu(listView);

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void getVisitsFromDatabase(VisitSortOrder sortOrder) {
        dataSource.open();
        visits = dataSource.getAllVisits(sortOrder);
        dataSource.close();
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
            final Intent intent = new Intent(mainActivity, EditVisitsActivity.class);
            intent.putExtra(Visit.ID, visit.getId());
            intent.putExtra(Visit.POSITION, info.position); // This is a bit silly, but we need to keep track of the position in the list somehow, for the edit callback.
            mainActivity.startActivityForResult(intent, REQUEST_EDIT);
        }
        return true;
    }

    @Override
    protected void onResume() {
        textSize = settings.getTextSize(textSize);
        setBackgroundColor(settings.getBgColor(BackgroundColor.GRAY));
        listAdapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    protected void onPause() {
        dataSource.close();
        saveSortOrder();
        super.onPause();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.visits, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getGroupId() == R.id.menuGroupSortOrder) {
            onOptionsItemSelectedSortOrder(item);
        } else {
            switch (item.getItemId()) {
                case R.id.menuAddVisitedCountry:
                    final Intent intent = new Intent(mainActivity, AddVisitActivity.class);
                    mainActivity.startActivityForResult(intent, REQUEST_CREATE);
                    break;
                case R.id.menuSettings:
                    Intent settings = new Intent(this, SettingsActivity.class);
                    startActivity(settings);
                    break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Change sort order, save settings and reload list.
     */
    private void onOptionsItemSelectedSortOrder(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuVisitsSortByCountryDesc:
                sortOrder = VisitSortOrder.COUNTRY_DESC;
                break;
            case R.id.menuVisitsSortByCountry:
                sortOrder = VisitSortOrder.COUNTRY;
                break;
            case R.id.menuVisitsSortByYearDesc:
                sortOrder = VisitSortOrder.YEAR_DESC;
                break;
            case R.id.menuVisitsSortByYear:
                sortOrder = VisitSortOrder.YEAR;
                break;
            default:
                sortOrder = VisitSortOrder.YEAR;
        }
        saveSortOrder();
        getVisitsFromDatabase(sortOrder);
        reloadList();
    }

    private void saveSortOrder() {
        settings.saveSortOrder(sortOrder);
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
            final int year = result.getIntExtra(Visit.YEAR, -1);
            final String country = result.getStringExtra(Visit.COUNTRY);
            final Visit visit = createVisit(year, country);
            visits.add(visit);
            reloadList();
        }
    }

    private void reloadList() {
        listAdapter = new ArrayAdapter<Visit>(this, R.layout.list_item, visits);
        listView.setAdapter(listAdapter);
    }

    private void onActivityResultEdit(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            final int year = result.getIntExtra(Visit.YEAR, -1);
            final String country = result.getStringExtra(Visit.COUNTRY);
            final long id = result.getLongExtra(Visit.ID, -1);
            if (editVisit(id, year, country)) {
                final int position = result.getIntExtra(Visit.POSITION, 0);
                visits.set(position, new Visit(year, country));
                reloadList();
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

    public void setBackgroundColor(String color) {
        if (color.equals(BackgroundColor.GRAY)) {
            listView.setBackgroundColor(Color.DKGRAY);
        } else if (color.equals(BackgroundColor.BLUE)) {
            listView.setBackgroundColor(Color.BLUE);
        } else if (color.equals(BackgroundColor.MAGENTA)) {
            listView.setBackgroundColor(Color.MAGENTA);
        }
    }
}
