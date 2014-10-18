package se.pikzel.assignment2.ex3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TimePicker;

import se.pikzel.assignment2.R;

/**
 * @author Pontus Palmenäs
 */
public class AddAlarmActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_alarm, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    public void onClick(View view) {
        TimePicker timePicker = (TimePicker)findViewById(R.id.alarmTimePicker);
        setResult(RESULT_OK,
                new Intent().putExtra("hour", timePicker.getCurrentHour())
                            .putExtra("minute", timePicker.getCurrentMinute())
        );
        finish();
    }
}
