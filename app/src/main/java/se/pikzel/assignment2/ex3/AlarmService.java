package se.pikzel.assignment2.ex3;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;

/**
 * @author Pontus Palmenäs
 */
public class AlarmService {
    private AlarmManager manager;
    private PowerManager.WakeLock wakeLock;
    private Context context;

    public AlarmService(Context context) {
        this.context = context;
    }

    public void register() {
    }
}

