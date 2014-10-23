package se.pikzel.assignment2.ex3;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import se.pikzel.assignment2.R;
import se.pikzel.assignment2.UIMessage;

/**
 * @author Pontus Palmenäs
 */
public class AlarmsActivity extends Activity {
    private ArrayAdapter<Alarm> listAdapter;
    private List<Alarm> alarms;
    private final int MENU_DELETE = 0;
    private AlarmService alarmService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarms);
        alarms = loadAlarms();
        ListView listView = (ListView) findViewById(R.id.listView);
        listAdapter = new ArrayAdapter<Alarm>(this, R.layout.list_item, alarms);
        listView.setAdapter(listAdapter);
        registerForContextMenu(listView);

        checkAndCreateSaveFile();

        alarmService = new AlarmService(this);

        startCurrentTimeHandler();
    }

    private void checkAndCreateSaveFile() {

    }

    private void error(String s) {
        new UIMessage(this).showErrorMessage(s);
    }

    private void saveAlarms() {
        AlarmFileHandler storage = new AlarmFileHandler(this);
        try {
            storage.save(alarms);
        } catch (IOException e) {
            error("Could not save file. I/O error.");
            Log.e("ExternalStorage", "Write error.", e);
        }
    }

    private List<Alarm> loadAlarms() {
        AlarmFileHandler storage = new AlarmFileHandler(this);
        List<Alarm> list = new ArrayList<Alarm>();
        try {
            list = storage.load();
        } catch (IOException e) {
            error("Could not load file. I/O error.");
            Log.e("ExternalStorage", "Read error.", e);
        } catch (ClassNotFoundException e) {
            error("Could not load file. Data error.");
            Log.e("ExternalStorage", "Read error.", e);
        }
        return list;
    }

    @Override
    protected void onResume() {
        alarms = loadAlarms();
        super.onResume();
    }

    @Override
    protected void onPause() {
        saveAlarms();
        super.onPause();
    }

    @Override
     public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.alarms, menu);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.listView) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle(alarms.get(info.position).toString());
            menu.add(0, MENU_DELETE, MENU_DELETE, R.string.menuDeleteAlarm);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (item.getItemId() == MENU_DELETE) {
            removeAlarm(info);
        }
        return true;
    }

    private void removeAlarm(AdapterView.AdapterContextMenuInfo info) {
        alarmService.remove(alarms.get(info.position));
        alarms.remove(info.position);
        reloadList();
        saveAlarms();
    }

    private void reloadList() {
        listAdapter.clear();
        listAdapter.addAll(alarms);
        listAdapter.notifyDataSetChanged();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuAddAlarm:
                final Intent intent = new Intent(this, AddAlarmActivity.class);
                this.startActivityForResult(intent, 0);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            int hour = data.getIntExtra("hour", 0);
            int minute = data.getIntExtra("minute", 0);
            addAlarm(hour, minute);
            reloadList();
            saveAlarms();
        }
    }

    /**
     * Create an alarm and assign it an id using shared preference.
     * Add the id to the global list and set it in the alarm manager.
     */
    private void addAlarm(int hour, int minute) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int newId = prefs.getInt("alarmId", 0) + 1;
        Alarm alarm = new Alarm(newId, hour, minute);
        alarms.add(alarm);
        alarmService.set(alarm);
        prefs.edit().putInt("alarmId", newId).apply();
    }

    /**
     * Start a timer that sends a message every five seconds to update the current time in the UI.
     */
    private void startCurrentTimeHandler() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0); // Just send an empty message to the handler, as a heartbeat.
            }
        };
        new Timer().scheduleAtFixedRate(task, 0, 5000);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            showCurrentTime();
        }
    };

    private void showCurrentTime() {
        TextView textView = (TextView)findViewById(R.id.alarmCurrentTime);
        final Calendar calendar = Calendar.getInstance();
        textView.setText(Alarm.pad(calendar.get(Calendar.HOUR_OF_DAY))
                + ":" + Alarm.pad(calendar.get(Calendar.MINUTE))
                + ":" + Alarm.pad(calendar.get(Calendar.SECOND)));
    }
}
